package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.CheckoutStatus;
import lombok.Data;
import java.time.LocalDate;
import static java.lang.String.format;

@Data
public class BookCheckout {
    private final int id;
    private final Book book;
    private final Member member;
    private final LocalDate checkoutDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private final boolean renewed;
    private final CheckoutStatus status;
    private final Librarian handledByLibrarian;

    public BookCheckout(int id, Book book, Member member, LocalDate checkoutDate,
                        LocalDate dueDate, LocalDate returnDate,
                        boolean renewed, CheckoutStatus status,
                        Librarian handledByLibrarian) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.renewed = renewed;
        this.status = status;
        this.handledByLibrarian = handledByLibrarian;

        book.addCheckout(this);
    }

    @Override
    public String toString() {
        return format("""
            BookCheckout:
            Id = %d
            Book Title = %s
            Book ISBN = %s
            Member = %s %s
            Checkout Date = %s
            Due Date = %s
            Return Date = %s
            Renewed = %b
            Status = %s
            Handled By = %s
            """,
                id,
                book.getTitle(),
                book.getIsbn(),
                member.getFirstName(),
                member.getLastName(),
                checkoutDate,
                dueDate,
                returnDate != null ? returnDate : "Not returned",
                renewed,
                status,
                handledByLibrarian != null ? handledByLibrarian.getFirstName() + " " + handledByLibrarian.getLastName() : "N/A"
        );
    }
}
