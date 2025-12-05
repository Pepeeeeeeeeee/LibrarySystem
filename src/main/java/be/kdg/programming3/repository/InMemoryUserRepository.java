package be.kdg.programming3.repository;

import be.kdg.programming3.domain.User;
import be.kdg.programming3.repository.interfaces.DataRepository;
import be.kdg.programming3.repository.interfaces.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Profile({"web", "console"})
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

    @Override
    public String getToken(int id) {
        return dataRepository.findAllUsers().stream().filter(user -> user.getId() == id).findFirst().get().getToken();
    }

    @Override
    public int updateToken(String token, int id) {
        User user = dataRepository.findAllUsers().stream().filter(u -> u.getId() == id).findFirst().get();
        user.setToken(token);
        return 1;
    }

    @Override
    public User getUserById(int id) {
        return dataRepository.findAllUsers().stream().filter(u -> u.getId() == id).findFirst().get();
    }

    @Override
    public User getUserByMemberId(int id) {
        return null;
    }

    @Override
    public User getUserByCredentials(String username, String password) {
        return dataRepository.findAllUsers().stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst().get();
    }

    @Override
    public User getUserByToken(String token) {
        return dataRepository.findAllUsers().stream().filter(u -> u.getToken().equals(token)).findFirst().get();
    }

    @Override
    public User registerUser(int id, String username, String password) {
        return null;
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        return false;
    }

    @Override
    public int updateUsername(int id, String username) {
        return 0;
    }
}
