package be.kdg.programming3.repository;

import be.kdg.programming3.domain.User;
import be.kdg.programming3.domain.enums.Flags;
import be.kdg.programming3.repository.interfaces.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface UserJpaRepository extends JpaRepository<User, Integer>, UserRepository {

    @Override
    default User save(User user) {
        return saveAndFlush(user);
    }

    @Override
    default String getToken(int id) {
        return findById(id).map(User::getToken).orElse(null);
    }

    @Override
    default int updateToken(String token, int id) {
        return findById(id).map(u -> {
            u.setToken(token);
            saveAndFlush(u);
            return 1;
        }).orElse(0);
    }

    @Override
    default User getUserById(int id) {
        return findById(id).orElse(null);
    }

    @Override
    default User getUserByMemberId(int id) {
        return findAll().stream().filter(u -> u.getMember() != null && u.getMember().getId() == id).findFirst().orElse(null);
    }

    @Override
    default User getUserByCredentials(String username, String password) {
        return findAll().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    default User getUserByToken(String token) {
        if(token == null) return null;
        return findAll().stream()
                .filter(u -> token.equals(u.getToken()))
                .findFirst()
                .orElse(null);
    }

    @Override
    default User registerUser(int memberId, String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFlags(Flags.USER);
        saveAndFlush(user);
        return user;
    }

    @Override
    default boolean checkIfUsernameExists(String username) {
        return findAll().stream().anyMatch(u -> u.getUsername().equals(username));
    }

    @Override
    default int updateUsername(int id, String username) {
        return findById(id).map(u -> {
            u.setUsername(username);
            saveAndFlush(u);
            return 1;
        }).orElse(0);
    }

    @Override
    default int updatePassword(int id, String password){
        return findById(id).map(u -> {
            u.setPassword(password);
            saveAndFlush(u);
            return 1;
        }).orElse(0);
    }
}
