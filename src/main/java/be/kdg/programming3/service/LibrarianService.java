package be.kdg.programming3.service;

import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.repository.interfaces.CheckoutRepository;
import be.kdg.programming3.repository.interfaces.LibrarianRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibrarianService {
    private final LibrarianRepository repo;
    private final CheckoutRepository checkoutRepository;

    public LibrarianService(LibrarianRepository repo, CheckoutRepository checkoutRepository) {
        this.repo = repo;
        this.checkoutRepository = checkoutRepository;
    }

    public List<Librarian> getAllLibrarians() {
        return repo.findAllLibrarians();
    }

    public Librarian getLibrarianById(int id) {
        return repo.findById(id);
    }

    public void addLibrarian(Librarian librarian) {
        repo.save(librarian);
    }

    public int update(Librarian librarian) {
        return repo.update(librarian);
    }

    public int deleteLibrarian(int id) {
        return repo.delete(id);
    }

    public List<Optional<BookCheckout>> getCheckOuts(int id){
        return checkoutRepository.findByLibrarianId(id);
    }
}
