package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.Genre;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static java.lang.String.format;

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

    public Book(int id, String isbn, String title, String author, LocalDate publicationDate,
                int pages, double price, Genre genre, boolean isAvailable,
                String coverImageUrl, String location) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.pages = pages;
        this.price = price;
        this.genre = genre;
        this.isAvailable = isAvailable;
        this.coverImageUrl = coverImageUrl;
        this.location = location;
    }
    
    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public int getPages() {
        return pages;
    }

    public double getPrice() {
        return price;
    }

    public Genre getGenre() {
        return genre;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String getLocation() {
        return location;
    }


    public List<BookCheckout> getCheckouts() {
        return checkouts;
    }

    // Relationship method (many-to-many)
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
