package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.BookCheckout;
import java.util.List;

public interface CheckoutRepository {
    List<BookCheckout> findAll();
    void save(BookCheckout bookCheckout);
}
