package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.repository.interfaces.DataRepository;
import be.kdg.programming3.repository.interfaces.LibrarianRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class InMemoryLibrarianRepository implements LibrarianRepository {

    private final DataRepository dataRepository;

    public InMemoryLibrarianRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<Librarian> findAll() {
        return dataRepository.findAllLibrarians();
    }

    @Override
    public void save(Librarian librarian) {
        dataRepository.findAllLibrarians().add(librarian);
    }
}
