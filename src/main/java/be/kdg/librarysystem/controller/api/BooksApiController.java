package be.kdg.librarysystem.controller.api;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.dto.BookCheckoutDetailsDTO;
import be.kdg.librarysystem.dto.BookDTO;
import be.kdg.librarysystem.mapper.BookMapper;
import be.kdg.librarysystem.service.BookService;
import be.kdg.librarysystem.service.FilteringService;
import be.kdg.librarysystem.viewModel.BookViewModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/books")
public class BooksApiController {

    private final BookService bookService;
    private final FilteringService filteringService;

    public BooksApiController(BookService bookService, FilteringService filteringService) {
        this.bookService = bookService;
        this.filteringService = filteringService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable int id) {
        Book book = bookService.findById(id);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(BookMapper.toDTO(book));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks()
                .stream()
                .map(BookMapper::toDTO)
                .toList();

        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {

        Book book = bookService.findById(id);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<BookDTO>> filterBooks(@RequestParam(defaultValue = "title") List<String> fields, @RequestParam String query) {

        if (query.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Predicate<Book> predicate = filteringService.buildMultiFieldPredicate(Book.class, fields, query);

        List<BookDTO> filtered = filteringService.filter(bookService.getAllBooks(), predicate)
                .stream()
                .map(BookMapper::toDTO)
                .toList();

        return ResponseEntity.ok(filtered);
    }


    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody @Valid BookViewModel bookViewModel) {

        Book book = BookMapper.vmToEntity(bookViewModel);
        bookService.addBook(book);

        return ResponseEntity.status(HttpStatus.CREATED).body(BookMapper.toDTO(book));
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable int id, @RequestBody @Valid BookViewModel bookViewModel) {

        Book existing = bookService.findById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        bookViewModel.setId(id);
        Book updated = BookMapper.vmToEntity(bookViewModel);

        bookService.updateBook(updated);

        return ResponseEntity.ok(BookMapper.toDTO(updated));
    }

    @GetMapping("/{id}/checkout")
    public ResponseEntity<BookCheckoutDetailsDTO> getCheckoutDetails(
            @PathVariable int id) {

        Book book = bookService.findById(id);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<BookCheckoutDetailsDTO> checkoutOpt =
                bookService.getCheckoutDetails(id);

        return checkoutOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}