package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.User;
import be.kdg.librarysystem.repository.interfaces.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer>, UserRepository {

    @Override
    Optional<User> findByUsername(String username);

    @Override
    boolean existsByUsername(String username);

    @Override
    Optional<User> findByMemberId(int memberId);
}
