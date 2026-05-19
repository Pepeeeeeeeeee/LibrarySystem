package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.User;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.domain.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Member member1;
    private User user1;
    private User user2;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE book_checkouts RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE books RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE application_users RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE members RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE librarians RESTART IDENTITY CASCADE");
    }

    @BeforeEach
    void setUp() {
        entityManager.clear();

        member1 = new Member();
        member1.setFirstName("John");
        member1.setLastName("Doe");
        member1.setMembershipDate(LocalDate.of(2023, 1, 1));
        member1.setMembershipType(MembershipType.PREMIUM);
        member1.setEmail("john.doe@example.com");
        member1.setPhoneNumber("123456789");
        member1.setActiveMembership(true);
        member1.setProfileImageUrl("http://example.com/john.jpg");
        Member persistedMember = entityManager.persistAndFlush(member1);

        user1 = new User();
        user1.setUsername("johndoe");
        user1.setPassword("password123");
        user1.setRole(Roles.USER);
        user1.setMember(persistedMember);
        persistedMember.setUser(user1);

        user2 = new User();
        user2.setUsername("janedoe");
        user2.setPassword("securepass");
        user2.setRole(Roles.USER);
    }

    @Test
    void findByUsername_shouldReturnUser_whenUsernameExists() {
        entityManager.persist(user1);
        entityManager.flush();

        Optional<User> foundUser = userJpaRepository.findByUsername("johndoe");

        assertTrue(foundUser.isPresent());
        assertEquals(user1.getUsername(), foundUser.get().getUsername());
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {
        entityManager.persist(user1);
        entityManager.flush();

        Optional<User> foundUser = userJpaRepository.findByUsername("nonexistent");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameExists() {
        entityManager.persist(user1);
        entityManager.flush();

        assertTrue(userJpaRepository.existsByUsername("johndoe"));
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenUsernameDoesNotExist() {
        entityManager.persist(user1);
        entityManager.flush();

        assertFalse(userJpaRepository.existsByUsername("nonexistent"));
    }

    @Test
    void findByMemberId_shouldReturnUser_whenMemberIdExists() {
        entityManager.persist(user1);
        entityManager.flush();

        Optional<User> foundUser = userJpaRepository.findByMemberId(member1.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(user1.getUsername(), foundUser.get().getUsername());
        assertEquals(member1.getId(), foundUser.get().getMember().getId());
    }

    @Test
    void findByMemberId_shouldReturnEmpty_whenMemberIdDoesNotExist() {
        entityManager.persist(user1);
        entityManager.flush();

        Optional<User> foundUser = userJpaRepository.findByMemberId(999);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void save_shouldThrowDataIntegrityViolationException_whenUsernameIsNotUnique() {
        entityManager.persist(user1);
        entityManager.flush();

        User duplicateUsernameUser = new User();
        duplicateUsernameUser.setUsername("johndoe");
        duplicateUsernameUser.setPassword("anotherpass");
        duplicateUsernameUser.setRole(Roles.USER);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userJpaRepository.save(duplicateUsernameUser);
            entityManager.flush();
        });
    }

    @Test
    void delete_shouldNotDeleteAssociatedMember() {
        // Arrange
        User persistedUser = entityManager.persistAndFlush(user1);
        entityManager.clear();

        // Verify user and member exist
        User savedUser = userJpaRepository.findById(persistedUser.getId()).orElseThrow();
        Member associatedMember = memberJpaRepository.findById(member1.getId()).orElseThrow();
        assertNotNull(savedUser);
        assertNotNull(associatedMember);
        assertEquals(associatedMember.getId(), savedUser.getMember().getId());

        userJpaRepository.delete(savedUser);
        // Explicitly break the bidirectional link in the managed Member entity
        associatedMember.setUser(null);
        entityManager.merge(associatedMember);
        entityManager.flush();
        entityManager.clear();

        // Assert: User should be deleted, but member should still exist
        assertFalse(userJpaRepository.findById(savedUser.getId()).isPresent());
        assertTrue(memberJpaRepository.findById(associatedMember.getId()).isPresent(), "Associated member should still exist");
    }
}