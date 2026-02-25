package be.kdg.librarysystem.viewModel;

import be.kdg.librarysystem.domain.enums.Genre;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookViewModel {
    private int id;

    @NotBlank(message = "{validation.book.isbn.notBlank}")
    private String isbn;

    @NotBlank(message = "{validation.book.title.notBlank}")
    private String title;

    @NotBlank(message = "{validation.book.author.notBlank}")
    private String author;

    @NotNull(message = "{validation.book.publicationDate.notNull}")
    @PastOrPresent(message = "{validation.book.publicationDate.pastOrPresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @Positive(message = "{validation.book.pages.positive}")
    private int pages;

    @PositiveOrZero(message = "{validation.book.price.positiveOrZero}")
    private double price;

    @NotNull(message = "{validation.book.genre.notNull}")
    private Genre genre;

    private boolean available = true;

    @NotBlank(message = "{validation.book.coverImageUrl.notBlank}")
    @Pattern(regexp = "^(http|https)://.*$", message = "{validation.book.coverImageUrl.pattern}")
    private String coverImageUrl;

    @NotBlank(message = "{validation.book.location.notBlank}")
    private String location;

    private boolean comic;

    private String artist;

    @AssertTrue(message = "{validation.comic.artist}")
    public boolean isArtistValid() {
        return !comic || (artist != null && !artist.isBlank() && artist.length() >= 2);
    }

}
