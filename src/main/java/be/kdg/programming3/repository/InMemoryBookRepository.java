package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.repository.interfaces.BookRepository;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Profile({"web", "console"})
public class InMemoryBookRepository implements BookRepository {

    private final DataRepository dataRepository;

    public InMemoryBookRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<Book> findAllBooks() {
        return dataRepository.findAllBooks();
    }

    @Override
    public Book findById(int id) {
        return null;
    }

    @Override
    public int save(Book book) {
        dataRepository.findAllBooks().add(book);
        return 0;
    }

    @Override
    public int updateAvailability(int id, boolean available) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }

    @Override
    public int updateBook(Book book) {
        return 0;
    }

    @Override
    public int deleteBookById(int id){
        return 0;
    }
}