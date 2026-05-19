package be.kdg.librarysystem.controller;

import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.dto.CheckoutDTO;
import be.kdg.librarysystem.mapper.CheckoutMapper;
import be.kdg.librarysystem.service.CheckoutService;
import be.kdg.librarysystem.service.FilteringService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;

@Controller
@RequestMapping("/checkouts")
public class CheckoutController {

    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    private final CheckoutService checkoutService;
    private final FilteringService filteringService;

    public CheckoutController(CheckoutService checkoutService, FilteringService filteringService) {
        this.checkoutService = checkoutService;
        this.filteringService = filteringService;
    }

    @GetMapping
    public String listCheckouts(Model model) {
        model.addAttribute("checkouts", checkoutService.getAllBookCheckouts().stream().map(CheckoutMapper::toDTO).toList());
        model.addAttribute("checkoutFields", filteringService.preloadDomainFields(BookCheckout.class));
        model.addAttribute("selectedFields", null);
        model.addAttribute("query", "");

        log.debug("Listed all checkouts");

        return "checkoutsTable";
    }

    @GetMapping("/filter")
    public String filterCheckouts(
            @RequestParam(defaultValue = "id") List<String> fields,
            @RequestParam String query,
            Model model) {

        log.debug("Filtering checkouts with field={} value={}", fields, query);

        Predicate<BookCheckout> predicate = filteringService.buildMultiFieldPredicate(BookCheckout.class, fields, query);
        List<BookCheckout> filtered = filteringService.filter(checkoutService.getAllBookCheckouts(), predicate);

        log.debug("Filtered {} checkouts", filtered.size());

        model.addAttribute("checkouts", filtered.stream().map(CheckoutMapper::toDTO).toList());
        model.addAttribute("checkoutFields", filteringService.preloadDomainFields(BookCheckout.class));
        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);

        return "checkoutsTable";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddCheckoutForm(Model model) {
        model.addAttribute("checkout", new CheckoutDTO());

        return "addCheckout";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addCheckout(@Valid @ModelAttribute("checkout") CheckoutDTO checkoutDTO) {
        checkoutService.addCheckout(CheckoutMapper.toEntity(checkoutDTO));

        return "redirect:/checkouts";
    }
}