package com.fladx.financeservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Transaction {
    private String category;
    private double amount;
    private boolean income; // true = доход, false = расход
    private LocalDateTime dateTime = LocalDateTime.now();

    public Transaction(String category, double amount, boolean income) {
        this.category = category;
        this.amount = amount;
        this.income = income;
    }
}
