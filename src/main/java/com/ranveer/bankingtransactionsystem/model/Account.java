package com.ranveer.bankingtransactionsystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

    private int accountId;
    private int userId;
    private BigDecimal balance;
    private String accountType;
    private LocalDateTime createdAt;

    public Account() {}

    public Account(int accountId, int userId, BigDecimal balance,
                   String accountType, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = createdAt;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}