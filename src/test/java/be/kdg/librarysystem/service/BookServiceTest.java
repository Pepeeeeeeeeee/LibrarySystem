package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.enums.Genre;
import be.kdg.librarysystem.repository.BookCheckoutJpaRepository;
import be.kdg.librarysystem.repository.BookJpaRepository;
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
class BookServiceTest {

    @Mock
    private BookJpaRepository bookRepository;

    @Mock
    private BookCheckoutJpaRepository checkoutRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;

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

        book2 = new Book();
        book2.setId(2);
        book2.setTitle("Title 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("0987654321");
        book2.setPublicationDate(LocalDate.of(2010, 5, 10));
        book2.setPages(300);
        book2.setPrice(15.50);
        book2.setGenre(Genre.FANTASY);
        book2.setAvailable(true);
        book2.setCoverImageUrl("http://example.com/cover2.jpg");
        book2.setLocation("Shelf B2");
    }

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(2, books.size());
        assertTrue(books.contains(book1));
        assertTrue(books.contains(book2));
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void addBook_shouldReturnSavedBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        Book savedBook = bookService.addBook(book1);

        assertNotNull(savedBook);
        assertEquals(book1.getTitle(), savedBook.getTitle());
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void findById_shouldReturnBook_whenBookExists() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book1));

        Book foundBook = bookService.findById(1);

        assertNotNull(foundBook);
        assertEquals(book1.getTitle(), foundBook.getTitle());
        verify(bookRepository, times(1)).findById(1);
    }

    @Test
    void findById_shouldReturnNull_whenBookDoesNotExist() {
        when(bookRepository.findById(99)).thenReturn(Optional.empty());

        Book foundBook = bookService.findById(99);

        assertNull(foundBook);
        verify(bookRepository, times(1)).findById(99);
    }

    @Test
    void updateBook_shouldReturnUpdatedBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        Book updatedBook = bookService.updateBook(book1);

        assertNotNull(updatedBook);
        assertEquals(book1.getTitle(), updatedBook.getTitle());
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void deleteBook_shouldCallRepositoryDelete() {
        doNothing().when(bookRepository).deleteById(1);

        bookService.deleteBook(1);

        verify(bookRepository, times(1)).deleteById(1);
    }
}
