package be.kdg.programming3.repository.interfaces;


import be.kdg.programming3.domain.Book;
import java.util.List;

public interface BookRepository {
    List<Book> findAll();
    void save(Book book);
}
