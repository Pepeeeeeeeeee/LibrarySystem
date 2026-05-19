package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.repository.BookCheckoutJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CheckoutService {

    private final BookCheckoutJpaRepository repository;

    public CheckoutService(
            BookCheckoutJpaRepository repository
    ) {
        this.repository = repository;
    }

    public List<BookCheckout> getAllBookCheckouts() {
        return repository.findAll();
    }

    public BookCheckout addCheckout(BookCheckout checkout) {
        return repository.save(checkout);
    }
}