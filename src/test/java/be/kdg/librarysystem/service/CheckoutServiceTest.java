package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.CheckoutStatus;
import be.kdg.librarysystem.domain.enums.Genre;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.repository.BookCheckoutJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CheckoutServiceTest {

    @Mock
    private BookCheckoutJpaRepository checkoutRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    private BookCheckout checkout1;
    private BookCheckout checkout2;
    private Book book1;
    private Member member1;

    @BeforeEach
    void setUp() {
        book1 = new Book();
        book1.setId(1);
        book1.setTitle("Title 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("1234567890");
        book1.setPublicationDate(LocalDate.of(2000, 1, 1));
        book1.setPages(200);
        book1.setPrice(10.00);
        book1.setGenre(Genre.FICTION);
        book1.setAvailable(true);
        book1.setCoverImageUrl("http://example.com/cover1.jpg");
        book1.setLocation("Shelf A1");

        member1 = new Member();
        member1.setId(1);
        member1.setFirstName("John");
        member1.setLastName("Doe");
        member1.setMembershipDate(LocalDate.of(1990, 1, 1));
        member1.setMembershipType(MembershipType.STANDARD);
        member1.setEmail("john.doe@example.com");
        member1.setPhoneNumber("123456789");
        member1.setActiveMembership(true);
        member1.setProfileImageUrl("http://example.com/john.jpg");

        checkout1 = new BookCheckout();
        checkout1.setId(1);
        checkout1.setBook(book1);
        checkout1.setMember(member1);
        checkout1.setCheckoutDate(LocalDate.now());
        checkout1.setDueDate(LocalDate.now().plusDays(14));
        checkout1.setStatus(CheckoutStatus.CHECKED_OUT);
        checkout1.setRenewed(false);

        checkout2 = new BookCheckout();
        checkout2.setId(2);
        checkout2.setBook(book1);
        checkout2.setMember(member1);
        checkout2.setCheckoutDate(LocalDate.now().minusDays(30));
        checkout2.setDueDate(LocalDate.now().minusDays(16));
        checkout2.setStatus(CheckoutStatus.RETURNED);
        checkout2.setRenewed(false);
    }

    @Test
    void getAllBookCheckouts_shouldReturnAllCheckouts() {
        when(checkoutRepository.findAll()).thenReturn(Arrays.asList(checkout1, checkout2));

        List<BookCheckout> checkouts = checkoutService.getAllBookCheckouts();

        assertNotNull(checkouts);
        assertEquals(2, checkouts.size());
        assertTrue(checkouts.contains(checkout1));
        assertTrue(checkouts.contains(checkout2));
        verify(checkoutRepository, times(1)).findAll();
    }

    @Test
    void addCheckout_shouldReturnSavedCheckout() {
        when(checkoutRepository.save(any(BookCheckout.class))).thenReturn(checkout1);

        BookCheckout savedCheckout = checkoutService.addCheckout(checkout1);

        assertNotNull(savedCheckout);
        assertEquals(checkout1.getId(), savedCheckout.getId());
        verify(checkoutRepository, times(1)).save(checkout1);
    }
}
