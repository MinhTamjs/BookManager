package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotPasswordController {
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "user/forgot_password_form";
    }
}
