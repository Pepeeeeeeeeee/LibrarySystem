package be.kdg.librarysystem.controller;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.dto.BookCheckoutDetailsDTO;
import be.kdg.librarysystem.dto.BookDTO;
import be.kdg.librarysystem.mapper.BookMapper;
import be.kdg.librarysystem.service.BookService;
import be.kdg.librarysystem.service.FilteringService;
import be.kdg.librarysystem.viewModel.BookViewModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final FilteringService filteringService;

    public BookController(BookService bookService, FilteringService filteringService) {
        this.bookService = bookService;
        this.filteringService = filteringService;
    }

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) List<String> fields,
            @RequestParam(required = false) String query,
            Model model) {

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
        model.addAttribute("bookFields", filteringService.preloadDomainFields(Book.class));
        model.addAttribute("query", query);
        model.addAttribute("selectedFields", fields);

        log.debug("Listed all books");

        return "books";
    }

    @GetMapping("/filter")
    public String filterBooks(
            @RequestParam(defaultValue = "title") List<String> fields,
            @RequestParam String query,
            Model model) {

        log.debug("Filtering books by field={} query={}", fields, query);

        if (query.isBlank()) {
            return "redirect:/books";
        }

        Predicate<Book> predicate = filteringService.buildMultiFieldPredicate(Book.class, fields, query);
        List<Book> filtered = filteringService.filter(bookService.getAllBooks(), predicate);

        model.addAttribute("books", filtered.stream().map(BookMapper::toDTO).toList());
        model.addAttribute("bookFields", filteringService.preloadDomainFields(Book.class));
        model.addAttribute("totalBooks", filtered.size());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);

        return "books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddBookForm(Model model) {

        model.addAttribute("book", new BookViewModel());

        return "addBook";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addBook(
            @Valid @ModelAttribute("book") BookViewModel bookViewModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "addBook";
        }

        bookService.addBook(BookMapper.vmToEntity(bookViewModel));

        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBookDetail(@PathVariable int id, Model model) {

        Book book = bookService.findById(id);

        if (book == null) {
            return "redirect:/books?error=notfound";
        }

        BookDTO bookDTO = BookMapper.toDTO(book);

        Optional<BookCheckoutDetailsDTO> checkoutDetail =
                bookService.getCheckoutDetails(id);

        model.addAttribute("book", bookDTO);

        if (checkoutDetail.isPresent()) {

            BookCheckoutDetailsDTO dto = checkoutDetail.get();

            model.addAttribute("isCheckedOut", dto.isCheckedOut());
            model.addAttribute("librarianId", dto.getLibrarianId());
            model.addAttribute("memberId", dto.getMemberId());
            model.addAttribute("librarianName", dto.getLibrarianName());
            model.addAttribute("memberName", dto.getMemberName());
            model.addAttribute("expectedReturnDate",
                    dto.getExpectedReturnDate());

            log.debug("Book [{}] checkout info loaded", id);

        } else {

            model.addAttribute("isCheckedOut", false);
            model.addAttribute("librarianId", -1);
            model.addAttribute("memberId", -1);
            model.addAttribute("librarianName", null);
            model.addAttribute("memberName", null);
            model.addAttribute("expectedReturnDate", "None");
        }

        return "bookDetail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable int id, Model model) {

        Book book = bookService.findById(id);

        if (book == null) {
            return "redirect:/books";
        }

        model.addAttribute("book", BookMapper.entityToVm(book));

        return "editBooks";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editBook(
            @PathVariable int id,
            @Valid @ModelAttribute("book") BookViewModel bookViewModel,
           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "editBooks";
        }

        bookViewModel.setId(id);

        bookService.updateBook(BookMapper.vmToEntity(bookViewModel));

        return "redirect:/books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);

        return "redirect:/books";
    }
}