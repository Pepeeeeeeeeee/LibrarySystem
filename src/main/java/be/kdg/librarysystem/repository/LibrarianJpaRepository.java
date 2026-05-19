package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.Librarian;
import be.kdg.librarysystem.repository.interfaces.LibrarianRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibrarianJpaRepository extends JpaRepository<Librarian, Integer>, LibrarianRepository {

}
