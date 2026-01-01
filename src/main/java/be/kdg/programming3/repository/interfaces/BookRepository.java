package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.Book;
import java.util.List;

public interface BookRepository {
    List<Book> findAllBooks();
    Book findById(int id);
    Book save(Book book);
    int updateAvailability(int id, boolean available);
    int delete(int id);
    int updateBook(Book book);
    int deleteBookById(int id);
}
