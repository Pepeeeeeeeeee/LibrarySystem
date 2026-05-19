package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Librarian;
import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.CheckoutStatus;
import be.kdg.librarysystem.domain.enums.Department;
import be.kdg.librarysystem.domain.enums.Genre;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.dto.LibrarianCheckoutDTO;
import be.kdg.librarysystem.mapper.LibrarianMapper;
import be.kdg.librarysystem.repository.BookCheckoutJpaRepository;
import be.kdg.librarysystem.repository.LibrarianJpaRepository;
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
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LibrarianServiceTest {

    @Mock
    private LibrarianJpaRepository librarianRepository;

    @Mock
    private BookCheckoutJpaRepository checkoutRepository;

    @InjectMocks
    private LibrarianService librarianService;

    private Librarian librarian1;
    private Librarian librarian2;
    private BookCheckout checkout1;
    private BookCheckout checkout2;
    private Book book1;
    private Member member1;

    @BeforeEach
    void setUp() {
        librarian1 = new Librarian();
        librarian1.setId(1);
        librarian1.setFirstName("Alice");
        librarian1.setLastName("Smith");
        librarian1.setEmail("alice.smith@example.com");
        librarian1.setHireDate(LocalDate.of(2020, 1, 1));
        librarian1.setSalary(50000.00);
        librarian1.setDepartment(Department.CHILDRENS_SECTION);
        librarian1.setFullTime(true);
        librarian1.setEmployeePhotoUrl("http://example.com/alice.jpg");
        librarian1.setPhoneNumber(123456789);

        librarian2 = new Librarian();
        librarian2.setId(2);
        librarian2.setFirstName("Bob");
        librarian2.setLastName("Johnson");
        librarian2.setEmail("bob.johnson@example.com");
        librarian2.setHireDate(LocalDate.of(2021, 3, 15));
        librarian2.setSalary(55000.00);
        librarian2.setDepartment(Department.GENERAL);
        librarian2.setFullTime(true);
        librarian2.setEmployeePhotoUrl("http://example.com/bob.jpg");
        librarian2.setPhoneNumber(987654321);

        book1 = new Book();
        book1.setId(101);
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

        member1 = new Member();
        member1.setId(1);
        member1.setFirstName("John");
        member1.setLastName("Doe");
        member1.setMembershipDate(LocalDate.of(1990, 1, 1));
        member1.setMembershipType(MembershipType.PREMIUM);
        member1.setEmail("john.doe@example.com");
        member1.setPhoneNumber("123456789");
        member1.setActiveMembership(true);
        member1.setProfileImageUrl("http://example.com/john.jpg");

        checkout1 = new BookCheckout();
        checkout1.setId(1);
        checkout1.setBook(book1);
        checkout1.setMember(member1);
        checkout1.setHandledByLibrarian(librarian1);
        checkout1.setCheckoutDate(LocalDate.now());
        checkout1.setDueDate(LocalDate.now().plusDays(14));
        checkout1.setStatus(CheckoutStatus.CHECKED_OUT);

        checkout2 = new BookCheckout();
        checkout2.setId(2);
        checkout2.setBook(book1);
        checkout2.setMember(member1);
        checkout2.setHandledByLibrarian(librarian1);
        checkout2.setCheckoutDate(LocalDate.now().minusDays(30));
        checkout2.setDueDate(LocalDate.now().minusDays(16));
        checkout2.setStatus(CheckoutStatus.RETURNED);
    }

    @Test
    void getAllLibrarians_shouldReturnAllLibrarians() {
        when(librarianRepository.findAll()).thenReturn(Arrays.asList(librarian1, librarian2));

        List<Librarian> librarians = librarianService.getAllLibrarians();

        assertNotNull(librarians);
        assertEquals(2, librarians.size());
        assertTrue(librarians.contains(librarian1));
        assertTrue(librarians.contains(librarian2));
        verify(librarianRepository, times(1)).findAll();
    }

    @Test
    void getLibrarianById_shouldReturnLibrarian_whenLibrarianExists() {
        when(librarianRepository.findById(1)).thenReturn(Optional.of(librarian1));

        Librarian foundLibrarian = librarianService.getLibrarianById(1);

        assertNotNull(foundLibrarian);
        assertEquals(1, foundLibrarian.getId());
        verify(librarianRepository, times(1)).findById(1);
    }

    @Test
    void getLibrarianById_shouldReturnNull_whenLibrarianDoesNotExist() {
        when(librarianRepository.findById(99)).thenReturn(Optional.empty());

        Librarian foundLibrarian = librarianService.getLibrarianById(99);

        assertNull(foundLibrarian);
        verify(librarianRepository, times(1)).findById(99);
    }

    @Test
    void addLibrarian_shouldReturnSavedLibrarian() {
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian1);

        Librarian newLibrarian = librarianService.addLibrarian(librarian1);

        assertNotNull(newLibrarian);
        assertEquals(librarian1.getFirstName(), newLibrarian.getFirstName());
        verify(librarianRepository, times(1)).save(librarian1);
    }

    @Test
    void update_shouldReturnUpdatedLibrarian() {
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian1);

        Librarian updatedLibrarian = librarianService.update(librarian1);

        assertNotNull(updatedLibrarian);
        assertEquals(librarian1.getFirstName(), updatedLibrarian.getFirstName());
        verify(librarianRepository, times(1)).save(librarian1);
    }

    @Test
    void deleteLibrarian_shouldCallRepositoryDelete() {
        doNothing().when(librarianRepository).deleteById(1);

        librarianService.deleteLibrarian(1);

        verify(librarianRepository, times(1)).deleteById(1);
    }

    @Test
    void getCheckouts_shouldReturnCheckoutsForLibrarian() {
        when(checkoutRepository.findByHandledByLibrarianId(1)).thenReturn(Arrays.asList(checkout1, checkout2));

        List<BookCheckout> checkouts = librarianService.getCheckouts(1);

        assertNotNull(checkouts);
        assertEquals(2, checkouts.size());
        assertTrue(checkouts.contains(checkout1));
        assertTrue(checkouts.contains(checkout2));
        verify(checkoutRepository, times(1)).findByHandledByLibrarianId(1);
    }

    @Test
    void findCheckoutDtosByLibrarianId_shouldReturnListOfDTOs() {
        when(checkoutRepository.findByHandledByLibrarianId(1)).thenReturn(Arrays.asList(checkout1, checkout2));

        // Mock the static method call to LibrarianMapper.toLibrarianCheckoutDTO
        try (var mockedMapper = mockStatic(LibrarianMapper.class)) {
            mockedMapper.when(() -> LibrarianMapper.toLibrarianCheckoutDTO(checkout1))
                    .thenReturn(new LibrarianCheckoutDTO(
                            checkout1.getId(),
                            checkout1.getMember().getId(),
                            checkout1.getMember().getFirstName() + " " + checkout1.getMember().getLastName(),
                            checkout1.getBook().getId(),
                            checkout1.getBook().getTitle(),
                            checkout1.getStatus().name(),
                            checkout1.getCheckoutDate().toString(),
                            checkout1.getDueDate().toString()
                    ));
            mockedMapper.when(() -> LibrarianMapper.toLibrarianCheckoutDTO(checkout2))
                    .thenReturn(new LibrarianCheckoutDTO(
                            checkout2.getId(),
                            checkout2.getMember().getId(),
                            checkout2.getMember().getFirstName() + " " + checkout2.getMember().getLastName(),
                            checkout2.getBook().getId(),
                            checkout2.getBook().getTitle(),
                            checkout2.getStatus().name(),
                            checkout2.getCheckoutDate().toString(),
                            checkout2.getDueDate().toString()
                    ));

            List<LibrarianCheckoutDTO> dtos = librarianService.findCheckoutDtosByLibrarianId(1);

            assertNotNull(dtos);
            assertEquals(2, dtos.size());
            assertEquals(checkout1.getId(), dtos.get(0).getCheckoutId());
            assertEquals(checkout2.getId(), dtos.get(1).getCheckoutId());
            verify(checkoutRepository, times(1)).findByHandledByLibrarianId(1);
            mockedMapper.verify(() -> LibrarianMapper.toLibrarianCheckoutDTO(checkout1), times(1));
            mockedMapper.verify(() -> LibrarianMapper.toLibrarianCheckoutDTO(checkout2), times(1));
        }
    }
}
