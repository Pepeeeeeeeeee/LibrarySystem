package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Librarian;
import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.CheckoutStatus;
import be.kdg.librarysystem.domain.enums.Department;
import be.kdg.librarysystem.domain.enums.Genre;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.dto.BookCheckoutDetailsDTO;
import be.kdg.librarysystem.repository.BookCheckoutJpaRepository;
import be.kdg.librarysystem.repository.BookJpaRepository;
import be.kdg.librarysystem.repository.LibrarianJpaRepository;
import be.kdg.librarysystem.repository.MemberJpaRepository;
import be.kdg.librarysystem.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private LibrarianJpaRepository librarianJpaRepository;

    @Autowired
    private BookCheckoutJpaRepository bookCheckoutJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private Book book1;
    private Book book2;
    private Member member1;
    private Librarian librarian1;

    @BeforeEach
    void setUp() {
        bookCheckoutJpaRepository.deleteAll();
        bookJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        memberJpaRepository.deleteAll();
        librarianJpaRepository.deleteAll();

        book1 = new Book();
        book1.setIsbn("978-0321765723");
        book1.setTitle("Effective Java");
        book1.setAuthor("Joshua Bloch");
        book1.setPublicationDate(LocalDate.of(2018, 1, 1));
        book1.setPages(412);
        book1.setPrice(45.00);
        book1.setGenre(Genre.MYSTERY);
        book1.setAvailable(true);
        book1.setLocation("Shelf A1");

        book2 = new Book();
        book2.setIsbn("978-0134685991");
        book2.setTitle("Clean Code");
        book2.setAuthor("Robert C. Martin");
        book2.setPublicationDate(LocalDate.of(2008, 8, 1));
        book2.setPages(464);
        book2.setPrice(35.00);
        book2.setGenre(Genre.NON_FICTION);
        book2.setAvailable(true);
        book2.setLocation("Shelf A2");

        member1 = new Member();
        member1.setFirstName("John");
        member1.setLastName("Doe");
        member1.setMembershipDate(LocalDate.of(2023, 1, 1));
        member1.setMembershipType(MembershipType.PREMIUM);
        member1.setEmail("john.doe@example.com");
        member1.setPhoneNumber("123456789");
        member1.setActiveMembership(true);
        member1.setProfileImageUrl("http://example.com/john.jpg");

        librarian1 = new Librarian();
        librarian1.setFirstName("Alice");
        librarian1.setLastName("Smith");
        librarian1.setHireDate(LocalDate.of(2020, 5, 10));
        librarian1.setSalary(50000.00);
        librarian1.setDepartment(Department.RESEARCH);
        librarian1.setFullTime(true);
        librarian1.setEmail("alice.smith@example.com");
        librarian1.setPhoneNumber(123456789);
    }

    @Test
    void getCheckoutDetails_shouldReturnDetailsForActiveCheckout() {
        // Arrange
        Book savedBook = bookJpaRepository.save(book1);
        Member savedMember = memberJpaRepository.save(member1);
        Librarian savedLibrarian = librarianJpaRepository.save(librarian1);

        BookCheckout checkout = new BookCheckout();
        checkout.setBook(savedBook);
        checkout.setMember(savedMember);
        checkout.setHandledByLibrarian(savedLibrarian);
        checkout.setStatus(CheckoutStatus.CHECKED_OUT);
        checkout.setCheckoutDate(LocalDate.now());
        checkout.setDueDate(LocalDate.now().plusDays(14));
        bookCheckoutJpaRepository.save(checkout);

        Optional<BookCheckoutDetailsDTO> dtoOptional = bookService.getCheckoutDetails(savedBook.getId());

        assertTrue(dtoOptional.isPresent());
        BookCheckoutDetailsDTO dto = dtoOptional.get();
        assertTrue(dto.isCheckedOut());
        assertEquals(checkout.getDueDate().toString(), dto.getExpectedReturnDate());
        assertEquals(savedLibrarian.getId(), dto.getLibrarianId());
        assertEquals(savedLibrarian.getFirstName() + " " + savedLibrarian.getLastName(), dto.getLibrarianName());
        assertEquals(savedMember.getId(), dto.getMemberId());
        assertEquals(savedMember.getFirstName() + " " + savedMember.getLastName(), dto.getMemberName());
    }

    @Test
    void getCheckoutDetails_shouldReturnEmptyOptional_whenBookHasNoCheckout() {
        Book savedBook = bookJpaRepository.save(book1);

        Optional<BookCheckoutDetailsDTO> dtoOptional = bookService.getCheckoutDetails(savedBook.getId());

        assertFalse(dtoOptional.isPresent());
    }

    @Test
    void getCheckoutDetails_shouldReturnEmptyOptional_whenBookDoesNotExist() {
        Optional<BookCheckoutDetailsDTO> dtoOptional = bookService.getCheckoutDetails(999); // Non-existent book ID

        assertFalse(dtoOptional.isPresent());
    }

    @Test
    void getCheckoutDetails_shouldReturnDetailsForReturnedCheckout() {
        Book savedBook = bookJpaRepository.save(book1);
        Member savedMember = memberJpaRepository.save(member1);
        Librarian savedLibrarian = librarianJpaRepository.save(librarian1);

        BookCheckout checkout = new BookCheckout();
        checkout.setBook(savedBook);
        checkout.setMember(savedMember);
        checkout.setHandledByLibrarian(savedLibrarian);
        checkout.setStatus(CheckoutStatus.RETURNED); // Returned checkout
        checkout.setCheckoutDate(LocalDate.now().minusDays(20));
        checkout.setDueDate(LocalDate.now().minusDays(10));
        checkout.setReturnDate(LocalDate.now().minusDays(5)); // Actual return date
        bookCheckoutJpaRepository.save(checkout);

        Optional<BookCheckoutDetailsDTO> dtoOptional = bookService.getCheckoutDetails(savedBook.getId());

        assertTrue(dtoOptional.isPresent());
        BookCheckoutDetailsDTO dto = dtoOptional.get();
        assertFalse(dto.isCheckedOut()); // Should be false for a returned book
        assertEquals(checkout.getReturnDate().toString(), dto.getExpectedReturnDate()); // Should show actual return date
        assertEquals(savedLibrarian.getId(), dto.getLibrarianId());
        assertEquals(savedLibrarian.getFirstName() + " " + savedLibrarian.getLastName(), dto.getLibrarianName());
        assertEquals(savedMember.getId(), dto.getMemberId());
        assertEquals(savedMember.getFirstName() + " " + savedMember.getLastName(), dto.getMemberName());
    }
}