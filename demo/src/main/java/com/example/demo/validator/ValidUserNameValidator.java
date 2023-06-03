package com.example.demo.validator;

import com.example.demo.entity.User;
import com.example.demo.repository.IUserRepository;
import com.example.demo.validator.annotation.ValidUserName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidUserNameValidator implements ConstraintValidator<ValidUserName, String> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context)
    {
        if(userRepository == null)
        {
            return true;
        }
        else{
            return userRepository.findByUsername(username) == null;
        }
    }
}
