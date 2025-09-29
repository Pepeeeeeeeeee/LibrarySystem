package be.kdg.programming3.service;

import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LibrarianService {
    private final DataRepository repo;

    public LibrarianService(DataRepository repo) {
        this.repo = repo;
    }

    public List<Librarian> getAllLibrarians() {
        return repo.findAllLibrarians();
    }

    public void addLibrarian(Librarian librarian) {
        repo.findAllLibrarians().add(librarian);
    }
}
