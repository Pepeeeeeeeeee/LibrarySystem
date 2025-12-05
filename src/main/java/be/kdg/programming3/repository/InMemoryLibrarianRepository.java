package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.repository.interfaces.DataRepository;
import be.kdg.programming3.repository.interfaces.LibrarianRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Profile({"web", "console"})
public class InMemoryLibrarianRepository implements LibrarianRepository {

    private final DataRepository dataRepository;

    public InMemoryLibrarianRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<Librarian> findAllLibrarians() {
        return dataRepository.findAllLibrarians();
    }

    @Override
    public Librarian findById(int id) {
        return null;
    }

    @Override
    public int save(Librarian librarian) {
        dataRepository.findAllLibrarians().add(librarian);
        return 1;
    }

    @Override
    public int update(Librarian librarian) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }
}
