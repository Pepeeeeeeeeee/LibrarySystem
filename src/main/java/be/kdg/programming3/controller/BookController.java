package be.kdg.programming3.controller;

import be.kdg.programming3.dto.BookDTO;
import be.kdg.programming3.mapper.BookMapper;
import be.kdg.programming3.service.MemberService;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.enums.*;
import be.kdg.programming3.service.BookService;
import be.kdg.programming3.service.FilteringService;
import be.kdg.programming3.viewModel.BookViewModel;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final FilteringService filteringService;
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    //Test user
    private MemberService memberService;

    public BookController(BookService bookService, FilteringService filteringService, MemberService memberService) {
        this.bookService = bookService;
        this.filteringService = filteringService;
        this.memberService = memberService;
    }

    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "1") int page, Model model, @RequestParam(required = false) List<String> fields, @RequestParam(required = false) String query) {
        int pageSize = 12;
        List<Book> allBooks = bookService.getAllBooks();
        int totalBooks = allBooks.size();
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

        int fromIndex = Math.min((page - 1) * pageSize, totalBooks);
        int toIndex = Math.min(fromIndex + pageSize, totalBooks);

        List<Book> pageBooks = allBooks.subList(fromIndex, toIndex);

        model.addAttribute("books", pageBooks.stream().map(BookMapper::toDTO).toList());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("currentUser", memberService.getAllMembers().getFirst());
        model.addAttribute("userPhotoUrl", "/images/default-profile.png");
        model.addAttribute("bookFields", filteringService.preloadDomainFields(Book.class));
        model.addAttribute("query", query);
        model.addAttribute("selectedFields", fields);

        log.debug("Listed all books");
        return "books";
    }

    @GetMapping("/filter")
    public String filterBooks(@RequestParam(defaultValue = "title") List<String> fields, @RequestParam String query, Model model) {
        log.debug("Filtering books by field={} query={}", fields, query);
        if(query.isBlank()) return "redirect:/books";
        Predicate<Book> predicate = filteringService.buildMultiFieldPredicate(Book.class, fields, query);
        List<Book> filtered = filteringService.filter(bookService.getAllBooks(), predicate);

        model.addAttribute("books", filtered.stream().map(BookMapper::toDTO).toList());
        model.addAttribute("bookFields", filteringService.preloadDomainFields(Book.class));
        model.addAttribute("totalBooks", filtered.size());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("currentUser", memberService.getAllMembers().getFirst());
        model.addAttribute("userPhotoUrl", "/images/default-profile.png");
        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);


        return "books";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new BookViewModel());
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
    public String showBookDetail(@PathVariable Long id, Model model) {
        Optional<BookDTO> bookOpt = bookService.getAllBooks().stream()
                .filter(b -> b.getId() == id)
                .map(BookMapper::toDTO)
                .findFirst();

        if (bookOpt.isEmpty()) {
            return "redirect:/books?error=notfound";
        }

        BookDTO book = bookOpt.get();
        Map.Entry<LocalDate, Boolean> checkoutDetail = bookService.getCheckoutDetails(id);

        LocalDate expectedReturnDate = null;
        boolean isCheckedOut = false;

        if (checkoutDetail != null) {
            Boolean checkoutStatus = checkoutDetail.getValue();
            expectedReturnDate = checkoutDetail.getKey();

            isCheckedOut = (checkoutStatus == null || checkoutStatus);
            log.debug("Book [{}] checkout info - status: {}, return date: {}", id, checkoutStatus, expectedReturnDate);
        }

        model.addAttribute("book", book);
        model.addAttribute("isCheckedOut", isCheckedOut);
        model.addAttribute("expectedReturnDate", expectedReturnDate != null ? expectedReturnDate.toString() : "None");

        return "bookDetail";
    }

}
