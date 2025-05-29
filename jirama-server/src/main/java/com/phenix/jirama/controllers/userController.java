package com.phenix.jirama.controllers;

import com.phenix.jirama.models.User;
import com.phenix.jirama.service.JWTService;
import com.phenix.jirama.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
public class userController {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/add")
    public User SaveUser(@RequestBody User user){
        return this.userService.SaveUser(user);
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> auth(Authentication authentication){
        System.out.println(authentication);
        String token = jwtService.generateJwt(authentication);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
