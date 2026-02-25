package be.kdg.librarysystem.dto;

import be.kdg.librarysystem.domain.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private int pages;
    private double price;
    private Genre genre;
    private boolean available;
    private String coverImageUrl;
    private String location;

    private boolean comic;
    private String artist;
}
