package be.kdg.librarysystem.controller;

import be.kdg.librarysystem.domain.*;
import be.kdg.librarysystem.dto.*;
import be.kdg.librarysystem.mapper.*;
import be.kdg.librarysystem.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TablesController {
    private static final Logger log = LoggerFactory.getLogger(TablesController.class);
    private final BookService bookService;
    private final MemberService memberService;
    private final LibrarianService librarianService;
    private final CheckoutService checkoutService;
    private final FilteringService filteringService;

    public TablesController(BookService bookService,
                                 MemberService memberService,
                                 LibrarianService librarianService,
                                 CheckoutService checkoutService,
                                 FilteringService filteringService) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.librarianService = librarianService;
        this.checkoutService = checkoutService;
        this.filteringService = filteringService;
    }

    @GetMapping("/tables/books")
    public String listAllBooks(Model model) {
        List<BookDTO> books = bookService.getAllBooks().stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());

        model.addAttribute("books", books);
        model.addAttribute("bookFields", filteringService.preloadDomainFields(Book.class));
        model.addAttribute("selectedFields", null);
        model.addAttribute("query", "");
        return "booksTable";
    }

    @GetMapping("/tables/members")
    public String listAllMembers(Model model) {
        List<MemberDTO> members = memberService.getAllMembers().stream()
                .map(MemberMapper::toDTO)
                .collect(Collectors.toList());

        model.addAttribute("members", members);
        model.addAttribute("memberFields", filteringService.preloadDomainFields(Member.class));
        model.addAttribute("selectedFields", null);
        model.addAttribute("query", "");
        return "membersTable";
    }

    @GetMapping("/tables/librarians")
    public String listAllLibrarians(Model model) {
        List<LibrarianDTO> librarians = librarianService.getAllLibrarians().stream()
                .map(LibrarianMapper::toDTO)
                .collect(Collectors.toList());

        model.addAttribute("librarians", librarians);
        model.addAttribute("librarianFields", filteringService.preloadDomainFields(Librarian.class));
        model.addAttribute("selectedFields", null);
        model.addAttribute("query", "");
        return "librariansTable";
    }

    @GetMapping("/tables/checkouts")
    public String listAllCheckouts(Model model) {
        List<CheckoutDTO> checkouts = checkoutService.getAllBookCheckouts().stream().map(CheckoutMapper::toDTO).collect(Collectors.toList());

        model.addAttribute("checkouts", checkouts);
        model.addAttribute("checkoutFields", List.of("id","bookTitle","memberEmail","checkoutDate","dueDate","returnDate","renewed","status","handledByLibrarianEmail"));
        model.addAttribute("selectedFields", null);
        model.addAttribute("query", "");
        return "checkoutsTable";
    }
}
