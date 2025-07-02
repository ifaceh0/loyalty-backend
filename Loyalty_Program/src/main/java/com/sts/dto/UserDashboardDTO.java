package com.sts.dto;


import java.time.LocalDateTime;

public class UserDashboardDTO {

    private LocalDateTime purchaseDate;
    private Integer transactionAmount;

    public UserDashboardDTO(LocalDateTime purchaseDate, Integer transactionAmount) {
        this.purchaseDate = purchaseDate;
        this.transactionAmount = transactionAmount;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Integer transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
