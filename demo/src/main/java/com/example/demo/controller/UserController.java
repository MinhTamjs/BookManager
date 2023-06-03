package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.services.UserService;
import com.example.demo.services.CustomUserDetailService;
import com.example.demo.validator.annotation.ValidUserId;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model)
    {
        if(bindingResult.hasErrors())
        {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "user/register";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }


    @GetMapping("/editUser")
    public String showEditUserForm(Model model) {
        // Retrieve the currently authenticated user and pass it to the view
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.getUserByUsername(username);
        model.addAttribute("user", currentUser);

        return "user/editUser";
    }

    @PostMapping("/editUser/update")
    public String updateUser(@ModelAttribute("user") User modifiedUser) {
        // Call the updateCurrentUser method in the UserService
        userService.updateCurrentUser(modifiedUser);

        // Redirect the user to the profile page or any other appropriate page
        return "redirect:/";
    }
}