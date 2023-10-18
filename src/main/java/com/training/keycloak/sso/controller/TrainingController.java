package com.training.keycloak.sso.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrainingController {

    @RequestMapping("/")
    public ModelAndView test(Model model, OAuth2AuthenticationToken authentication){

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("username",authentication.getPrincipal().getAttribute("preferred_username"));
        model.addAttribute("username","test");
        return mav;
    }

    @RequestMapping("/customer")
    public ModelAndView testCust(Model model, OAuth2AuthenticationToken authentication){

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("username",authentication.getPrincipal().getAttribute("preferred_username"));
        model.addAttribute("username","test");

        return mav;
    }


}
