package com.example.tutorial.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.tutorial.model.ApiResponse;
import com.example.tutorial.model.User;
import com.example.tutorial.repository.UserRepository;
import com.example.tutorial.util.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    public AuthController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // API để tạo token (login API)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("/login")
    public ApiResponse<HashMap<String,Object>> login(@RequestBody Map<String, Object> reqBody) throws JsonProcessingException {
        User user = userRepository.findByNameAndHashPassword(            
            (String) reqBody.get("name"),
            DigestUtils.sha256Hex((String) reqBody.get("password")));        
        if (user != null) {
            try {
                return new ApiResponse("000", "success", Map.of("token", jwtTokenUtil.generateToken(user)));
            } catch(IllegalArgumentException e) {
                return new ApiResponse("002", "Generate token fail", Map.of("errorMessage", e.getMessage()));
            }
        } else {
            return new ApiResponse("001", "Account not found", null);
        }
    }
}
