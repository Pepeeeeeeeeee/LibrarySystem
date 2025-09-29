package be.kdg.programming3.service;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.stereotype.Service;
import java.util.List;

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
}
