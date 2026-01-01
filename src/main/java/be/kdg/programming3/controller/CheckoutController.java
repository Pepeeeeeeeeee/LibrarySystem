package be.kdg.programming3.controller;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.User;
import be.kdg.programming3.dto.CheckoutDTO;
import be.kdg.programming3.mapper.CheckoutMapper;
import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final FilteringService filteringService;
    private final UserService userService;
    private final AuthService authService;
    private final MemberService memberService;
    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    public CheckoutController(CheckoutService checkoutService,
                              FilteringService filteringService,
                              UserService userService,
                              AuthService authService,
                              MemberService memberService) {
        this.checkoutService = checkoutService;
        this.filteringService = filteringService;
        this.userService = userService;
        this.authService = authService;
        this.memberService = memberService;
    }

    @GetMapping
    public String listCheckouts(Model model, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());
        model.addAttribute("checkouts",
                checkoutService.getAllBookCheckouts().stream()
                        .map(CheckoutMapper::toDTO)
                        .toList());
        log.debug("Listed all checkouts");
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", user != null ? memberService.findById(user.getId()).getProfileImageUrl() : null);
        model.addAttribute("checkoutFields", filteringService.preloadDomainFields(BookCheckout.class));
        model.addAttribute("selectedFields", null);
        model.addAttribute("query", "");

        return "checkoutsTable";
    }

    @GetMapping("/filter")
    public String filterCheckouts(@RequestParam(defaultValue = "id") List<String> fields, @RequestParam String query, Model model,
                                  HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        log.debug("Filtering checkouts with field={} value={}", fields, query);
        Predicate<BookCheckout> predicate = filteringService.buildMultiFieldPredicate(BookCheckout.class, fields, query);
        List<BookCheckout> filtered = filteringService.filter(checkoutService.getAllBookCheckouts(), predicate);
        log.debug(
                "Filtered {} checkouts: {}",
                filtered.size(),
                filtered.stream().map(BookCheckout::getId).toList()
        );
        model.addAttribute("checkouts", filtered);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", user != null ? memberService.findById(user.getId()).getProfileImageUrl() : null);
        model.addAttribute("checkoutFields", filteringService.preloadDomainFields(BookCheckout.class));
        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);
        return "checkoutsTable";
    }

    @GetMapping("/add")
    public String showAddCheckoutForm(Model model) {
        model.addAttribute("checkout", new CheckoutDTO());
        return "addCheckout";
    }

    @PostMapping("/add")
    public String addCheckout(@ModelAttribute("checkout") CheckoutDTO checkoutDTO) {
        checkoutService.addCheckout(CheckoutMapper.toEntity(checkoutDTO));
        return "redirect:/checkouts";
    }
}
