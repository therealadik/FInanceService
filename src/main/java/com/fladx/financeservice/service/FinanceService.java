package com.fladx.financeservice.service;

import com.fladx.financeservice.model.Transaction;
import com.fladx.financeservice.model.User;
import com.fladx.financeservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final UserRepository userRepository;

    public void addIncome(User user, String category, double amount) {
        Transaction transaction = new Transaction(category, amount, true);
        user.getWallet().updateBalance(amount);
        user.getWallet().getTransactions().add(transaction);
        userRepository.save(user);
        checkLimits(user, category);
    }

    public void addExpense(User user, String category, double amount) {
        Transaction transaction = new Transaction(category, amount, false);
        user.getWallet().updateBalance(-amount);
        user.getWallet().getTransactions().add(transaction);
        userRepository.save(user);
        checkLimits(user, category);
    }

    public void setBudget(User user, String category, double limit) {
        user.getWallet().getCategoryBudgets().put(category, limit);
        userRepository.save(user);
    }

    private void checkLimits(User user, String category) {
        // Если в категории задан лимит
        Double limit = user.getWallet().getCategoryBudgets().get(category);
        if (limit != null) {
            double spent = getSumByCategory(user, category, false);
            if (spent > limit) {
                System.out.println("Лимит по категории '" + category + "' превышен!");
            }
        }

        // Если общие расходы превысили доходы
        double totalIncome = getSumAllCategories(user, true);
        double totalExpense = getSumAllCategories(user, false);
        if (totalExpense > totalIncome) {
            System.out.println("Расходы превышают доходы!");
        }
    }

    public double getSumAllCategories(User user, boolean income) {
        return user.getWallet().getTransactions().stream()
                .filter(t -> t.isIncome() == income)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getSumByCategory(User user, String category, boolean income) {
        return user.getWallet().getTransactions().stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category) && t.isIncome() == income)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}
