package com.ranveer.bankingtransactionsystem.dao;

import com.ranveer.bankingtransactionsystem.model.User;
import com.ranveer.bankingtransactionsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    // REGISTER
    public boolean registerUser(User user) {
        String check = "SELECT user_id FROM users WHERE email = ?";
        String insert = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            // Check duplicate email
            try (PreparedStatement ps = conn.prepareStatement(check)) {
                ps.setString(1, user.getEmail());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    System.out.println("Email already exists: " + user.getEmail());
                    return false;
                }
            }

            // Insert new user
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());

                int rowsAffected = ps.executeUpdate();
                System.out.println("User registered. Rows affected: " + rowsAffected);
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error in registerUser: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // LOGIN
    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setCreatedAt(rs.getTimestamp("created_at"));

                System.out.println("Login successful for: " + email);
                return user;
            } else {
                System.out.println("Login failed for: " + email);
            }

        } catch (SQLException e) {
            System.err.println("Error in loginUser: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}