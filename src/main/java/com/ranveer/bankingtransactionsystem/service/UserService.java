package com.ranveer.bankingtransactionsystem.service;

import com.ranveer.bankingtransactionsystem.dao.AccountDAO;
import com.ranveer.bankingtransactionsystem.dao.UserDAO;
import com.ranveer.bankingtransactionsystem.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AccountDAO accountDAO;

    // REGISTER (with auto account creation)
    @Transactional
    public boolean register(String name, String email, String password) {
        try {
            if (name == null || email == null || password == null) {
                return false;
            }

            User user = new User();
            user.setName(name.trim());
            user.setEmail(email.trim().toLowerCase());
            user.setPassword(password);

            boolean userCreated = userDAO.registerUser(user);

            if (userCreated) {
                // Get the newly created user to get userId
                User createdUser = userDAO.loginUser(email.trim().toLowerCase(), password);
                
                if (createdUser != null) {
                    // Auto-create account for new user
                    accountDAO.createAccount(createdUser.getUserId(), "SAVINGS");
                }
            }

            return userCreated;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // LOGIN (EMAIL only)
    public User login(String email, String password) {
        try {
            if (email == null || password == null) {
                return null;
            }

            return userDAO.loginUser(email.trim().toLowerCase(), password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}