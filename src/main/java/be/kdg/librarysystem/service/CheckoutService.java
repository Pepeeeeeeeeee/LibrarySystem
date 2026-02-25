package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.repository.interfaces.CheckoutRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CheckoutService {
    private final CheckoutRepository repo;

    public CheckoutService(CheckoutRepository repo) {
        this.repo = repo;
    }

    public List<BookCheckout> getAllBookCheckouts() {
        return repo.findAll();
    }

    public void addCheckout(BookCheckout checkout) {
        repo.save(checkout);
    }
}
