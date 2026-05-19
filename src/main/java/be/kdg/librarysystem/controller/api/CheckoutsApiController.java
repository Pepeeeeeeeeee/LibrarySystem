package be.kdg.librarysystem.controller.api;

import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.dto.CheckoutDTO;
import be.kdg.librarysystem.mapper.CheckoutMapper;
import be.kdg.librarysystem.service.CheckoutService;
import be.kdg.librarysystem.service.FilteringService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/checkouts")
public class CheckoutsApiController {

    private final CheckoutService checkoutService;
    private final FilteringService filteringService;

    public CheckoutsApiController(
            CheckoutService checkoutService,
            FilteringService filteringService
    ) {
        this.checkoutService = checkoutService;
        this.filteringService = filteringService;
    }

    @GetMapping
    public ResponseEntity<List<CheckoutDTO>> getAllCheckouts() {

        List<CheckoutDTO> checkouts =
                checkoutService.getAllBookCheckouts()
                        .stream()
                        .map(CheckoutMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(checkouts);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CheckoutDTO>> filterCheckouts(
            @RequestParam(defaultValue = "id")
            List<String> fields,

            @RequestParam String query
    ) {

        Predicate<BookCheckout> predicate =
                filteringService.buildMultiFieldPredicate(
                        BookCheckout.class,
                        fields,
                        query
                );

        List<CheckoutDTO> filtered =
                filteringService.filter(
                                checkoutService.getAllBookCheckouts(),
                                predicate
                        )
                        .stream()
                        .map(CheckoutMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(filtered);
    }
}