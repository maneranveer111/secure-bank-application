package com.ranveer.bankingtransactionsystem.service;

import com.ranveer.bankingtransactionsystem.dao.AccountDAO;
import com.ranveer.bankingtransactionsystem.dao.TransactionDAO;
import com.ranveer.bankingtransactionsystem.model.Account;
import com.ranveer.bankingtransactionsystem.model.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    // ================= VALIDATION =================
    private boolean isInvalidAmount(double amount) {
        return amount <= 0;
    }

    // ================= GET =================
    public Account getAccount(int userId) {
        return accountDAO.getAccountByUserId(userId);
    }

    // ================= CREATE =================
    public boolean createAccount(int userId, String accountType) {
        try {
            Account existing = accountDAO.getAccountByUserId(userId);

            if (existing != null) {
                System.out.println("Account already exists for userId: " + userId);
                return false;
            }

            return accountDAO.createAccount(userId, accountType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= DEPOSIT =================
    @Transactional
    public boolean deposit(int userId, double amount) {
        try {
            if (isInvalidAmount(amount)) {
                return false;
            }

            Account account = getAccount(userId);
            if (account == null) {
                return false;
            }

            BigDecimal amountBD = BigDecimal.valueOf(amount);
            BigDecimal newBalance = account.getBalance().add(amountBD);

            boolean updated = accountDAO.updateBalance(account.getAccountId(), newBalance);

            if (updated) {
                transactionDAO.saveTransaction(
                        new Transaction(account.getAccountId(), amountBD, "DEPOSIT"));
            }

            return updated;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= WITHDRAW =================
    @Transactional
    public boolean withdraw(int userId, double amount) {
        try {
            if (isInvalidAmount(amount)) {
                return false;
            }

            Account account = getAccount(userId);
            if (account == null) {
                return false;
            }

            BigDecimal amountBD = BigDecimal.valueOf(amount);

            if (account.getBalance().compareTo(amountBD) < 0) {
                return false;
            }

            BigDecimal newBalance = account.getBalance().subtract(amountBD);

            boolean updated = accountDAO.updateBalance(account.getAccountId(), newBalance);

            if (updated) {
                transactionDAO.saveTransaction(
                        new Transaction(account.getAccountId(), amountBD, "WITHDRAW"));
            }

            return updated;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= TRANSFER =================
    @Transactional
    public boolean transfer(int fromUserId, int toUserId, double amount) {
        try {
            if (isInvalidAmount(amount)) {
                return false;
            }

            Account from = getAccount(fromUserId);
            Account to = getAccount(toUserId);

            if (from == null || to == null) {
                return false;
            }

            BigDecimal amountBD = BigDecimal.valueOf(amount);

            if (from.getBalance().compareTo(amountBD) < 0) {
                return false;
            }

            BigDecimal fromNew = from.getBalance().subtract(amountBD);
            BigDecimal toNew = to.getBalance().add(amountBD);

            accountDAO.updateBalance(from.getAccountId(), fromNew);
            accountDAO.updateBalance(to.getAccountId(), toNew);

            transactionDAO.saveTransaction(
                    new Transaction(from.getAccountId(), amountBD, "TRANSFER_OUT"));

            transactionDAO.saveTransaction(
                    new Transaction(to.getAccountId(), amountBD, "TRANSFER_IN"));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}