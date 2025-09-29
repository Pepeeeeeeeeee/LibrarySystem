package be.kdg.programming3.presenter;

import be.kdg.programming3.domain.*;
import be.kdg.programming3.domain.enums.*;
import be.kdg.programming3.service.*;
import be.kdg.programming3.view.ViewContract;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

@Component
public class LibraryPresenter {
    private final BookService bookService;
    private final MemberService memberService;
    private final LibrarianService librarianService;
    private final CheckoutService checkoutService;
    private final EntityFactoryService entityFactoryService;
    private final FilteringService filteringService;
    private final ViewContract view;

    private List<MenuAction> mainMenu;
    private List<MenuAction> dataDisplayMenu;
    private List<MenuAction> dataManagementMenu;

    public LibraryPresenter(BookService bookService, MemberService memberService,
                            LibrarianService librarianService, CheckoutService checkoutService,
                            EntityFactoryService entityFactoryService, FilteringService filteringService,
                            ViewContract view) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.librarianService = librarianService;
        this.checkoutService = checkoutService;
        this.entityFactoryService = entityFactoryService;
        this.filteringService = filteringService;
        this.view = view;
        buildMenus();
    }

    private void buildMenus() {
        mainMenu = List.of(
                new MenuAction("1", "Select data for display", () -> showMenu("Data Display Menu", dataDisplayMenu)),
                new MenuAction("2", "Data management menu", () -> showMenu("Data Management Menu", dataManagementMenu)),
                new MenuAction("0", "Exit", () -> System.exit(0)));

        dataDisplayMenu = List.of(
                new MenuAction("1", "List all books", () -> bookService.getAllBooks().forEach(view::display)),
                new MenuAction("2", "List all members", () -> memberService.getAllMembers().forEach(view::display)),
                new MenuAction("3", "List all librarians", () -> librarianService.getAllLibrarians().forEach(view::display)),
                new MenuAction("4", "List all checkouts", () -> checkoutService.getAllBookCheckouts().forEach(view::display)),
                new MenuAction("5", "Back to main menu", () -> showMenu("Main Menu", mainMenu)),
                new MenuAction("0", "Exit", () -> System.exit(0)));

        dataManagementMenu = List.of(
                new MenuAction("1", "Book Management", this::bookMenu),
                new MenuAction("2", "Member Management", this::memberMenu),
                new MenuAction("3", "Librarian Management", this::librarianMenu),
                new MenuAction("4", "Checkout Management", this::checkoutMenu),
                new MenuAction("0", "Exit", () -> System.exit(0)));
    }

    public void start() {
        while (true) showMenu("Main Menu", mainMenu);
    }

    private void showMenu(String title, List<MenuAction> menu) {
        view.showMenu(title, menu.stream().map(m -> m.getKey() + ". " + m.getDescription()).toArray(String[]::new));
        String choice = view.prompt("Choose option: ");
        menu.stream()
                .filter(m -> m.getKey().equals(choice))
                .findFirst()
                .ifPresent(MenuAction::execute);
    }

    private void bookMenu() {
        List<MenuAction> bookMenu = List.of(
                new MenuAction("1", "Add a book", this::addBook),
                new MenuAction("2", "Advanced filter on books", () -> filterEntities(Book.class, bookService.getAllBooks())),
                new MenuAction("3", "Back", () -> {})
        );
        showMenu("Book Management", bookMenu);
    }

    private void addBook() {
        String isbn = view.prompt("Enter ISBN: ");
        String title = view.prompt("Enter title: ");
        String author = view.prompt("Enter author: ");
        LocalDate publicationDate = LocalDate.parse(view.prompt("Enter publication date (yyyy-MM-dd): "));
        int pages = Integer.parseInt(view.prompt("Enter number of pages: "));
        double price = Double.parseDouble(view.prompt("Enter price: "));
        Genre genre = Genre.valueOf(view.prompt("Enter genre: ").toUpperCase());
        boolean isAvailable = Boolean.parseBoolean(view.prompt("Is available (true/false): "));
        String coverImageUrl = view.prompt("Enter cover image URL: ");
        String location = view.prompt("Enter location: ");
        int id = bookService.getAllBooks().getLast().getId() + 1;

        Book book = entityFactoryService.createEntity(Book.class, id, isbn, title, author, publicationDate,
                pages, price, genre, isAvailable, coverImageUrl, location);
        bookService.addBook(book);
        view.display("Book added: " + book);
    }

    private void memberMenu() {
        List<MenuAction> memberMenu = List.of(
                new MenuAction("1", "Add a member", this::addMember),
                new MenuAction("2", "Advanced filter on members", () -> filterEntities(Member.class, memberService.getAllMembers())),
                new MenuAction("3", "Back", () -> {})
        );
        showMenu("Member Management", memberMenu);
    }

    private void addMember() {
        String firstName = view.prompt("Enter first name: ");
        String lastName = view.prompt("Enter last name: ");
        LocalDate membershipDate = LocalDate.parse(view.prompt("Enter membership date (yyyy-MM-dd): "));
        MembershipType membershipType = MembershipType.valueOf(view.prompt("Enter membership type: ").toUpperCase());
        String email = view.prompt("Enter email: ");
        String phoneNumber = view.prompt("Enter phone number: ");
        boolean activeMembership = Boolean.parseBoolean(view.prompt("Is active membership (true/false): "));
        String profileImageUrl = view.prompt("Enter profile image URL: ");
        int id = memberService.getAllMembers().getLast().getId() + 1;

        Member member = entityFactoryService.createEntity(Member.class, id, firstName, lastName, membershipDate,
                membershipType, email, phoneNumber, activeMembership, profileImageUrl);
        memberService.addMember(member);
        view.display("Member added: " + member);
    }

    private void librarianMenu() {
        List<MenuAction> librarianMenu = List.of(
                new MenuAction("1", "Add a librarian", this::addLibrarian),
                new MenuAction("2", "Advanced filter on librarians", () -> filterEntities(Librarian.class, librarianService.getAllLibrarians())),
                new MenuAction("3", "Back", () -> {})
        );
        showMenu("Librarian Management", librarianMenu);
    }

    private void addLibrarian() {
        String firstName = view.prompt("Enter first name: ");
        String lastName = view.prompt("Enter last name: ");
        LocalDate hireDate = LocalDate.parse(view.prompt("Enter hire date (yyyy-MM-dd): "));
        double salary = Double.parseDouble(view.prompt("Enter salary: "));
        Department department = Department.valueOf(view.prompt("Enter department: ").toUpperCase());
        boolean fullTime = Boolean.parseBoolean(view.prompt("Is full time (true/false): "));
        String employeePhotoUrl = view.prompt("Enter employee photo URL: ");
        int id = librarianService.getAllLibrarians().getLast().getId() + 1;

        Librarian librarian = entityFactoryService.createEntity(Librarian.class, id,
                firstName, lastName, hireDate, salary, department, fullTime, employeePhotoUrl);
        librarianService.addLibrarian(librarian);
        view.display("Librarian added: " + librarian);
    }

    private void checkoutMenu() {
        List<MenuAction> checkoutMenu = List.of(
                new MenuAction("1", "Add a checkout", this::addCheckout),
                new MenuAction("2", "Advanced filter on checkouts", () -> filterEntities(BookCheckout.class, checkoutService.getAllBookCheckouts())),
                new MenuAction("3", "Back", () -> {})
        );
        showMenu("Checkout Management", checkoutMenu);
    }

    private void addCheckout() {
        int bookId = Integer.parseInt(view.prompt("Enter book ID: "));
        Book book = bookService.getAllBooks().stream()
                .filter(b -> b.getId() == bookId)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Book not found"));

        int memberId = Integer.parseInt(view.prompt("Enter member ID: "));
        Member member = memberService.getAllMembers().stream()
                .filter(m -> m.getId() == memberId)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Member not found"));

        LocalDate checkoutDate = LocalDate.parse(view.prompt("Enter checkout date (yyyy-MM-dd): "));
        LocalDate dueDate = LocalDate.parse(view.prompt("Enter due date (yyyy-MM-dd): "));
        String returnInput = view.prompt("Enter return date (yyyy-MM-dd or empty if not returned): ");
        LocalDate returnDate = returnInput.isBlank() ? null : LocalDate.parse(returnInput);

        boolean renewed = Boolean.parseBoolean(view.prompt("Renewed (true/false): "));
        CheckoutStatus status = CheckoutStatus.valueOf(view.prompt("Enter status: ").toUpperCase());

        int librarianId = Integer.parseInt(view.prompt("Enter librarian ID who handled checkout: "));
        Librarian librarian = librarianService.getAllLibrarians().stream()
                .filter(l -> l.getId() == librarianId)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Librarian not found"));

        int id = checkoutService.getAllBookCheckouts().getLast().getId() + 1;

        BookCheckout checkout = entityFactoryService.createEntity(BookCheckout.class, id,
                book, member, checkoutDate, dueDate, returnDate, renewed, status, librarian);
        checkoutService.addCheckout(checkout);
        view.display("Checkout added: " + checkout);
    }

    private <T> void filterEntities(Class<T> clazz, List<T> entities) {
        String field = view.prompt("Enter field name: ");
        String operator = view.prompt("Enter operator (=, !=, contains, >, <, >=, <=): ");
        String value = view.prompt("Enter value: ");

        Predicate<T> predicate = filteringService.buildPredicate(clazz, field, operator, value);
        filteringService.filter(entities, predicate).forEach(view::display);
    }

    static class MenuAction {
        private final String key;
        private final String description;
        private final Runnable action;

        MenuAction(String key, String description, Runnable action) {
            this.key = key;
            this.description = description;
            this.action = action;
        }

        public String getKey() { return key; }
        public String getDescription() { return description; }
        public void execute() { action.run(); }
    }
}