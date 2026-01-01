package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.repository.interfaces.LibrarianRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Profile("jpa")
public interface LibrarianJpaRepository extends JpaRepository<Librarian, Integer>, LibrarianRepository {

    @Override
    default List<Librarian> findAllLibrarians() {
        return findAll();
    }

    @Override
    default Librarian findById(int id) {
        return findById((Integer) id).orElse(null);
    }

    @Override
    default Librarian save(Librarian librarian) {
        return saveAndFlush(librarian);
    }

    @Override
    default int update(Librarian librarian) {
        saveAndFlush(librarian);
        return 1;
    }

    @Override
    default int delete(int id) {
        deleteById(id);
        return 1;
    }
}
