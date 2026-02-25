package be.kdg.librarysystem.repository.interfaces;

import be.kdg.librarysystem.domain.*;
import java.util.List;

public interface DataRepository {
    List<Book> findAllBooks();
    List<Member> findAllMembers();
    List<Librarian> findAllLibrarians();
    List<BookCheckout> findAllBookCheckouts();
    List<User> findAllUsers();
}