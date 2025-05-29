package com.phenix.jirama.service;

import com.phenix.jirama.Repository.UserRepository;
import com.phenix.jirama.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User SaveUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }
    public User findByUsername(String username){
        return this.userRepository.findByUsername(username).orElseThrow();
    }

}
