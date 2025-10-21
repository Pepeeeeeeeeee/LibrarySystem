package be.kdg.programming3.controller;

import be.kdg.programming3.dto.LibrarianDTO;
import be.kdg.programming3.mapper.LibrarianMapper;
import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.service.LibrarianService;
import be.kdg.programming3.service.EntityFactoryService;
import be.kdg.programming3.service.FilteringService;
import be.kdg.programming3.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.function.Predicate;

@Controller
@RequestMapping("/librarians")
public class LibrarianController {
    private final LibrarianService librarianService;
    private final FilteringService filteringService;
    private final MemberService memberService;
    private static final Logger log = LoggerFactory.getLogger(LibrarianController.class);

    public LibrarianController(LibrarianService librarianService, FilteringService filteringService, MemberService memberService) {
        this.librarianService = librarianService;
        this.filteringService = filteringService;
        this.memberService = memberService;
    }

    @GetMapping
    public String listLibrarians(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) List<String> fields, @RequestParam(required = false) String query, Model model) {

        int pageSize = 12;
        List<Librarian> allLibrarians = librarianService.getAllLibrarians();
        int totalLibrarians = allLibrarians.size();
        int totalPages = (int) Math.ceil((double) totalLibrarians / pageSize);

        int fromIndex = Math.min((page - 1) * pageSize, totalLibrarians);
        int toIndex = Math.min(fromIndex + pageSize, totalLibrarians);

        List<Librarian> pageLibrarians = allLibrarians.subList(fromIndex, toIndex);

        model.addAttribute("librarians", pageLibrarians.stream().map(LibrarianMapper::toDTO).toList());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalLibrarians", totalLibrarians);
        model.addAttribute("librarianFields", filteringService.preloadDomainFields(Librarian.class));
        model.addAttribute("query", query);
        model.addAttribute("selectedFields", fields);
        model.addAttribute("currentUser", memberService.getAllMembers().getFirst());
        model.addAttribute("userPhotoUrl", "/images/default-profile.png");

        log.debug("Listed all librarians");
        return "librarians";
    }

    @GetMapping("/filter")
    public String filterLibrarians(@RequestParam(defaultValue = "email") List<String> fields,
                                   @RequestParam String query,
                                   Model model) {
        log.debug("Filtering librarians by fields={} query={}", fields, query);
        if (query.isBlank()) return "redirect:/librarians";

        Predicate<Librarian> predicate = filteringService.buildMultiFieldPredicate(Librarian.class, fields, query);
        List<Librarian> filtered = filteringService.filter(librarianService.getAllLibrarians(), predicate);

        model.addAttribute("librarians", filtered.stream().map(LibrarianMapper::toDTO).toList());
        model.addAttribute("librarianFields", filteringService.preloadDomainFields(Librarian.class));
        model.addAttribute("totalLibrarians", filtered.size());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("currentUser", memberService.getAllMembers().getFirst());
        model.addAttribute("userPhotoUrl", "/images/default-profile.png");
        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);

        return "librarians";
    }

    @GetMapping("/add")
    public String showAddLibrarianForm(Model model) {
        model.addAttribute("librarian", new LibrarianDTO());
        return "addLibrarian";
    }

    @PostMapping("/add")
    public String addLibrarian(@ModelAttribute("librarian") LibrarianDTO librarianDTO) {
        librarianService.addLibrarian(LibrarianMapper.toEntity(librarianDTO));
        return "redirect:/librarians";
    }
}
