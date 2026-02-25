package be.kdg.librarysystem.dto;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.Librarian;
import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.CheckoutStatus;

import java.time.LocalDate;

public class CheckoutDTO {
    private int id;
    private Book book;
    private Member member;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean renewed;
    private CheckoutStatus status;
    private Librarian handledByLibrarian;

    public CheckoutDTO(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isRenewed() {
        return renewed;
    }

    public void setRenewed(boolean renewed) {
        this.renewed = renewed;
    }

    public CheckoutStatus getStatus() {
        return status;
    }

    public void setStatus(CheckoutStatus status) {
        this.status = status;
    }

    public Librarian getHandledByLibrarian() {
        return handledByLibrarian;
    }

    public void setHandledByLibrarian(Librarian handledByLibrarian) {
        this.handledByLibrarian = handledByLibrarian;
    }
}
