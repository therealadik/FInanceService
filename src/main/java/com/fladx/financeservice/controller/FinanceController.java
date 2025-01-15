package com.fladx.financeservice.controller;

import com.fladx.financeservice.model.User;
import com.fladx.financeservice.repository.UserRepository;
import com.fladx.financeservice.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;
    private final UserRepository userRepository;

    @PostMapping("/income")
    public ResponseEntity<String> addIncome(
            @RequestParam String login,
            @RequestParam String category,
            @RequestParam double amount
    ) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
        financeService.addIncome(user, category, amount);
        return ResponseEntity.ok("Доход добавлен");
    }

    @PostMapping("/expense")
    public ResponseEntity<String> addExpense(
            @RequestParam String login,
            @RequestParam String category,
            @RequestParam double amount
    ) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
        financeService.addExpense(user, category, amount);
        return ResponseEntity.ok("Расход добавлен");
    }

    @PostMapping("/budget")
    public ResponseEntity<String> setBudget(
            @RequestParam String login,
            @RequestParam String category,
            @RequestParam double limit
    ) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
        financeService.setBudget(user, category, limit);
        return ResponseEntity.ok("Бюджет установлен");
    }

    @GetMapping("/report")
    public ResponseEntity<String> getReport(@RequestParam String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }
        StringBuilder sb = new StringBuilder();
        double totalIncome = financeService.getSumAllCategories(user, true);
        double totalExpense = financeService.getSumAllCategories(user, false);

        sb.append("Общий доход: ").append(totalIncome).append("\n")
                .append("Общий расход: ").append(totalExpense).append("\n\n")
                .append("Бюджет по категориям:\n");

        user.getWallet().getCategoryBudgets().forEach((cat, lim) -> {
            double spent = financeService.getSumByCategory(user, cat, false);
            double remaining = lim - spent;
            sb.append(cat).append(": Лимит=").append(lim)
                    .append(", Остаток=").append(remaining).append("\n");
        });

        return ResponseEntity.ok(sb.toString());
    }
}