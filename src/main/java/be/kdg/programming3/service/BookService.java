package be.kdg.programming3.service;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.exception.BookProcessingException;
import be.kdg.programming3.repository.interfaces.BookRepository;
import be.kdg.programming3.repository.interfaces.CheckoutRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public Optional<BookCheckout> getCheckoutDetailsObj(int bookId) {
        return checkoutRepository.findByBookId(bookId);
    }

    public Book findById(int id){
        Book book =  bookRepository.findById(id);
        if(book == null){
            throw new BookProcessingException("Book doesn't exist");
        }
        return book;
    }

    public int updateBook(Book book){
        return bookRepository.updateBook(book);
    }

    public void deleteBook(int id) {
        bookRepository.deleteBookById(id);
    }
}