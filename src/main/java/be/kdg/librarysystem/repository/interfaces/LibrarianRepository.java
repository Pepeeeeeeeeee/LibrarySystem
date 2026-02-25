package be.kdg.librarysystem.repository.interfaces;

import be.kdg.librarysystem.domain.Librarian;
import java.util.List;

public interface LibrarianRepository {
    List<Librarian> findAllLibrarians();
    Librarian findById(int id);
    Librarian save(Librarian librarian);
    int update(Librarian librarian);
    int delete(int id);
}
