package be.kdg.programming3.repository;

import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.repository.interfaces.CheckoutRepository;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
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
    public void save(BookCheckout bookCheckout) {
        dataRepository.findAllBookCheckouts().add(bookCheckout);
    }
}
