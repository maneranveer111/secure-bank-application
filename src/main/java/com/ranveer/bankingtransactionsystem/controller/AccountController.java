package com.ranveer.bankingtransactionsystem.controller;

import com.ranveer.bankingtransactionsystem.model.Account;
import com.ranveer.bankingtransactionsystem.service.AccountService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@CrossOrigin("*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // ✅ Create Account (called after registration)
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAccount(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int userId = Integer.parseInt(request.get("userId").toString());
            String accountType = request.getOrDefault("accountType", "SAVINGS").toString();
            
            boolean success = accountService.createAccount(userId, accountType);

            if (success) {
                response.put("status", "success");
                response.put("message", "Account created successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Failed to create account");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ✅ Get Account by userId
    @GetMapping("/{userId}")
    public ResponseEntity<Account> getAccount(@PathVariable int userId) {
        try {
            Account account = accountService.getAccount(userId);
            
            if (account != null) {
                return ResponseEntity.ok(account);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ Deposit
    @PostMapping("/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int userId = Integer.parseInt(request.get("userId").toString());
            double amount = Double.parseDouble(request.get("amount").toString());
            
            boolean success = accountService.deposit(userId, amount);

            if (success) {
                response.put("status", "success");
                response.put("message", "Deposit successful");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Deposit failed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ✅ Withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int userId = Integer.parseInt(request.get("userId").toString());
            double amount = Double.parseDouble(request.get("amount").toString());
            
            boolean success = accountService.withdraw(userId, amount);

            if (success) {
                response.put("status", "success");
                response.put("message", "Withdrawal successful");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Insufficient balance or invalid amount");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ✅ Transfer
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int fromUserId = Integer.parseInt(request.get("fromUserId").toString());
            int toUserId = Integer.parseInt(request.get("toUserId").toString());
            double amount = Double.parseDouble(request.get("amount").toString());
            
            boolean success = accountService.transfer(fromUserId, toUserId, amount);

            if (success) {
                response.put("status", "success");
                response.put("message", "Transfer successful");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Transfer failed. Check balance or recipient ID");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}