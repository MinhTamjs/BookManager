package com.example.demo.services;

import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.entity.User;
import com.example.demo.repository.IRoleRepository;
import com.example.demo.repository.IUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private IRoleRepository roleRepository;
    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

    public void save(User user){
        userRepository.save(user);
        Long userId = userRepository.getUserIdByUsername(user.getUsername());
        Long roleId = roleRepository.getRoleIdByName("USER");
        if(roleId !=0 && userId !=0)
        {
            userRepository.addRoleToUser(userId, roleId);
        }
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public void updateCurrentUser(User modifiedUser) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username);

        if (currentUser != null) {
            // Update the user information with the modified date
            currentUser.setName(modifiedUser.getName());
            currentUser.setEmail(modifiedUser.getEmail());
            currentUser.setUsername(modifiedUser.getUsername());

            // Save the updated user to the database
            userRepository.save(currentUser);
        }
    }

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user != null) {
            user.setTokenforgotpassword(token);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Khong ton tai user co email " + email);
        }
    }

    public User getUserByTokenForgotPassWord(String token) {
        return userRepository.getUserBytokenforgotpassword(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setTokenforgotpassword(null);
        userRepository.save(user);
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
    }
}
