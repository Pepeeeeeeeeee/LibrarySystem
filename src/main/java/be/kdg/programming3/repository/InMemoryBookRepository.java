package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.repository.interfaces.BookRepository;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final DataRepository dataRepository;

    public InMemoryBookRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<Book> findAll() {
        return dataRepository.findAllBooks();
    }

    @Override
    public void save(Book book) {
        dataRepository.findAllBooks().add(book);
    }
}