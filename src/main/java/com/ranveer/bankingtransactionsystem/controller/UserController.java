package com.ranveer.bankingtransactionsystem.controller;

import com.ranveer.bankingtransactionsystem.model.User;
import com.ranveer.bankingtransactionsystem.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Register
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = userService.register(
                    user.getName(),
                    user.getEmail(),
                    user.getPassword());

            if (success) {
                response.put("status", "success");
                response.put("message", "Registration successful");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Email already exists");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ✅ Login (EMAIL only)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User loggedInUser = userService.login(user.getEmail(), user.getPassword());
            
            if (loggedInUser != null) {
                response.put("status", "success");
                response.put("userId", loggedInUser.getUserId());
                response.put("name", loggedInUser.getName());
                response.put("email", loggedInUser.getEmail());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Invalid email or password");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ✅ Verify Password (for balance view & transactions)
    @PostMapping("/verify-password")
    public ResponseEntity<Map<String, Object>> verifyPassword(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            User user = userService.login(email, password);
            
            if (user != null) {
                response.put("status", "success");
                response.put("verified", true);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("verified", false);
                response.put("message", "Invalid password");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("verified", false);
            response.put("message", "Verification failed");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}