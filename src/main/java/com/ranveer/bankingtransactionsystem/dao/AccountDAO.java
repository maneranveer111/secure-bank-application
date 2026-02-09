package com.ranveer.bankingtransactionsystem.dao;

import com.ranveer.bankingtransactionsystem.model.Account;
import com.ranveer.bankingtransactionsystem.util.DBConnection;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AccountDAO {

    public boolean createAccount(int userId, String accountType) {
        String sql = "INSERT INTO accounts (user_id, account_type, balance) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, accountType);
            ps.setBigDecimal(3, BigDecimal.ZERO);

            int rowsAffected = ps.executeUpdate();
            System.out.println("Account created for userId: " + userId + ", rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setUserId(rs.getInt("user_id"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setAccountType(rs.getString("account_type"));
                account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                return account;
            }

        } catch (SQLException e) {
            System.err.println("Error getting account: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateBalance(int accountId, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, newBalance);
            ps.setInt(2, accountId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating balance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}