package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.Librarian;
import java.util.List;

public interface LibrarianRepository {
    List<Librarian> findAll();
    void save(Librarian librarian);
}
