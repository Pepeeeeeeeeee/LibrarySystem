package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.domain.Member;
import java.util.List;

public interface DataRepository {
    List<Book> findAllBooks();
    List<Member> findAllMembers();
    List<Librarian> findAllLibrarians();
    List<BookCheckout> findAllBookCheckouts();
}