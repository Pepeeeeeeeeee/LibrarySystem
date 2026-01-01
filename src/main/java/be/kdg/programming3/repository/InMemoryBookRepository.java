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
        return dataRepository.findAllBooks().stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Book save(Book book) {
        dataRepository.findAllBooks().add(book);
        return null;
    }

    @Override
    public int updateAvailability(int id, boolean available) {
        return 0;
    }

    @Override
    public int delete(int id) {
        boolean removed = dataRepository.findAllBooks()
                .removeIf(b -> b.getId() == id);

        return removed ? 1 : 0;
    }

    @Override
    public int updateBook(Book book) {
        boolean removed = dataRepository.findAllBooks()
                .removeIf(b -> b.getId() == book.getId());

        save(book);
        return removed ? 1 : 0;
    }

    @Override
    public int deleteBookById(int id){
        boolean removed = dataRepository.findAllBooks()
                .removeIf(b -> b.getId() == id);

        return removed ? 1 : 0;
    }
}