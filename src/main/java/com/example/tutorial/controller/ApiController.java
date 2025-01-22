package com.example.tutorial.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tutorial.model.ApiResponse;
import com.example.tutorial.model.Book;
import com.example.tutorial.model.User;
import com.example.tutorial.repository.BookRepository;
import com.example.tutorial.repository.UserRepository;
import com.example.tutorial.util.JwtTokenUtil;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    private final JwtTokenUtil jwtTokenUtil;

    public ApiController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Value("${jwt_secret}")
    private String secret;

    @GetMapping("/test")
    public ApiResponse<String> test() {
        return new ApiResponse<String>("000", "success", secret);
    }

    @GetMapping("/get-all-users")
    public ApiResponse<Object> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return new ApiResponse<Object>("000", "success", Map.of(
                "userList", userList,
                "count", userList.size()));
    }

    @PostMapping("/add-user")
    public ApiResponse<User> addUser(@RequestBody Map<String, Object> reqBody) {
        User user = new User();
        user.setEmail((String) reqBody.get("email"));
        user.setName((String) reqBody.get("name"));
        user.setHashPassword(DigestUtils.sha256Hex((String) reqBody.get("password")));
        try {
            return new ApiResponse<User>("000", "success", userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            System.err.println(e);
            return new ApiResponse<User>("001", "Name already exists", null);
        }
    }

    @PostMapping("/find-user")
    public ApiResponse<User> findUser(@RequestBody Map<String, Object> reqBody) {
        User user = userRepository.findByNameAndHashPassword(
                (String) reqBody.get("name"),
                DigestUtils.sha256Hex((String) reqBody.get("password")));
        return user != null
                ? new ApiResponse<User>("000", "success", user)
                : new ApiResponse<User>("001", "User not found", user);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/get-all-books")
    public ApiResponse<List<Book>> getAllBooks(@RequestHeader String authorization) {
        String token = authorization.split(" ")[1];
        if (jwtTokenUtil.validateToken(token)) {
            return new ApiResponse("000", "success", bookRepository.findAll());
        } else {
            return new ApiResponse("003", "Token không hợp lệ", null);
        }
    }
}