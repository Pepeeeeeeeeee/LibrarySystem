package be.kdg.librarysystem.controller.api;

import be.kdg.librarysystem.domain.Librarian;
import be.kdg.librarysystem.dto.LibrarianCheckoutDTO;
import be.kdg.librarysystem.dto.LibrarianDTO;
import be.kdg.librarysystem.mapper.LibrarianMapper;
import be.kdg.librarysystem.service.FilteringService;
import be.kdg.librarysystem.service.LibrarianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/librarians")
public class LibrariansApiController {

    private final LibrarianService librarianService;
    private final FilteringService filteringService;

    public LibrariansApiController(
            LibrarianService librarianService,
            FilteringService filteringService
    ) {
        this.librarianService = librarianService;
        this.filteringService = filteringService;
    }

    @GetMapping
    public ResponseEntity<List<LibrarianDTO>> getAllLibrarians() {
        List<LibrarianDTO> librarians =
                librarianService.getAllLibrarians()
                        .stream()
                        .map(LibrarianMapper::toDTO)
                        .toList();
        return ResponseEntity.ok(librarians);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<LibrarianDTO>> filterLibrarians(
            @RequestParam(defaultValue = "id")
            List<String> fields,

            @RequestParam String query
    ) {
        Predicate<Librarian> predicate =
                filteringService.buildMultiFieldPredicate(
                        Librarian.class,
                        fields,
                        query
                );

        List<LibrarianDTO> filtered =
                filteringService.filter(
                                librarianService.getAllLibrarians(),
                                predicate
                        )
                        .stream()
                        .map(LibrarianMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibrarianDTO> getLibrarianById(@PathVariable int id) {
        Librarian librarian = librarianService.getLibrarianById(id);
        if (librarian == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(LibrarianMapper.toDTO(librarian));
    }

    @PostMapping
    public ResponseEntity<LibrarianDTO> addLibrarian(@RequestBody LibrarianDTO librarianDTO) {
        Librarian librarian = LibrarianMapper.toEntity(librarianDTO);
        Librarian savedLibrarian = librarianService.addLibrarian(librarian);
        return new ResponseEntity<>(LibrarianMapper.toDTO(savedLibrarian), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibrarianDTO> updateLibrarian(@PathVariable int id, @RequestBody LibrarianDTO librarianDTO) {
        Librarian existingLibrarian = librarianService.getLibrarianById(id);
        if (existingLibrarian == null) {
            return ResponseEntity.notFound().build();
        }
        Librarian updatedLibrarian = LibrarianMapper.toEntity(librarianDTO);
        updatedLibrarian.setId(id);
        Librarian savedLibrarian = librarianService.update(updatedLibrarian);
        return ResponseEntity.ok(LibrarianMapper.toDTO(savedLibrarian));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrarian(@PathVariable int id) {
        Librarian existingLibrarian = librarianService.getLibrarianById(id);
        if (existingLibrarian == null) {
            return ResponseEntity.notFound().build();
        }
        librarianService.deleteLibrarian(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/checkouts")
    public ResponseEntity<List<LibrarianCheckoutDTO>> getLibrarianCheckoutsById(
            @PathVariable int id
    ) {
        Librarian librarian = librarianService.getLibrarianById(id);

        if (librarian == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                librarianService.findCheckoutDtosByLibrarianId(librarian.getId())
        );
    }
}