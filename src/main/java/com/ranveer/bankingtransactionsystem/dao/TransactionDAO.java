package com.ranveer.bankingtransactionsystem.dao;

import com.ranveer.bankingtransactionsystem.model.Transaction;
import com.ranveer.bankingtransactionsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class TransactionDAO {

    public boolean saveTransaction(Transaction tx) {
        String sql = "INSERT INTO transactions (account_id, type, amount) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tx.getAccountId());
            ps.setString(2, tx.getType());
            ps.setBigDecimal(3, tx.getAmount());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getTransactionsByUserId(int userId) {
        List<Transaction> list = new ArrayList<>();

        String sql = """
            SELECT t.transaction_id, t.account_id, t.type, t.amount, t.transaction_time
            FROM transactions t
            JOIN accounts a ON t.account_id = a.account_id
            WHERE a.user_id = ?
            ORDER BY t.transaction_time DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction tx = new Transaction();
                tx.setTransactionId(rs.getInt("transaction_id"));
                tx.setAccountId(rs.getInt("account_id"));
                tx.setType(rs.getString("type"));
                tx.setAmount(rs.getBigDecimal("amount"));
                tx.setTransactionTime(rs.getTimestamp("transaction_time").toLocalDateTime());

                list.add(tx);
            }

        } catch (SQLException e) {
            System.err.println("Error getting transactions: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
}