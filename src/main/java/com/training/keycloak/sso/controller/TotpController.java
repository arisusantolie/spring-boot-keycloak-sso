package com.training.keycloak.sso.controller;

import com.training.keycloak.sso.dto.*;
import com.training.keycloak.sso.services.Keycloak2faTotp;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller()
@RequestMapping("/totp")
public class TotpController {
    private final Keycloak2faTotp totpService;

    public TotpController(Keycloak2faTotp totpService) {
        this.totpService = totpService;
    }


    @GetMapping("/")
    public ModelAndView totpHome(OAuth2AuthenticationToken authentication) {
        ModelAndView mav = new ModelAndView("totp/index");
        String userId = authentication.getPrincipal().getAttribute("sub");
        String username = authentication.getPrincipal().getAttribute("preferred_username");

        // Check if user already has TOTP setup with default device name "MyDevice"
        boolean hasTotpSetup = false;
        try {
            hasTotpSetup = totpService.hasTotpSetup(userId);
        } catch (Exception e) {
            // If error checking, assume no setup
            hasTotpSetup = false;
        }

        mav.addObject("username", username);
        mav.addObject("userId", userId);
        mav.addObject("hasTotpSetup", hasTotpSetup);
        return mav;
    }
    @PostMapping("/generate")
    public String generateSecret(OAuth2AuthenticationToken authentication,
                                 Model model, RedirectAttributes redirectAttributes) {
        try {
            String userId = authentication.getPrincipal().getAttribute("sub");
            TotpSecretResponse response = totpService.generateTotpSecret(userId);

            model.addAttribute("username", authentication.getPrincipal().getAttribute("preferred_username"));
            model.addAttribute("userId", userId);
            model.addAttribute("secret", response);
            model.addAttribute("showSetup", true);


            // Check if user already has TOTP setup with default device name "MyDevice"
            boolean hasTotpSetup = false;
            try {
                hasTotpSetup = totpService.hasTotpSetup(userId);
            } catch (Exception e) {
                // If error checking, assume no setup
                hasTotpSetup = false;
            }
            model.addAttribute("hasTotpSetup", hasTotpSetup);


            return "totp/index";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to generate TOTP secret: " + e.getMessage());
            return "redirect:/totp/";
        }
    }

    @PostMapping("/register")
    public String registerTotp(@RequestParam String encodedSecret,
                               @RequestParam String initialCode,
                               @RequestParam(defaultValue = "false") boolean overwrite,
                               OAuth2AuthenticationToken authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            String userId = authentication.getPrincipal().getAttribute("sub");

            TotpRegisterRequest request = new TotpRegisterRequest("MyDevice", encodedSecret, initialCode, true);
            TotpRegisterorDeleteResponse response = totpService.registerTotpCredential(userId, request);

            redirectAttributes.addFlashAttribute("success", response.getMessage());
            return "redirect:/totp/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to register TOTP: " + e.getMessage());
            return "redirect:/totp/";
        }
    }

    @PostMapping("/delete")
    public String deleteTotp(OAuth2AuthenticationToken authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            String userId = authentication.getPrincipal().getAttribute("sub");

            TotpDeleteRequest request = new TotpDeleteRequest("MyDevice");
            TotpRegisterorDeleteResponse response = totpService.deleteTotpCredential(userId, request);

            redirectAttributes.addFlashAttribute("success", response.getMessage());
            return "redirect:/totp/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete TOTP: " + e.getMessage());
            return "redirect:/totp/";
        }
    }
}
