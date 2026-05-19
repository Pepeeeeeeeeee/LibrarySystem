package be.kdg.librarysystem.domain;

import be.kdg.librarysystem.domain.enums.Genre;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "book_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("BOOK")
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String isbn;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private int pages;
    private double price;
    @Enumerated(EnumType.STRING)
    private Genre genre;

    private boolean isAvailable;
    private String coverImageUrl;
    private String location;

    @OneToMany(
            mappedBy = "book",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<BookCheckout> checkouts = new ArrayList<>();


    /**
     * LEGACY constructor — DO NOT USE for new code.
     * ID is ignored by JPA when persisting.
     */
    @Deprecated
    public Book(
            int id,
            String isbn,
            String title,
            String author,
            LocalDate publicationDate,
            int pages,
            double price,
            Genre genre,
            boolean isAvailable,
            String coverImageUrl,
            String location
    ) {
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

    public void addCheckout(BookCheckout checkout) {
        checkouts.add(checkout);
        checkout.setBook(this);
    }
}