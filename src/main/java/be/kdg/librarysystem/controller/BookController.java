package be.kdg.librarysystem.controller;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.User;
import be.kdg.librarysystem.domain.enums.*;
import be.kdg.librarysystem.dto.BookDTO;
import be.kdg.librarysystem.exception.BookProcessingException;
import be.kdg.librarysystem.mapper.BookMapper;
import be.kdg.librarysystem.service.*;
import be.kdg.librarysystem.viewModel.BookViewModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final FilteringService filteringService;
    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final UserService userService;
    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "1") int page, Model model, @RequestParam(required = false) List<String> fields, @RequestParam(required = false) String query, HttpServletRequest request, HttpServletResponse response) {
        int pageSize = 12;
        List<Book> allBooks = bookService.getAllBooks();
        int totalBooks = allBooks.size();
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

        int fromIndex = Math.min((page - 1) * pageSize, totalBooks);
        int toIndex = Math.min(fromIndex + pageSize, totalBooks);

        List<Book> pageBooks = allBooks.subList(fromIndex, toIndex);

        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        model.addAttribute("books", pageBooks.stream().map(BookMapper::toDTO).toList());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", user != null ? memberService.findById(user.getId()).getProfileImageUrl() : null);
        model.addAttribute("bookFields", filteringService.preloadDomainFields(Book.class));
        model.addAttribute("query", query);
        model.addAttribute("selectedFields", fields);

        log.debug("Listed all books");
        return "books";
    }

    public BookController(BookService bookService, FilteringService filteringService, UserService userService, AuthService authService, MemberService memberService) {
        this.bookService = bookService;
        this.filteringService = filteringService;
        this.userService = userService;
        this.authService = authService;
        this.memberService = memberService;
    }

    @GetMapping("/filter")
    public String filterBooks(@RequestParam(defaultValue = "title") List<String> fields, @RequestParam String query, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Filtering books by field={} query={}", fields, query);
        if(query.isBlank()) return "redirect:/books";
        Predicate<Book> predicate = filteringService.buildMultiFieldPredicate(Book.class, fields, query);
        List<Book> filtered = filteringService.filter(bookService.getAllBooks(), predicate);

        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        model.addAttribute("books", filtered.stream().map(BookMapper::toDTO).toList());
        model.addAttribute("bookFields", filteringService.preloadDomainFields(Book.class));
        model.addAttribute("totalBooks", filtered.size());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", user != null ? memberService.findById(user.getId()).getProfileImageUrl() : null);
        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);

        return "books";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        model.addAttribute("book", new BookViewModel());
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", user != null ? memberService.findById(user.getId()).getProfileImageUrl() : null);
        return "addBook";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") BookViewModel bookViewModel, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "addBook";
        }

        bookService.addBook(BookMapper.vmToEntity(bookViewModel));

        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBookDetail(@PathVariable int id, Model model, HttpServletRequest request, HttpServletResponse response) {
        Book book = bookService.findById(id);

        if (book == null) {
            return "redirect:/books?error=notfound";
        }

        BookDTO bookDTO = BookMapper.toDTO(book);
        Optional<BookCheckout> checkoutDetail = bookService.getCheckoutDetailsObj(id);

        LocalDate expectedReturnDate = null;
        boolean isCheckedOut = false;

        int librarianId = -1;
        String librarianName = null;

        int memberId = -1;
        String memberName = null;

        if (checkoutDetail.isPresent()) {
            Boolean checkoutStatus = checkoutDetail.get().getStatus() != CheckoutStatus.RETURNED;
            expectedReturnDate = checkoutDetail.get().getReturnDate();

            librarianId = checkoutDetail.get().getHandledByLibrarian().getId();
            librarianName = checkoutDetail.get().getHandledByLibrarian().getFirstName() + " " + checkoutDetail.get().getHandledByLibrarian().getLastName();

            memberId = checkoutDetail.get().getMember().getId();
            memberName = checkoutDetail.get().getMember().getFirstName() + " " + checkoutDetail.get().getMember().getLastName();

            isCheckedOut = checkoutStatus;
            log.debug("Book [{}] checkout info - status: {}, return date: {}", id, checkoutStatus, expectedReturnDate);
        }

        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        model.addAttribute("book", bookDTO);
        model.addAttribute("isCheckedOut", isCheckedOut);
        model.addAttribute("librarianId", librarianId);
        model.addAttribute("memberId", memberId);
        model.addAttribute("librarianName", librarianName);
        model.addAttribute("memberName", memberName);
        model.addAttribute("expectedReturnDate", expectedReturnDate != null ? expectedReturnDate.toString() : "None");
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", user != null ? memberService.findById(user.getId()).getProfileImageUrl() : null);

        return "bookDetail";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable int id,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        Book book = bookService.findById(id);
        BookViewModel vm = BookMapper.entityToVm(book);

        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        model.addAttribute("book", vm);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", user != null ? memberService.findById(user.getId()).getProfileImageUrl() : null);

        return "editBooks";
    }

    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable int id,
                           @Valid @ModelAttribute("book") BookViewModel bookViewModel,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "editBooks";
        }

        bookViewModel.setId(id);

        bookService.updateBook(BookMapper.vmToEntity(bookViewModel));

        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(
            @PathVariable int id,
            HttpServletRequest request,
            HttpServletResponse response) {

        var currentUser = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        if (!currentUser.getFlags().name().equals("ADMIN")) {
            return "redirect:/accessDenied";
        }

        bookService.deleteBook(id);

        return "redirect:/books";
    }
}
