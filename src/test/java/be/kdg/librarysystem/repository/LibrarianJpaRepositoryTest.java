package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.Librarian;
import be.kdg.librarysystem.domain.enums.Department;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LibrarianJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LibrarianJpaRepository librarianJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Librarian librarian1;
    private Librarian librarian2;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE librarians RESTART IDENTITY CASCADE");
    }

    @BeforeEach
    void setUp() {
        entityManager.clear();

        librarian1 = new Librarian();
        librarian1.setFirstName("Alice");
        librarian1.setLastName("Smith");
        librarian1.setHireDate(LocalDate.of(2020, 5, 10));
        librarian1.setSalary(50000.00);
        librarian1.setDepartment(Department.GENERAL);
        librarian1.setFullTime(true);
        librarian1.setEmail("alice.smith@example.com");
        librarian1.setPhoneNumber(123456789);

        librarian2 = new Librarian();
        librarian2.setFirstName("Bob");
        librarian2.setLastName("Johnson");
        librarian2.setHireDate(LocalDate.of(2021, 1, 15));
        librarian2.setSalary(55000.00);
        librarian2.setDepartment(Department.CHILDRENS_SECTION);
        librarian2.setFullTime(true);
        librarian2.setEmail("bob.johnson@example.com");
        librarian2.setPhoneNumber(987654321);
    }

    @Test
    void save_shouldPersistLibrarian() {
        Librarian savedLibrarian = librarianJpaRepository.save(librarian1);
        entityManager.flush();
        entityManager.clear();

        Optional<Librarian> foundLibrarian = librarianJpaRepository.findById(savedLibrarian.getId());
        assertTrue(foundLibrarian.isPresent());
        assertEquals(librarian1.getFirstName(), foundLibrarian.get().getFirstName());
        assertEquals(librarian1.getEmail(), foundLibrarian.get().getEmail());
    }

    @Test
    void findAll_shouldReturnAllLibrarians() {
        entityManager.persist(librarian1);
        entityManager.persist(librarian2);
        entityManager.flush();

        List<Librarian> librarians = librarianJpaRepository.findAll();
        assertNotNull(librarians);
        assertEquals(2, librarians.size());
        assertTrue(librarians.stream().anyMatch(l -> l.getEmail().equals("alice.smith@example.com")));
        assertTrue(librarians.stream().anyMatch(l -> l.getEmail().equals("bob.johnson@example.com")));
    }

    @Test
    void findById_shouldReturnLibrarian_whenExists() {
        Librarian savedLibrarian = entityManager.persist(librarian1);
        entityManager.flush();

        Optional<Librarian> foundLibrarian = librarianJpaRepository.findById(savedLibrarian.getId());
        assertTrue(foundLibrarian.isPresent());
        assertEquals(savedLibrarian.getEmail(), foundLibrarian.get().getEmail());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        Optional<Librarian> foundLibrarian = librarianJpaRepository.findById(999);
        assertFalse(foundLibrarian.isPresent());
    }

    @Test
    void save_shouldThrowDataIntegrityViolationException_whenEmailIsNotUnique() {
        entityManager.persist(librarian1);
        entityManager.flush();

        Librarian duplicateEmailLibrarian = new Librarian();
        duplicateEmailLibrarian.setFirstName("Charlie");
        duplicateEmailLibrarian.setLastName("Brown");
        duplicateEmailLibrarian.setHireDate(LocalDate.now());
        duplicateEmailLibrarian.setSalary(40000.00);
        duplicateEmailLibrarian.setDepartment(Department.ARCHIVE);
        duplicateEmailLibrarian.setFullTime(false);
        duplicateEmailLibrarian.setEmail("alice.smith@example.com");
        duplicateEmailLibrarian.setPhoneNumber(111222333);

        assertThrows(DataIntegrityViolationException.class, () -> {
            librarianJpaRepository.save(duplicateEmailLibrarian);
            entityManager.flush();
        });
    }

    @Test
    void delete_shouldRemoveLibrarian() {
        Librarian savedLibrarian = entityManager.persist(librarian1);
        entityManager.flush();

        librarianJpaRepository.deleteById(savedLibrarian.getId());
        entityManager.flush();

        Optional<Librarian> foundLibrarian = librarianJpaRepository.findById(savedLibrarian.getId());
        assertFalse(foundLibrarian.isPresent());
    }
}