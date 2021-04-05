package com.msmir.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
  @GetMapping(value = "/login")
  public String login(Model model){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if(auth != null && !(auth instanceof AnonymousAuthenticationToken)){
      if(auth.isAuthenticated()){
        return "redirect:/";
      }
    }
    return "login";
  }
}
