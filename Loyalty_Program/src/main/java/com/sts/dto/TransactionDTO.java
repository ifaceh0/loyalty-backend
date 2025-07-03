package com.sts.dto;

import lombok.Data;

@Data
public class TransactionDTO {
    private String date;
    private int transactionAmount;
    private int pointsReceived;

    public TransactionDTO(String date, int transactionAmount, int pointsReceived) {
        this.date = date;
        this.transactionAmount = transactionAmount;
        this.pointsReceived = pointsReceived;
    }
}
