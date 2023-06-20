package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.services.UserService;
import com.example.demo.services.CustomUserDetailService;
import com.example.demo.validator.annotation.ValidUserId;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

import java.io.UnsupportedEncodingException;
import java.util.List;
@Controller
public class UserController {
    @Autowired
    private JavaMailSender mailSender;
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
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, HttpServletRequest request)
    {
        if(bindingResult.hasErrors())
        {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "user/register";
        }
        user.setVerificationCode(RandomString.make(30));
        user.setEnabled(false);

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        try {
            sendVerificationEmail(user, getSiteURL(request));
            model.addAttribute("message", "Registration successful. Please check your email to verify your account.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", "Failed to send verification email. Please try again later.");
        }
        return "redirect:/login";
    }
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "Your email address";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
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
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code, Model model) {
        if (userService.verify(code)) {
            model.addAttribute("message", "Congratulations, your account has been verified.");
        } else {
            model.addAttribute("error", "Sorry, we could not verify account. It maybe already verified,\n"
                    + "        or verification code is incorrect.");
        }
        return "auth/result_Verify_form";
    }

}
