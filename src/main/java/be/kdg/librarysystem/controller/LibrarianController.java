package be.kdg.librarysystem.controller;

import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Librarian;
import be.kdg.librarysystem.dto.LibrarianDTO;
import be.kdg.librarysystem.mapper.LibrarianMapper;
import be.kdg.librarysystem.service.FilteringService;
import be.kdg.librarysystem.service.LibrarianService;
import be.kdg.librarysystem.viewModel.LibrarianViewModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Controller
@RequestMapping("/librarians")
public class LibrarianController {

    private static final Logger log =
            LoggerFactory.getLogger(LibrarianController.class);

    private final LibrarianService librarianService;
    private final FilteringService filteringService;

    public LibrarianController(
            LibrarianService librarianService,
            FilteringService filteringService
    ) {
        this.librarianService = librarianService;
        this.filteringService = filteringService;
    }

    @GetMapping
    public String listLibrarians(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false)
            List<String> fields,
            @RequestParam(required = false)
            String query,
            Model model
    ) {

        int pageSize = 12;

        List<Librarian> allLibrarians =
                librarianService.getAllLibrarians();

        int totalLibrarians =
                allLibrarians.size();

        int totalPages =
                (int) Math.ceil(
                        (double) totalLibrarians / pageSize
                );

        int fromIndex =
                Math.min(
                        (page - 1) * pageSize,
                        totalLibrarians
                );

        int toIndex =
                Math.min(
                        fromIndex + pageSize,
                        totalLibrarians
                );

        List<Librarian> pageLibrarians =
                allLibrarians.subList(fromIndex, toIndex);

        model.addAttribute(
                "librarians",
                pageLibrarians.stream()
                        .map(LibrarianMapper::toDTO)
                        .toList()
        );

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute(
                "totalLibrarians",
                totalLibrarians
        );

        model.addAttribute(
                "librarianFields",
                filteringService.preloadDomainFields(
                        Librarian.class
                )
        );

        model.addAttribute("query", query);

        model.addAttribute(
                "selectedFields",
                fields
        );

        log.debug("Listed all librarians");

        return "librarians";
    }

    @GetMapping("/filter")
    public String filterLibrarians(
            @RequestParam(defaultValue = "email")
            List<String> fields,

            @RequestParam String query,

            Model model
    ) {

        log.debug(
                "Filtering librarians by fields={} query={}",
                fields,
                query
        );

        if (query.isBlank()) {
            return "redirect:/librarians";
        }

        Predicate<Librarian> predicate =
                filteringService.buildMultiFieldPredicate(
                        Librarian.class,
                        fields,
                        query
                );

        List<Librarian> filtered =
                filteringService.filter(
                        librarianService.getAllLibrarians(),
                        predicate
                );

        model.addAttribute(
                "librarians",
                filtered.stream()
                        .map(LibrarianMapper::toDTO)
                        .toList()
        );

        model.addAttribute(
                "librarianFields",
                filteringService.preloadDomainFields(
                        Librarian.class
                )
        );

        model.addAttribute(
                "totalLibrarians",
                filtered.size()
        );

        model.addAttribute("currentPage", 1);

        model.addAttribute("totalPages", 1);

        model.addAttribute(
                "selectedFields",
                fields
        );

        model.addAttribute("query", query);

        return "librarians";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddLibrarianForm(
            Model model
    ) {

        model.addAttribute(
                "librarian",
                new LibrarianViewModel()
        );

        return "addLibrarian";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addLibrarian(
            @Valid
            @ModelAttribute("librarian")
            LibrarianViewModel librarianViewModel,

            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "addLibrarian";
        }

        librarianService.addLibrarian(
                LibrarianMapper.vmToEntity(
                        librarianViewModel
                )
        );

        return "redirect:/librarians";
    }

    @GetMapping("/{id}")
    public String showLibrarianDetail(
            @PathVariable int id,
            Model model
    ) {

        Librarian librarian =
                librarianService.getLibrarianById(id);

        if (librarian == null) {
            return "redirect:/librarians?error=notfound";
        }

        LibrarianDTO librarianDTO =
                LibrarianMapper.toDTO(librarian);

        List<BookCheckout> checkouts =
                librarianService.getCheckouts(id);

        model.addAttribute(
                "librarian",
                librarianDTO
        );

        model.addAttribute(
                "checkouts",
                checkouts
        );

        return "librarianDetail";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditLibrarianForm(
            @PathVariable int id,
            Model model
    ) {

        Librarian librarian =
                librarianService.getLibrarianById(id);

        if (librarian == null) {
            return "redirect:/librarians";
        }

        model.addAttribute(
                "librarian",
                LibrarianMapper.entityToVm(librarian)
        );

        return "editLibrarian";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editLibrarian(
            @PathVariable int id,

            @Valid
            @ModelAttribute("librarian")
            LibrarianViewModel librarianViewModel,

            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "editLibrarian";
        }

        librarianViewModel.setId(id);

        librarianService.update(
                LibrarianMapper.vmToEntity(
                        librarianViewModel
                )
        );

        return "redirect:/librarians";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteLibrarian(
            @PathVariable int id
    ) {

        librarianService.deleteLibrarian(id);

        return "redirect:/librarians";
    }
}