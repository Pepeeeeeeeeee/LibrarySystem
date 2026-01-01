package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.CheckoutStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "book_checkouts")
@Data
@NoArgsConstructor
public class BookCheckout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Member member;

    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean renewed;

    @Enumerated(EnumType.STRING)
    private CheckoutStatus status;

    @ManyToOne
    private Librarian handledByLibrarian;

    /**
     * LEGACY constructor — DO NOT USE for new code.
     */
    @Deprecated
    public BookCheckout(
            int id,
            Book book,
            Member member,
            LocalDate checkoutDate,
            LocalDate dueDate,
            LocalDate returnDate,
            boolean renewed,
            CheckoutStatus status,
            Librarian handledByLibrarian
    ) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.renewed = renewed;
        this.status = status;
        this.handledByLibrarian = handledByLibrarian;

        // Maintain bidirectional consistency (legacy behavior)
        book.addCheckout(this);
        member.getCheckouts().add(this);
    }
}

