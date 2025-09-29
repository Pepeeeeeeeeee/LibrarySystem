package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.CheckoutStatus;
import java.time.LocalDate;
import static java.lang.String.format;

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

        // Link this checkout to book and member
        book.addCheckout(this);
    }

    // Getters
    public int getId() { return id; }
    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isRenewed() { return renewed; }
    public CheckoutStatus getStatus() { return status; }
    public Librarian getHandledByLibrarian() { return handledByLibrarian; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
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
