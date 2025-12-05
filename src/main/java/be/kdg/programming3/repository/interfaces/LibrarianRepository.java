package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.Librarian;
import java.util.List;

public interface LibrarianRepository {
    List<Librarian> findAllLibrarians();
    Librarian findById(int id);
    int save(Librarian librarian);
    int update(Librarian librarian);
    int delete(int id);
}
