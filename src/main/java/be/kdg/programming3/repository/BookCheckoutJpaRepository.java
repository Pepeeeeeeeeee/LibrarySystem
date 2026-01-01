package be.kdg.programming3.repository;

import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.repository.interfaces.CheckoutRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public interface BookCheckoutJpaRepository extends JpaRepository<BookCheckout, Integer>, CheckoutRepository {

    @Override
    default BookCheckout findById(int id) {
        return findById((Integer) id).orElse(null);
    }

    @Override
    default BookCheckout save(BookCheckout checkout) {
        return saveAndFlush(checkout);
    }

    @Override
    default int updateReturnDate(int id, LocalDate returnDate) {
        BookCheckout checkout = findById(id);
        if (checkout != null) {
            checkout.setReturnDate(returnDate);
            saveAndFlush(checkout);
            return 1;
        }
        return 0;
    }

    @Override
    default int delete(int id) {
        deleteById(id);
        return 1;
    }

    @Override
    default Optional<BookCheckout> findByBookId(int bookId) {
        return findAll().stream().filter(c -> c.getBook().getId() == bookId).findFirst();
    }

    @Override
    default List<Optional<BookCheckout>> findByLibrarianId(int librarianId) {
        return findAll().stream()
                .filter(c -> c.getHandledByLibrarian() != null && c.getHandledByLibrarian().getId() == librarianId)
                .map(Optional::of)
                .toList();
    }

    @Override
    default List<Optional<BookCheckout>> findByMemberId(int memberId) {
        return findAll().stream()
                .filter(c -> c.getMember() != null && c.getMember().getId() == memberId)
                .map(Optional::of)
                .toList();
    }
}
