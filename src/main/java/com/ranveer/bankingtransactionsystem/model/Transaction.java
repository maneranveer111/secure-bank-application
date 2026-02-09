package com.ranveer.bankingtransactionsystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    
    private int transactionId;
    private int accountId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime transactionTime;

    public Transaction() {
    }

    public Transaction(int accountId, BigDecimal amount, String type) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", transactionTime=" + transactionTime +
                '}';
    }
}
