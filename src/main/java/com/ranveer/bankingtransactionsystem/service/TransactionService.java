package com.ranveer.bankingtransactionsystem.service;

import com.ranveer.bankingtransactionsystem.dao.TransactionDAO;
import com.ranveer.bankingtransactionsystem.model.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionDAO transactionDAO;

    public List<Transaction> getTransactionsByUserId(int userId) {
        try {
            return transactionDAO.getTransactionsByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}