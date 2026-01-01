package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.Comic;
import be.kdg.programming3.domain.enums.Genre;
import be.kdg.programming3.repository.interfaces.BookRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Profile("jpa")
public interface BookJpaRepository extends JpaRepository<Book, Integer>, BookRepository {

    @Override
    default List<Book> findAllBooks() {
        return findAll();
    }

    @Override
    default Book findById(int id) {
        return findById((Integer) id).orElse(null);
    }

    @Override
    default Book save(Book book) {
        return saveAndFlush(book);
    }

    @Override
    default int updateAvailability(int id, boolean available) {
        Book book = findById(id);
        if (book != null) {
            book.setAvailable(available);
            saveAndFlush(book);
            return 1;
        }
        return 0;
    }

    @Override
    default int updateBook(Book book) {
        saveAndFlush(book);
        return 1;
    }

    @Override
    default int delete(int id) {
        deleteById(id);
        return 1;
    }

    @Override
    default int deleteBookById(int id) {
        deleteById(id);
        return 1;
    }

    List<Book> findByGenre(Genre genre);

    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("select b from Comic b where b.artist = :artist")
    List<Comic> findComicsByArtist(@Param("artist") String artist);
}
