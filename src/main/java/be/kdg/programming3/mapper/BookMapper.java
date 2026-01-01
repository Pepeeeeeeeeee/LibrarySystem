package be.kdg.programming3.mapper;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.Comic;
import be.kdg.programming3.dto.BookDTO;
import be.kdg.programming3.viewModel.BookViewModel;

public class BookMapper {
    public static BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setGenre(book.getGenre());
        dto.setAvailable(book.isAvailable());
        dto.setCoverImageUrl(book.getCoverImageUrl());
        dto.setPrice(book.getPrice());
        dto.setIsbn(book.getIsbn());
        dto.setPages(book.getPages());

        if (book instanceof Comic comic) {
            dto.setComic(true);
            dto.setArtist(comic.getArtist());
        }

        return dto;
    }


    public static Book toEntity(BookDTO dto) {
        return new Book(
                dto.getId(),
                dto.getIsbn(),
                dto.getTitle(),
                dto.getAuthor(),
                dto.getPublicationDate(),
                dto.getPages(),
                dto.getPrice(),
                dto.getGenre(),
                dto.isAvailable(),
                dto.getCoverImageUrl(),
                dto.getLocation()
        );
    }

    public static Book vmToEntity(BookViewModel bvm) {
        if (bvm.isComic()) {
            Comic comic = new Comic();
            comic.setId(bvm.getId());
            comic.setIsbn(bvm.getIsbn());
            comic.setTitle(bvm.getTitle());
            comic.setAuthor(bvm.getAuthor());
            comic.setPublicationDate(bvm.getPublicationDate());
            comic.setPages(bvm.getPages());
            comic.setPrice(bvm.getPrice());
            comic.setGenre(bvm.getGenre());
            comic.setAvailable(bvm.isAvailable());
            comic.setCoverImageUrl(bvm.getCoverImageUrl());
            comic.setLocation(bvm.getLocation());
            comic.setArtist(bvm.getArtist());
            return comic;
        } else {
            Book book = new Book();
            book.setId(bvm.getId());
            book.setIsbn(bvm.getIsbn());
            book.setTitle(bvm.getTitle());
            book.setAuthor(bvm.getAuthor());
            book.setPublicationDate(bvm.getPublicationDate());
            book.setPages(bvm.getPages());
            book.setPrice(bvm.getPrice());
            book.setGenre(bvm.getGenre());
            book.setAvailable(bvm.isAvailable());
            book.setCoverImageUrl(bvm.getCoverImageUrl());
            book.setLocation(bvm.getLocation());
            return book;
        }
    }

    public static BookViewModel entityToVm(Book book) {
        return new BookViewModel(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationDate(),
                book.getPages(),
                book.getPrice(),
                book.getGenre(),
                book.isAvailable(),
                book.getCoverImageUrl(),
                book.getLocation(),
                book instanceof Comic,
                book instanceof Comic c ? c.getArtist() : null
        );
    }
}
