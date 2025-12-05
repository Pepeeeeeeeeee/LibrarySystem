package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.BookCheckout;
import java.util.List;
import java.util.Optional;

public interface CheckoutRepository {
    List<BookCheckout> findAll();
    BookCheckout findById(int id);
    int save(BookCheckout checkout);
    int updateReturnDate(int id, java.time.LocalDate returnDate);
    int delete(int id);
    Optional<BookCheckout> findByBookId(int bookId);
    List<Optional<BookCheckout>> findByLibrarianId(int librarianId);
    List<Optional<BookCheckout>> findByMemberId(int memberId);
}
