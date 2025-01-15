package com.fladx.financeservice.repository;

import com.fladx.financeservice.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public User findByLogin(String login) {
        return users.get(login);
    }

    public User save(User user) {
        return users.put(user.getLogin(), user);
    }

    public boolean existsByLogin(String login) {
        return users.containsKey(login);
    }

    public Map<String, User> findAll() {
        return new HashMap<>(users);
    }
}
