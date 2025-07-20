package com.training.keycloak.sso.controller;

import com.training.keycloak.sso.dto.*;
import com.training.keycloak.sso.services.Keycloak2faTotp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TrainingController {

    @RequestMapping("/")
    public ModelAndView home(Model model, OAuth2AuthenticationToken authentication){
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("username", authentication.getPrincipal().getAttribute("preferred_username"));
        mav.addObject("userId", authentication.getPrincipal().getAttribute("sub"));
        return mav;
    }


}
