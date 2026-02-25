package be.kdg.librarysystem.viewModel;

import be.kdg.librarysystem.domain.enums.CheckoutStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class BookCheckoutViewModel {
    private int id;

    @NotNull(message = "{validation.checkout.bookId.notNull}")
    private Integer bookId;

    @NotNull(message = "{validation.checkout.memberId.notNull}")
    private Integer memberId;

    @NotNull(message = "{validation.checkout.checkoutDate.notNull}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkoutDate;

    @NotNull(message = "{validation.checkout.dueDate.notNull}")
    @FutureOrPresent(message = "{validation.checkout.dueDate.futureOrPresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    private boolean renewed;

    @NotNull(message = "{validation.checkout.status.notNull}")
    private CheckoutStatus status;

    @NotNull(message = "{validation.checkout.handledById.notNull}")
    private Integer handledById;
}
