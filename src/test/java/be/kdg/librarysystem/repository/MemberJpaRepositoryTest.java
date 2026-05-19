package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.CheckoutStatus;
import be.kdg.librarysystem.domain.enums.Genre;
import be.kdg.librarysystem.domain.enums.MembershipType;
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
class MemberJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private BookCheckoutJpaRepository bookCheckoutJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Member member1;
    private Member member2;
    private Book book1;
    private BookCheckout checkout1;

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

        member2 = new Member();
        member2.setFirstName("Jane");
        member2.setLastName("Smith");
        member2.setMembershipDate(LocalDate.of(2023, 2, 1));
        member2.setMembershipType(MembershipType.STANDARD);
        member2.setEmail("jane.smith@example.com");
        member2.setPhoneNumber("987654321");
        member2.setActiveMembership(false);
        member2.setProfileImageUrl("http://example.com/jane.jpg");

        book1 = new Book();
        book1.setTitle("Test Book");
        book1.setAuthor("Test Author");
        book1.setIsbn("1234567890");
        book1.setPublicationDate(LocalDate.of(2020, 1, 1));
        book1.setPages(300);
        book1.setPrice(20.00);
        book1.setGenre(Genre.FICTION);
        book1.setAvailable(true);
        book1.setCoverImageUrl("http://example.com/book.jpg");
        book1.setLocation("Shelf C3");
        book1 = entityManager.persistAndFlush(book1);

        checkout1 = new BookCheckout();
        checkout1.setBook(book1);
        checkout1.setStatus(CheckoutStatus.CHECKED_OUT);
        checkout1.setCheckoutDate(LocalDate.now());
        checkout1.setDueDate(LocalDate.now().plusDays(14));
    }

    @Test
    void findByEmailIgnoreCase_shouldReturnMember_whenEmailExists() {
        entityManager.persist(member1);
        entityManager.flush();

        Optional<Member> foundMember = memberJpaRepository.findByEmailIgnoreCase("john.doe@example.com");

        assertTrue(foundMember.isPresent());
        assertEquals(member1.getEmail(), foundMember.get().getEmail());

        Optional<Member> foundMemberCaseInsensitive = memberJpaRepository.findByEmailIgnoreCase("JOHN.DOE@example.com");
        assertTrue(foundMemberCaseInsensitive.isPresent());
        assertEquals(member1.getEmail(), foundMemberCaseInsensitive.get().getEmail());
    }

    @Test
    void findByEmailIgnoreCase_shouldReturnEmpty_whenEmailDoesNotExist() {
        entityManager.persist(member1);
        entityManager.flush();

        Optional<Member> foundMember = memberJpaRepository.findByEmailIgnoreCase("nonexistent@example.com");

        assertFalse(foundMember.isPresent());
    }

    @Test
    void save_shouldThrowDataIntegrityViolationException_whenEmailIsNotUnique() {
        entityManager.persist(member1);
        entityManager.flush();

        Member duplicateEmailMember = new Member();
        duplicateEmailMember.setFirstName("Another");
        duplicateEmailMember.setLastName("User");
        duplicateEmailMember.setEmail("john.doe@example.com");
        duplicateEmailMember.setMembershipDate(LocalDate.now());
        duplicateEmailMember.setMembershipType(MembershipType.STANDARD);
        duplicateEmailMember.setPhoneNumber("111222333");
        duplicateEmailMember.setActiveMembership(true);

        // Expect an exception when trying to save a member with a duplicate email
        assertThrows(DataIntegrityViolationException.class, () -> {
            memberJpaRepository.save(duplicateEmailMember);
            entityManager.flush();
        });
    }

//    @Test
//    void delete_shouldNotDeleteAssociatedCheckouts_dueToNoCascadeRemove() {
//        Member persistedMember = entityManager.persistAndFlush(member1);
//        Book persistedBook = entityManager.find(Book.class, book1.getId());
//
//        BookCheckout checkout = new BookCheckout();
//        checkout.setBook(persistedBook);
//        checkout.setMember(persistedMember);
//        checkout.setStatus(CheckoutStatus.CHECKED_OUT);
//        checkout.setCheckoutDate(LocalDate.now());
//        checkout.setDueDate(LocalDate.now().plusDays(14));
//
//        entityManager.persistAndFlush(checkout);
//        entityManager.clear();
//
//        // Verify member and checkout exist
//        Member savedMember = memberJpaRepository.findById(persistedMember.getId()).orElseThrow();
//        BookCheckout savedCheckout = bookCheckoutJpaRepository.findById(checkout.getId()).orElseThrow();
//        assertNotNull(savedMember);
//        assertNotNull(savedCheckout);
//        assertEquals(savedMember.getId(), savedCheckout.getMember().getId());
//
//        assertThrows(DataIntegrityViolationException.class, () -> {
//            memberJpaRepository.delete(savedMember);
//            entityManager.flush();
//        });
//
//        // Verify member and checkout still exist after failed delete attempt
//        assertTrue(memberJpaRepository.findById(savedMember.getId()).isPresent());
//        assertTrue(bookCheckoutJpaRepository.findById(savedCheckout.getId()).isPresent());
//    }
}