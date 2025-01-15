package com.fladx.financeservice.service;

import com.fladx.financeservice.model.User;
import com.fladx.financeservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User register(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Уже есть такой пользователь");
        }
        User newUser = new User(login, password);
        return userRepository.save(newUser);
    }

    public User login(String login, String password) {
        User user = userRepository.findByLogin(login);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        throw new IllegalArgumentException("Неправильный логин или пароль");
    }
}