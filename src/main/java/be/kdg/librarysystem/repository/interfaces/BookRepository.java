package be.kdg.librarysystem.repository.interfaces;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.Comic;
import be.kdg.librarysystem.domain.enums.Genre;

import java.util.List;

public interface BookRepository {
    List<Book> findByGenre(Genre genre);
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Comic> findComicsByArtist(String artist);
}
