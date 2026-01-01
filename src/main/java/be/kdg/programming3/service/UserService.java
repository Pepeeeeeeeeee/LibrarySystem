package be.kdg.programming3.service;

import be.kdg.programming3.domain.User;
import be.kdg.programming3.repository.interfaces.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public void addUser(User user) {
        repo.save(user);
    }

    public String getToken(int id){
        return repo.getToken(id);
    }

    public int updateToken(int id, String token){
        return repo.updateToken(token, id);
    }

    public User getUserById(int id){
        return repo.getUserById(id);
    }

    public User getUserByMemberId(int id){
        return repo.getUserByMemberId(id);
    }

    public User authUser(String username, String password){
        return repo.getUserByCredentials(username, password);
    }

    public User registerUser(int id, String username, String password){
        return repo.registerUser(id, username, password);
    }

    public boolean checkIfUsernameExists(String username){
        return repo.checkIfUsernameExists(username);
    }

    public User getUserByToken(String token){
        return repo.getUserByToken(token);
    }

    public int updateUsername(int id, String username){
        return repo.updateUsername(id, username);
    }

    public void updatePassword(int id, String password){
        repo.updatePassword(id, password);
    }

}
