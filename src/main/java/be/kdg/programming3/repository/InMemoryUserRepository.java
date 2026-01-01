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
    public User save(User user) {
        dataRepository.findAllUsers().add(user);
        return user;
    }

    @Override
    public String getToken(int id) {
        return dataRepository.findAllUsers().stream().filter(user -> user.getId() == id).findFirst().orElse(null).getToken();
    }

    @Override
    public int updateToken(String token, int id) {
        User user = dataRepository.findAllUsers().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        if(user == null) return 1;
        user.setToken(token);
        return 1;
    }

    @Override
    public User getUserById(int id) {
        return dataRepository.findAllUsers().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    @Override
    public User getUserByMemberId(int id) {
        return dataRepository.findAllUsers().stream().filter(u -> u.getMember().getId() == id).findFirst().orElse(null);
    }

    @Override
    public User getUserByCredentials(String username, String password) {
        return dataRepository.findAllUsers().stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst().orElse(null);
    }

    @Override
    public User getUserByToken(String token) {
        return dataRepository.findAllUsers().stream().filter(u -> u.getToken().equals(token)).findFirst().orElse(null);
    }

    @Override
    public User registerUser(int id, String username, String password) {
        return null;
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        return dataRepository.findAllUsers().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null) != null;
    }

    @Override
    public int updateUsername(int id, String username) {
        User oldUserData = dataRepository.findAllUsers().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        if(oldUserData == null) return 0;
        boolean updated = dataRepository.findAllUsers().removeIf(u -> u.getId() == id);
        oldUserData.setUsername(username);
        save(oldUserData);
        return updated ? 1 : 0;
    }

    @Override
    public int updatePassword(int id, String password) {
        User oldUserData = dataRepository.findAllUsers().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        if(oldUserData == null) return 0;
        boolean updated = dataRepository.findAllUsers().removeIf(u -> u.getId() == id);
        oldUserData.setPassword(password);
        save(oldUserData);
        return updated ? 1 : 0;
    }
}
