package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Librarian;
import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.CheckoutStatus;
import be.kdg.librarysystem.domain.enums.Department;
import be.kdg.librarysystem.domain.enums.Genre;
import be.kdg.librarysystem.domain.enums.MembershipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookCheckoutJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookCheckoutJpaRepository bookCheckoutJpaRepository;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private LibrarianJpaRepository librarianJpaRepository;

    private Book book1;
    private Member member1;
    private Librarian librarian1;
    private BookCheckout checkout1;
    private BookCheckout checkout2;

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
        book1.setGenre(Genre.HISTORY);
        book1.setAvailable(true);
        book1.setLocation("Shelf A1");
        entityManager.persist(book1);

        member1 = new Member();
        member1.setFirstName("John");
        member1.setLastName("Doe");
        member1.setMembershipDate(LocalDate.of(2023, 1, 1));
        member1.setMembershipType(MembershipType.PREMIUM);
        member1.setEmail("john.doe@example.com");
        member1.setPhoneNumber("123456789");
        member1.setActiveMembership(true);
        entityManager.persist(member1);

        librarian1 = new Librarian();
        librarian1.setFirstName("Alice");
        librarian1.setLastName("Smith");
        librarian1.setHireDate(LocalDate.of(2020, 5, 10));
        librarian1.setSalary(50000.00);
        librarian1.setDepartment(Department.GENERAL);
        librarian1.setFullTime(true);
        librarian1.setEmail("alice.smith@example.com");
        librarian1.setPhoneNumber(123456789);
        entityManager.persist(librarian1);

        checkout1 = new BookCheckout();
        checkout1.setBook(book1);
        checkout1.setMember(member1);
        checkout1.setHandledByLibrarian(librarian1);
        checkout1.setStatus(CheckoutStatus.CHECKED_OUT);
        checkout1.setCheckoutDate(LocalDate.now());
        checkout1.setDueDate(LocalDate.now().plusDays(14));

        Book book2 = new Book();
        book2.setIsbn("978-0134685991");
        book2.setTitle("Clean Code");
        book2.setAuthor("Robert C. Martin");
        book2.setPublicationDate(LocalDate.of(2008, 8, 1));
        book2.setPages(464);
        book2.setPrice(35.00);
        book2.setGenre(Genre.HORROR);
        book2.setAvailable(true);
        book2.setLocation("Shelf A2");
        entityManager.persist(book2);

        checkout2 = new BookCheckout();
        checkout2.setBook(book2);
        checkout2.setMember(member1);
        checkout2.setHandledByLibrarian(librarian1);
        checkout2.setStatus(CheckoutStatus.RETURNED);
        checkout2.setCheckoutDate(LocalDate.now().minusMonths(1));
        checkout2.setDueDate(LocalDate.now().minusWeeks(2));
        checkout2.setReturnDate(LocalDate.now().minusWeeks(1));
    }

    @Test
    void findByBookId_shouldReturnCheckout_whenBookHasCheckout() {
        entityManager.persist(checkout1);
        entityManager.flush();

        Optional<BookCheckout> foundCheckout = bookCheckoutJpaRepository.findByBookId(book1.getId());
        assertTrue(foundCheckout.isPresent());
        assertEquals(checkout1.getId(), foundCheckout.get().getId());
        assertEquals(book1.getId(), foundCheckout.get().getBook().getId());
        assertNotNull(foundCheckout.get().getMember());
        assertNotNull(foundCheckout.get().getHandledByLibrarian());
    }

    @Test
    void findByBookId_shouldReturnEmpty_whenBookHasNoCheckout() {
        // book1 is persisted but checkout1 is not for this test
        Optional<BookCheckout> foundCheckout = bookCheckoutJpaRepository.findByBookId(book1.getId());
        assertFalse(foundCheckout.isPresent());
    }

    @Test
    void findByBookId_shouldReturnEmpty_whenBookDoesNotExist() {
        Optional<BookCheckout> foundCheckout = bookCheckoutJpaRepository.findByBookId(999);
        assertFalse(foundCheckout.isPresent());
    }

    @Test
    void findByHandledByLibrarianId_shouldReturnCheckouts_whenLibrarianHasCheckouts() {
        entityManager.persist(checkout1);
        entityManager.persist(checkout2);
        entityManager.flush();

        List<BookCheckout> checkouts = bookCheckoutJpaRepository.findByHandledByLibrarianId(librarian1.getId());
        assertNotNull(checkouts);
        assertEquals(2, checkouts.size());
        assertTrue(checkouts.stream().anyMatch(bc -> bc.getId() == checkout1.getId()));
        assertTrue(checkouts.stream().anyMatch(bc -> bc.getId() == checkout2.getId()));
    }

    @Test
    void findByHandledByLibrarianId_shouldReturnEmptyList_whenLibrarianHasNoCheckouts() {
        // librarian1 is persisted but no checkouts associated with them for this test
        List<BookCheckout> checkouts = bookCheckoutJpaRepository.findByHandledByLibrarianId(librarian1.getId());
        assertNotNull(checkouts);
        assertTrue(checkouts.isEmpty());
    }

    @Test
    void findByMemberId_shouldReturnCheckouts_whenMemberHasCheckouts() {
        entityManager.persist(checkout1);
        entityManager.persist(checkout2);
        entityManager.flush();

        List<BookCheckout> checkouts = bookCheckoutJpaRepository.findByMemberId(member1.getId());
        assertNotNull(checkouts);
        assertEquals(2, checkouts.size());
        assertTrue(checkouts.stream().anyMatch(bc -> bc.getId() == checkout1.getId()));
        assertTrue(checkouts.stream().anyMatch(bc -> bc.getId() == checkout2.getId()));
        assertNotNull(checkouts.get(0).getBook());
    }

    @Test
    void findByMemberId_shouldReturnEmptyList_whenMemberHasNoCheckouts() {
        // member1 is persisted but no checkouts associated with them for this test
        List<BookCheckout> checkouts = bookCheckoutJpaRepository.findByMemberId(member1.getId());
        assertNotNull(checkouts);
        assertTrue(checkouts.isEmpty());
    }

    @Test
    void delete_shouldRemoveCheckoutButNotAssociatedEntities() {
        entityManager.persist(checkout1);
        entityManager.flush();
        entityManager.clear();

        Book savedBook = bookJpaRepository.findById(book1.getId()).orElseThrow();
        Member savedMember = memberJpaRepository.findById(member1.getId()).orElseThrow();
        Librarian savedLibrarian = librarianJpaRepository.findById(librarian1.getId()).orElseThrow();
        BookCheckout savedCheckout = bookCheckoutJpaRepository.findById(checkout1.getId()).orElseThrow();

        assertNotNull(savedBook);
        assertNotNull(savedMember);
        assertNotNull(savedLibrarian);
        assertNotNull(savedCheckout);

        bookCheckoutJpaRepository.delete(savedCheckout);
        entityManager.flush();
        entityManager.clear();

        assertFalse(bookCheckoutJpaRepository.findById(savedCheckout.getId()).isPresent());

        assertTrue(bookJpaRepository.findById(savedBook.getId()).isPresent(), "Associated book should still exist");
        assertTrue(memberJpaRepository.findById(savedMember.getId()).isPresent(), "Associated member should still exist");
        assertTrue(librarianJpaRepository.findById(savedLibrarian.getId()).isPresent(), "Associated librarian should still exist");
    }
}
