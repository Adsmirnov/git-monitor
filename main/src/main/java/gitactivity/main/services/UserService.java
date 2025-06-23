package gitactivity.main.services;


import gitactivity.main.model.User;
import gitactivity.main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {  // Метод для получения белого списка
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long id) {  // Метод для получения пользователя по айди
        return userRepository.findById(id);
    }
    public List<User> getUserByLogin(String login) {  // Метод для получения пользователя по логину
        return userRepository.findUserByLogin(login);
    }
}