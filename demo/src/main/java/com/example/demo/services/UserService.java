package com.example.demo.services;

import com.example.demo.entity.User;
import com.example.demo.repository.IRoleRepository;
import com.example.demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

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


}
