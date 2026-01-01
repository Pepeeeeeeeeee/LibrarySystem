package be.kdg.programming3.repository;

import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.repository.interfaces.CheckoutRepository;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Profile({"web", "console"})
public class InMemoryBookCheckoutRepository implements CheckoutRepository {
    private final DataRepository dataRepository;

    public InMemoryBookCheckoutRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<BookCheckout> findAll() {
        return dataRepository.findAllBookCheckouts();
    }

    @Override
    public BookCheckout findById(int id) {
        return null;
    }

    @Override
    public BookCheckout save(BookCheckout bookCheckout) {
        dataRepository.findAllBookCheckouts().add(bookCheckout);
        return null;
    }

    @Override
    public int updateReturnDate(int id, LocalDate returnDate) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }

    @Override
    public Optional<BookCheckout> findByBookId(int bookId) {
        return dataRepository.findAllBookCheckouts().stream()
                .filter(c -> c.getBook().getId() == bookId)
                .findFirst();
    }

    @Override
    public List<Optional<BookCheckout>> findByLibrarianId(int librarianId) {
        return dataRepository.findAllBookCheckouts().stream().
                filter(c -> c.getHandledByLibrarian().getId() == librarianId).map(Optional::of)
                .toList();
    }

    @Override
    public List<Optional<BookCheckout>> findByMemberId(int memberId) {
        return dataRepository.findAllBookCheckouts().stream().
                filter(c -> c.getMember().getId() == memberId).map(Optional::of)
                .toList();
    }
}
