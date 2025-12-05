package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.User;
import java.util.List;

public interface UserRepository {
    List<User> findAll();
    void save(User user);
    String getToken(int id);
    int updateToken(String token, int id);
    User getUserById(int id);
    User getUserByMemberId(int id);
    User getUserByCredentials(String username, String password);
    User getUserByToken(String token);
    User registerUser(int id, String username, String password);
    boolean checkIfUsernameExists(String username);
    int updateUsername(int id, String username);
}
