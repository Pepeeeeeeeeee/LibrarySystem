package be.kdg.programming3.repository;

import be.kdg.programming3.config.DataFactory;
import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.domain.Member;
import be.kdg.programming3.repository.interfaces.DataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryDataRepository implements DataRepository {
    private final List<Book> books = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<Librarian> librarians = new ArrayList<>();
    private final List<BookCheckout> checkouts = new ArrayList<>();

    @PostConstruct
    public void seed() {
        DataFactory.seed(books, members, librarians, checkouts);
    }

    @Override
    public List<Book> findAllBooks() {
        return books;
    }

    @Override
    public List<Member> findAllMembers() {
        return members;
    }

    @Override
    public List<Librarian> findAllLibrarians() {
        return librarians;
    }

    @Override
    public List<BookCheckout> findAllBookCheckouts() {
        return checkouts;
    }
}
