package be.kdg.librarysystem.repository.interfaces;

import be.kdg.librarysystem.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByMemberId(int memberId);
}
