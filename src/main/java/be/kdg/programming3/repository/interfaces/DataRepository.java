package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.*;
import java.util.List;

public interface DataRepository {
    List<Book> findAllBooks();
    List<Member> findAllMembers();
    List<Librarian> findAllLibrarians();
    List<BookCheckout> findAllBookCheckouts();
    List<User> findAllUsers();
}