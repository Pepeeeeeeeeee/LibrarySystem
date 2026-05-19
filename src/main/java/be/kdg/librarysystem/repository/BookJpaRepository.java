package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.Comic;
import be.kdg.librarysystem.domain.enums.Genre;
import be.kdg.librarysystem.repository.interfaces.BookRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Integer>, BookRepository {
    @Override
    List<Book> findByGenre(Genre genre);

    @Override
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Override
    @Query("""
            select c
            from Comic c
            where lower(c.artist) = lower(:artist)
            """)
    List<Comic> findComicsByArtist(String artist);
}
