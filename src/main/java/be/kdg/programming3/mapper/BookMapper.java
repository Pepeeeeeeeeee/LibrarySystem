package be.kdg.programming3.mapper;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.dto.BookDTO;
import be.kdg.programming3.viewModel.BookViewModel;

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

    public static Book vmToEntity(BookViewModel bvm) {
        return new Book(
                bvm.getId(),
                bvm.getIsbn(),
                bvm.getTitle(),
                bvm.getAuthor(),
                bvm.getPublicationDate(),
                bvm.getPages(),
                bvm.getPrice(),
                bvm.getGenre(),
                bvm.isAvailable(),
                bvm.getCoverImageUrl(),
                bvm.getLocation()
        );
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
                book.getLocation()
        );
    }
}
