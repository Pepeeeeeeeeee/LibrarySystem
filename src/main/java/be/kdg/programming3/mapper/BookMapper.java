package be.kdg.programming3.mapper;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.dto.BookDTO;

public class BookMapper {
    public static BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setPages(book.getPages());
        dto.setPrice(book.getPrice());
        dto.setGenre(book.getGenre());
        dto.setAvailable(book.isAvailable());
        dto.setCoverImageUrl(book.getCoverImageUrl());
        dto.setLocation(book.getLocation());
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
}
