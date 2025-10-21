package be.kdg.programming3.dto;

import be.kdg.programming3.domain.enums.Genre;
import java.time.LocalDate;

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

    public BookDTO() {} // Required for Spring form binding

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public LocalDate getPublicationDate() { return publicationDate; }
    public void setPublicationDate(LocalDate publicationDate) { this.publicationDate = publicationDate; }

    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
