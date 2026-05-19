package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.User;
import be.kdg.librarysystem.domain.enums.Roles;
import be.kdg.librarysystem.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserJpaRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User createUser(
            Member member,
            String username,
            String rawPassword
    ) {

        User user = new User();

        user.setMember(member);
        user.setUsername(username);

        user.setPassword(
                passwordEncoder.encode(rawPassword)
        );

        user.setRole(Roles.USER);

        return userRepository.save(user);
    }

    public User findById(int id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public User findByMemberId(int memberId) {
        return userRepository.findByMemberId(memberId)
                .orElse(null);
    }

    public User updateUsername(int id, String username) {

        User user = findById(id);

        if (user == null) {
            return null;
        }

        user.setUsername(username);

        return userRepository.save(user);
    }

    public boolean changePassword(
            User user,
            String oldPassword,
            String newPassword
    ) {

        if (!passwordEncoder.matches(
                oldPassword,
                user.getPassword()
        )) {
            return false;
        }

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        return true;
    }
}