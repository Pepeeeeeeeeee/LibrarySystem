package be.kdg.librarysystem.repository.interfaces;

import be.kdg.librarysystem.domain.BookCheckout;
import java.util.List;
import java.util.Optional;

public interface CheckoutRepository {
    Optional<BookCheckout> findByBookId(int bookId);
    List<BookCheckout> findByHandledByLibrarianId(int librarianId);
    List<BookCheckout> findByMemberId(int memberId);
}
