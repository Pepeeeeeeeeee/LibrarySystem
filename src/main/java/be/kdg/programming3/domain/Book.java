package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static java.lang.String.format;

@Data
@RequiredArgsConstructor
public class Book {
    private final int id;
    private final String isbn;
    private final String title;
    private final String author;
    private final LocalDate publicationDate;
    private final int pages;
    private final double price;
    private final Genre genre;
    private final boolean isAvailable;
    private final String coverImageUrl;
    private final String location;

    private List<BookCheckout> checkouts = new ArrayList<>();

    public Book addCheckout(BookCheckout checkout) {
        if (!checkouts.contains(checkout)) {
            checkouts.add(checkout);
        }
        return this;
    }

    @Override
    public String toString() {
        return format("""
                Book:
                Id = %d
                Isbn = %s
                Title = %s
                Author = %s
                Publication date = %s
                Pages = %d
                Price = %f
                Genre = %s
                Available = %b
                Location = %s
                Members Count = %d
                
                """, id, isbn, title, author, publicationDate, pages, price, genre, isAvailable, location, checkouts.size());
    }
}
