package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Librarian;
import be.kdg.librarysystem.dto.LibrarianCheckoutDTO;
import be.kdg.librarysystem.mapper.LibrarianMapper;
import be.kdg.librarysystem.repository.BookCheckoutJpaRepository;
import be.kdg.librarysystem.repository.LibrarianJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LibrarianService {

    private final LibrarianJpaRepository repository;
    private final BookCheckoutJpaRepository checkoutRepository;

    public LibrarianService(
            LibrarianJpaRepository repository,
            BookCheckoutJpaRepository checkoutRepository
    ) {
        this.repository = repository;
        this.checkoutRepository = checkoutRepository;
    }

    public List<Librarian> getAllLibrarians() {
        return repository.findAll();
    }

    public Librarian getLibrarianById(int id) {
        return repository.findById(id)
                .orElse(null);
    }

    public Librarian addLibrarian(Librarian librarian) {
        return repository.save(librarian);
    }

    public Librarian update(Librarian librarian) {
        return repository.save(librarian);
    }

    public void deleteLibrarian(int id) {
        repository.deleteById(id);
    }

    public List<BookCheckout> getCheckouts(int librarianId) {
        return checkoutRepository.findByHandledByLibrarianId(librarianId);
    }

    public List<LibrarianCheckoutDTO> findCheckoutDtosByLibrarianId(int librarianId) {
        return getCheckouts(librarianId).stream()
                .map(LibrarianMapper::toLibrarianCheckoutDTO)
                .collect(Collectors.toList());
    }
}