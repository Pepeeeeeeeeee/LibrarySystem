package be.kdg.programming3.service;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.domain.enums.CheckoutStatus;
import be.kdg.programming3.repository.interfaces.BookRepository;
import be.kdg.programming3.repository.interfaces.CheckoutRepository;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
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

//    public Map.Entry<LocalDate, Boolean> getCheckoutDetails(int bookId) {
//        return checkoutRepository.findByBookId(bookId)
//                .map(checkout -> new AbstractMap.SimpleEntry<>(
//                        checkout.getReturnDate(),
//                        checkout.getStatus() != CheckoutStatus.RETURNED
//                ))
//                .orElse(null);
//    }

    public Optional<BookCheckout> getCheckoutDetailsObj(int bookId) {
        return checkoutRepository.findByBookId(bookId);
    }

    public Book findById(int id){
        return bookRepository.findById(id);
    }

    public int updateBook(Book book){
        return bookRepository.updateBook(book);
    }

    public void deleteBook(int id) {
        bookRepository.deleteBookById(id);
    }
}