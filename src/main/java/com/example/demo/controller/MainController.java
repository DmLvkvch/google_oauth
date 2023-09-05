package com.example.demo.controller;

import com.example.demo.service.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class MainController {

    private final GoogleService googleService;

    @Autowired
    public MainController(GoogleService googleService) {
        this.googleService = googleService;
    }

    @GetMapping("/home")
    public String greeting(@CookieValue(value = "access_token",
            defaultValue = "unknown") String access_token,
                           Model model) {
        boolean loggedIn = false;
        String name = googleService.getUserName(access_token);
        
        if(name != null) {
            loggedIn = true;
        }
        else {
            name = "new user";
        }
        model.addAttribute("logged_in", loggedIn);
        model.addAttribute("name", name);
        return "index";
    }

    @GetMapping("/signin")
    public String signin(@CookieValue(value = "access_token",
                        defaultValue = "unknown") String access_token,
                         Model model) {
        if(googleService.getUserName(access_token) == null){
            model.addAttribute("logged_in", false);
            model.addAttribute("oauth_url", googleService.getOauthUrl());
        }
        else{
            String name = googleService.getUserName(access_token);
            model.addAttribute("name", name);
        }
        return"loginpage";
    }

    @GetMapping("/logout")
    @ResponseBody
    public String logout(HttpServletResponse response) {
        response.addCookie(new Cookie("access_token", "unknown"));
        return"/home";
    }

    @GetMapping("/code")
    @ResponseBody
    public RedirectView getCode(HttpServletResponse response,
                                @RequestParam(name = "code", required = false, defaultValue = "") String code) {
        Map<Object, Object> accessToken = googleService.getTokenResponse(code);
        int expires_in = ((Integer)accessToken.get("expires_in"));
        Cookie cookie = new Cookie("access_token", (String)accessToken.get("access_token"));
        cookie.setMaxAge(expires_in);
        response.addCookie(cookie);
        return new RedirectView("/home");
    }
}
