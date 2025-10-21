package be.kdg.programming3.repository;

import be.kdg.programming3.domain.User;
import be.kdg.programming3.repository.interfaces.DataRepository;
import be.kdg.programming3.repository.interfaces.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final DataRepository dataRepository;

    public InMemoryUserRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<User> findAll() {
        return dataRepository.findAllUsers();
    }

    @Override
    public void save(User user) {
        dataRepository.findAllUsers().add(user);
    }
}
