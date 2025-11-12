package be.kdg.programming3.service;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.enums.CheckoutStatus;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    private final DataRepository repo;

    public BookService(DataRepository repo) {
        this.repo = repo;
    }

    public List<Book> getAllBooks() {
        return repo.findAllBooks();
    }

    public void addBook(Book book) {
        repo.findAllBooks().add(book);
    }

    public Map.Entry<LocalDate, Boolean> getCheckoutDetails(long id) {
        return repo.findAllBookCheckouts().stream()
                .filter(c -> c.getBook().getId() == id) .findFirst()
                .map(c -> new AbstractMap.SimpleEntry<>(c.getReturnDate(), c.getStatus() != CheckoutStatus.RETURNED))
                .orElse(null); }
}
