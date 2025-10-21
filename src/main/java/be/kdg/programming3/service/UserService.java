package be.kdg.programming3.service;

import be.kdg.programming3.domain.User;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final DataRepository repo;

    public UserService(DataRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAllUsers();
    }

    public void addUser(User user) {
        repo.findAllUsers().add(user);
    }
}
