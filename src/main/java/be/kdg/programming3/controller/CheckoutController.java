package be.kdg.programming3.controller;

import be.kdg.programming3.dto.CheckoutDTO;
import be.kdg.programming3.mapper.CheckoutMapper;
import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.service.CheckoutService;
import be.kdg.programming3.service.EntityFactoryService;
import be.kdg.programming3.service.FilteringService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.function.Predicate;

@Controller
@RequestMapping("/checkouts")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final EntityFactoryService entityFactoryService;
    private final FilteringService filteringService;
    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    public CheckoutController(CheckoutService checkoutService,
                              EntityFactoryService entityFactoryService,
                              FilteringService filteringService) {
        this.checkoutService = checkoutService;
        this.entityFactoryService = entityFactoryService;
        this.filteringService = filteringService;
    }

    @GetMapping
    public String listCheckouts(Model model) {
        model.addAttribute("checkouts",
                checkoutService.getAllBookCheckouts().stream()
                        .map(CheckoutMapper::toDTO)
                        .toList());
        log.debug("Listed all checkouts");
        return "checkouts";
    }

    @GetMapping("/filter")
    public String filterCheckouts(@RequestParam String field,
                                  @RequestParam String operator,
                                  @RequestParam String value,
                                  Model model) {
        log.debug("Filtering checkouts with field={} operator={} value={}", field, operator, value);
        Predicate<BookCheckout> predicate = filteringService.buildPredicate(BookCheckout.class, field, operator, value);
        List<BookCheckout> filtered = filteringService.filter(checkoutService.getAllBookCheckouts(), predicate);
        model.addAttribute("checkouts", filtered);
        return "checkouts";
    }

    @GetMapping("/add")
    public String showAddCheckoutForm(Model model) {
        model.addAttribute("checkout", new CheckoutDTO());
        return "add-checkout";
    }

    @PostMapping("/add")
    public String addCheckout(@ModelAttribute("checkout") CheckoutDTO checkoutDTO) {
        checkoutService.addCheckout(CheckoutMapper.toEntity(checkoutDTO));
        return "redirect:/checkouts";
    }
}
