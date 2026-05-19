package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.enums.CheckoutStatus;
import be.kdg.librarysystem.dto.BookCheckoutDetailsDTO;
import be.kdg.librarysystem.repository.BookCheckoutJpaRepository;
import be.kdg.librarysystem.repository.BookJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookJpaRepository bookRepository;
    private final BookCheckoutJpaRepository checkoutRepository;

    public BookService(
            BookJpaRepository bookRepository,
            BookCheckoutJpaRepository checkoutRepository
    ) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<BookCheckoutDetailsDTO> getCheckoutDetails(int bookId) {

        return checkoutRepository.findByBookId(bookId)
                .map(checkout -> {

                    boolean checkedOut =
                            checkout.getStatus() != CheckoutStatus.RETURNED;

                    String expectedReturnDate =
                            checkedOut
                                    ? checkout.getDueDate().toString()
                                    : (checkout.getReturnDate() != null
                                            ? checkout.getReturnDate().toString()
                                            : "None");

                    int librarianId = -1;
                    String librarianName = null;

                    if (checkout.getHandledByLibrarian() != null) {
                        librarianId =
                                checkout.getHandledByLibrarian().getId();

                        librarianName =
                                checkout.getHandledByLibrarian().getFirstName()
                                        + " "
                                        + checkout.getHandledByLibrarian().getLastName();
                    }

                    int memberId = -1;
                    String memberName = null;

                    if (checkout.getMember() != null) {
                        memberId = checkout.getMember().getId();

                        memberName =
                                checkout.getMember().getFirstName()
                                        + " "
                                        + checkout.getMember().getLastName();
                    }

                    return new BookCheckoutDetailsDTO(
                            checkedOut,
                            expectedReturnDate,
                            librarianId,
                            librarianName,
                            memberId,
                            memberName
                    );
                });
    }

    public Book findById(int id) {
        return bookRepository.findById(id)
                .orElse(null);
    }

    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }
}