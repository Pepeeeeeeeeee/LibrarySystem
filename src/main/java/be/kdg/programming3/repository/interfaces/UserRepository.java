package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    void save(User user);
}
