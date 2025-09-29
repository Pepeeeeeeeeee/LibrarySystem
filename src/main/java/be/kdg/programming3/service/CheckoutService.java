package be.kdg.programming3.service;

import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CheckoutService {
    private final DataRepository repo;

    public CheckoutService(DataRepository repo) {
        this.repo = repo;
    }

    public List<BookCheckout> getAllBookCheckouts() {
        return repo.findAllBookCheckouts();
    }

    public void addCheckout(BookCheckout checkout) {
        repo.findAllBookCheckouts().add(checkout);
    }
}
