package com.fladx.financeservice.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Wallet {
    private double balance = 0;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<Transaction> transactions = new ArrayList<>();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Map<String, Double> categoryBudgets = new HashMap<>();

    public void updateBalance(double amount) {
        balance += amount;
    }
}
