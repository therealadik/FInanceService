package com.fladx.financeservice.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fladx.financeservice.model.User;
import com.fladx.financeservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class DataStorage {


    private static final String FILE_NAME = "users.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;

    @PostConstruct
    public void loadData(){
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try{
            var type = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, User.class);
            HashMap<String, User> usersFromFile = objectMapper.readValue(file, type);
            usersFromFile.forEach((k, v) -> userRepository.save(v));
        } catch (IOException e) {
            System.out.println("Не удалось загрузить данные из файла.");
        }
    }

    @PreDestroy
    public void saveData(){
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_NAME), userRepository.findAll());
        } catch (IOException e) {
            System.out.println("Не удалось сохранить данные в файл.");
        }
    }
}
