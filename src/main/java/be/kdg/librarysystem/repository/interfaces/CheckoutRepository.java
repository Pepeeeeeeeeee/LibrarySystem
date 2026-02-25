package be.kdg.librarysystem.repository.interfaces;

import be.kdg.librarysystem.domain.BookCheckout;
import java.util.List;
import java.util.Optional;

public interface CheckoutRepository {
    List<BookCheckout> findAll();
    BookCheckout findById(int id);
    BookCheckout save(BookCheckout checkout);
    int updateReturnDate(int id, java.time.LocalDate returnDate);
    int delete(int id);
    Optional<BookCheckout> findByBookId(int bookId);
    List<Optional<BookCheckout>> findByLibrarianId(int librarianId);
    List<Optional<BookCheckout>> findByMemberId(int memberId);
}
