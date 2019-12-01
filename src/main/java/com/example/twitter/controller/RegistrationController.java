package com.example.twitter.controller;

import com.example.twitter.entity.User;
import com.example.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping ("/login")
    public void login(Map<String, Object> model) {
        model.put("AuthUsers",true);
        model.put("regButton",true);

    }

    @GetMapping("/registration")
    public String registration(Map<String, Object> model) {
        model.put("AuthUsers",true);
        return "registration";
    }

    @PostMapping("/registration")
    public String addNewUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String password1,
            User user,
            Map<String,Object> model) {

        if (username.isEmpty() || password.isEmpty()) {
            model.put("q1","Fields can not be empty.");
            return "registration";
        }

       if (!password.equals(password1)) {
           model.put("q2","Different passwords.");
           return "registration";
       }

        if(!userService.addUser(user)) {
            model.put("q","Such login is already exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activateEmail(Map<String, String> model,
                                @PathVariable String code) {

        boolean isActivated = userService.activateUser(code);

        if(isActivated) {
            model.put("act","You are successfully activated.");
        }else {
            model.put("actNo","Activation code is already used.");
        }

        return "login";
    }
}
