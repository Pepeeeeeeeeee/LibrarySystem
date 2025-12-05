package be.kdg.programming3.controller;

import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.domain.User;
import be.kdg.programming3.dto.LibrarianDTO;
import be.kdg.programming3.mapper.LibrarianMapper;
import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.service.*;
import be.kdg.programming3.viewModel.LibrarianViewModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Controller
@RequestMapping("/librarians")
public class LibrarianController {
    private final LibrarianService librarianService;
    private final FilteringService filteringService;
    private final MemberService memberService;
    private static final Logger log = LoggerFactory.getLogger(LibrarianController.class);
    private final UserService userService;
    private final AuthService authService;

    public LibrarianController(LibrarianService librarianService, FilteringService filteringService, MemberService memberService, UserService userService, AuthService authService) {
        this.librarianService = librarianService;
        this.filteringService = filteringService;
        this.memberService = memberService;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public String listLibrarians(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) List<String> fields,
                                 @RequestParam(required = false) String query, Model model, HttpServletRequest request, HttpServletResponse response) {

        int pageSize = 12;
        List<Librarian> allLibrarians = librarianService.getAllLibrarians();
        int totalLibrarians = allLibrarians.size();
        int totalPages = (int) Math.ceil((double) totalLibrarians / pageSize);

        int fromIndex = Math.min((page - 1) * pageSize, totalLibrarians);
        int toIndex = Math.min(fromIndex + pageSize, totalLibrarians);

        List<Librarian> pageLibrarians = allLibrarians.subList(fromIndex, toIndex);

        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());
        
        model.addAttribute("librarians", pageLibrarians.stream().map(LibrarianMapper::toDTO).toList());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalLibrarians", totalLibrarians);
        model.addAttribute("librarianFields", filteringService.preloadDomainFields(Librarian.class));
        model.addAttribute("query", query);
        model.addAttribute("selectedFields", fields);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", memberService.findById(user.getId()).getProfileImageUrl());


        log.debug("Listed all librarians");
        return "librarians";
    }

    @GetMapping("/filter")
    public String filterLibrarians(@RequestParam(defaultValue = "email") List<String> fields,
                                   @RequestParam String query,
                                   Model model,
                                   HttpServletRequest request, HttpServletResponse response) {
        log.debug("Filtering librarians by fields={} query={}", fields, query);
        if (query.isBlank()) return "redirect:/librarians";

        Predicate<Librarian> predicate = filteringService.buildMultiFieldPredicate(Librarian.class, fields, query);
        List<Librarian> filtered = filteringService.filter(librarianService.getAllLibrarians(), predicate);

        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        model.addAttribute("librarians", filtered.stream().map(LibrarianMapper::toDTO).toList());
        model.addAttribute("librarianFields", filteringService.preloadDomainFields(Librarian.class));
        model.addAttribute("totalLibrarians", filtered.size());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", memberService.findById(user.getId()).getProfileImageUrl());
        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);

        return "librarians";
    }


    @GetMapping("/add")
    public String showAddLibrarianForm(Model model, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());
        
        model.addAttribute("librarian", new LibrarianViewModel());
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", memberService.findById(user.getId()).getProfileImageUrl());

        return "addLibrarian";
    }

    @PostMapping("/add")
    public String addLibrarian(
            @Valid @ModelAttribute("librarian") LibrarianViewModel librarianViewModel,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "addLibrarian";
        }

        librarianService.addLibrarian(LibrarianMapper.vmToEntity(librarianViewModel));

        return "redirect:/librarians";
    }

    @GetMapping("/{id}")
    public String showLibrarianDetail(@PathVariable int id, Model model, HttpServletRequest request, HttpServletResponse response) {
        Librarian librarian = librarianService.getLibrarianById(id);

        if(librarian == null) {
            return "redirect:/librarians?error=notfound";
        }

        LibrarianDTO librarianDTO = LibrarianMapper.toDTO(librarian);

        List<Optional<BookCheckout>> checkouts = librarianService.getCheckOuts(id);

        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());
        
        model.addAttribute("librarian", librarianDTO);
        model.addAttribute("checkouts", checkouts);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", memberService.findById(user.getId()).getProfileImageUrl());


        return "librarianDetail";
    }

    @GetMapping("/edit/{id}")
    public String showEditLibrarianForm(@PathVariable int id,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        Librarian librarian = librarianService.getLibrarianById(id);
        LibrarianViewModel vm = LibrarianMapper.entityToVm(librarian);

        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());
        
        model.addAttribute("librarian", vm);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", memberService.findById(user.getId()).getProfileImageUrl());


        return "editLibrarian";
    }

    @PostMapping("/edit/{id}")
    public String editLibrarian(@PathVariable int id,
                           @Valid @ModelAttribute("book") LibrarianViewModel librarianViewModel,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "editLibrarian";
        }

        librarianViewModel.setId(id);

        librarianService.update(LibrarianMapper.vmToEntity(librarianViewModel));

        return "redirect:/librarian";
    }

    @GetMapping("/delete/{id}")
    public String deleteLibrarian(
            @PathVariable int id,
            HttpServletRequest request,
            HttpServletResponse response) {

        var currentUser = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        if (!currentUser.getFlags().name().equals("ADMIN")) {
            return "redirect:/accessDenied";
        }

        librarianService.deleteLibrarian(id);

        return "redirect:/librarians";
    }
}
