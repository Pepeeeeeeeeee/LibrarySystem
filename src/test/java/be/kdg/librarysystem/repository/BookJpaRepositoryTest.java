package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Comic;
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
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private BookCheckoutJpaRepository bookCheckoutJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private LibrarianJpaRepository librarianJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Book book1;
    private Book book2;
    private Comic comic1;
    private Member member1;

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

        book1 = new Book();
        book1.setIsbn("978-0321765723");
        book1.setTitle("Effective Java");
        book1.setAuthor("Joshua Bloch");
        book1.setPublicationDate(LocalDate.of(2018, 1, 1));
        book1.setPages(412);
        book1.setPrice(45.00);
        book1.setGenre(Genre.FICTION);
        book1.setAvailable(true);
        book1.setLocation("Shelf A1");

        book2 = new Book();
        book2.setIsbn("978-0134685991");
        book2.setTitle("Clean Code");
        book2.setAuthor("Robert C. Martin");
        book2.setPublicationDate(LocalDate.of(2008, 8, 1));
        book2.setPages(464);
        book2.setPrice(35.00);
        book2.setGenre(Genre.FANTASY);
        book2.setAvailable(true);
        book2.setLocation("Shelf A2");

        comic1 = new Comic();
        comic1.setIsbn("978-1607060765");
        comic1.setTitle("Saga Vol. 1");
        comic1.setAuthor("Brian K. Vaughan");
        comic1.setArtist("Fiona Staples");
        comic1.setPublicationDate(LocalDate.of(2012, 10, 23));
        comic1.setPages(160);
        comic1.setPrice(9.99);
        comic1.setGenre(Genre.FANTASY);
        comic1.setAvailable(true);
        comic1.setLocation("Shelf C1");

        member1 = new Member();
        member1.setFirstName("Test");
        member1.setLastName("Member");
        member1.setEmail("test.member@example.com");
        member1.setMembershipType(MembershipType.STANDARD);
        member1.setMembershipDate(LocalDate.now());

    }

    @Test
    void findByGenre_shouldReturnBooksOfGivenGenre() {
        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(comic1);
        entityManager.flush();

        List<Book> fictionBooks =
                bookJpaRepository.findByGenre(Genre.FICTION);

        assertEquals(1, fictionBooks.size());
        assertEquals("Effective Java", fictionBooks.get(0).getTitle());

        List<Book> fantasyBooks =
                bookJpaRepository.findByGenre(Genre.FANTASY);

        assertEquals(2, fantasyBooks.size());

        assertTrue(
                fantasyBooks.stream()
                        .anyMatch(b -> b.getTitle().equals("Clean Code"))
        );

        assertTrue(
                fantasyBooks.stream()
                        .anyMatch(b -> b.getTitle().equals("Saga Vol. 1"))
        );
    }

    @Test
    void findByTitleContainingIgnoreCase_shouldReturnBooksWithMatchingTitle() {

        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(comic1);
        entityManager.flush();

        List<Book> javaBooks =
                bookJpaRepository.findByTitleContainingIgnoreCase("java");

        assertEquals(1, javaBooks.size());
        assertEquals("Effective Java", javaBooks.get(0).getTitle());

        List<Book> codeBooks =
                bookJpaRepository.findByTitleContainingIgnoreCase("CODE");

        assertEquals(1, codeBooks.size());
        assertEquals("Clean Code", codeBooks.get(0).getTitle());

        List<Book> sagaBooks =
                bookJpaRepository.findByTitleContainingIgnoreCase("Saga");

        assertEquals(1, sagaBooks.size());
        assertEquals("Saga Vol. 1", sagaBooks.get(0).getTitle());
    }

    @Test
    void findComicsByArtist_shouldReturnComicsByGivenArtist() {

        entityManager.persist(book1);
        entityManager.persist(comic1);
        entityManager.flush();

        List<Comic> comics =
                bookJpaRepository.findComicsByArtist("fiona staples");

        assertEquals(1, comics.size());
        assertEquals("Saga Vol. 1", comics.get(0).getTitle());

        List<Comic> none =
                bookJpaRepository.findComicsByArtist("nonexistent");

        assertTrue(none.isEmpty());
    }

    @Test
    void save_shouldThrowDataIntegrityViolationException_whenIsbnIsNotUnique() {

        entityManager.persistAndFlush(book1);

        Book duplicate = new Book();

        duplicate.setIsbn("978-0321765723");
        duplicate.setTitle("Another Book");
        duplicate.setAuthor("Another Author");
        duplicate.setPublicationDate(LocalDate.now());
        duplicate.setPages(100);
        duplicate.setPrice(10.00);
        duplicate.setGenre(Genre.FICTION);
        duplicate.setAvailable(true);
        duplicate.setLocation("Shelf Z1");

        assertThrows(DataIntegrityViolationException.class, () -> {
            bookJpaRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    void delete_shouldThrowDataIntegrityViolationException_whenAssociatedCheckoutsExist() {
        Book persistedBook = entityManager.persistAndFlush(book1);
        Member persistedMember = entityManager.persistAndFlush(member1);

        BookCheckout checkout = new BookCheckout();
        checkout.setBook(persistedBook);
        checkout.setMember(persistedMember);
        checkout.setStatus(CheckoutStatus.CHECKED_OUT);
        checkout.setCheckoutDate(LocalDate.now());
        checkout.setDueDate(LocalDate.now().plusDays(14));

        entityManager.persistAndFlush(checkout);

        entityManager.clear();

        Book savedBook = bookJpaRepository.findById(persistedBook.getId()).orElseThrow();

        assertThrows(DataIntegrityViolationException.class, () -> {
            bookJpaRepository.delete(savedBook);
            bookJpaRepository.flush();
        });
    }
}