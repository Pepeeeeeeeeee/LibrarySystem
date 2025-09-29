package be.kdg.programming3.config;

import be.kdg.programming3.domain.*;
import be.kdg.programming3.domain.enums.*;
import java.time.LocalDate;
import java.util.List;

public class DataFactory {
    public static void seed(List<Book> books,
                            List<Member> members,
                            List<Librarian> librarians,
                            List<BookCheckout> checkouts) {
        // Seed Books
        books.add(new Book(1, "978-0140449136", "The Odyssey", "Homer",
                LocalDate.of(1996, 11, 1), 560, 12.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/id/8231996-L.jpg", "Shelf 1, Alley A, Floor 1"));

        books.add(new Book(2, "978-0261103573", "The Hobbit", "J.R.R. Tolkien",
                LocalDate.of(2002, 9, 15), 310, 14.50, Genre.FANTASY,
                true, "https://covers.openlibrary.org/b/id/6979861-L.jpg", "Shelf 2, Alley B, Floor 1"));

        books.add(new Book(3, "978-0553380163", "A Brief History of Time", "Stephen Hawking",
                LocalDate.of(1998, 3, 1), 212, 18.00, Genre.SCIENCE,
                true, "https://covers.openlibrary.org/b/id/240726-L.jpg", "Shelf 5, Alley C, Floor 2"));

        books.add(new Book(4, "978-1455586691", "Steve Jobs", "Walter Isaacson",
                LocalDate.of(2011, 10, 24), 656, 20.00, Genre.BIOGRAPHY,
                true, "https://covers.openlibrary.org/b/id/8099259-L.jpg", "Shelf 3, Alley D, Floor 1"));

        books.add(new Book(5, "978-0439064873", "Harry Potter and the Chamber of Secrets", "J.K. Rowling",
                LocalDate.of(1999, 6, 2), 341, 9.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/id/7888781-L.jpg", "Shelf 4, Alley E, Floor 1"));

        // Seed Members
        members.add(new Member(1, "Alice", "Johnson", LocalDate.of(2020, 1, 15),
                MembershipType.PREMIUM, "alice.johnson@email.com", "555-1234", true,
                "https://randomuser.me/api/portraits/women/1.jpg"));

        members.add(new Member(2, "Bob", "Smith", LocalDate.of(2019, 5, 23),
                MembershipType.STANDARD, "bob.smith@email.com", "555-5678", true,
                "https://randomuser.me/api/portraits/men/2.jpg"));

        members.add(new Member(3, "Charlie", "Brown", LocalDate.of(2021, 3, 10),
                MembershipType.STUDENT, "charlie.brown@email.com", "555-9012", true,
                "https://randomuser.me/api/portraits/men/3.jpg"));

        members.add(new Member(4, "Diana", "Prince", LocalDate.of(2018, 7, 19),
                MembershipType.PREMIUM, "diana.prince@email.com", "555-3456", false,
                "https://randomuser.me/api/portraits/women/4.jpg"));

        members.add(new Member(5, "Ethan", "Hunt", LocalDate.of(2022, 9, 5),
                MembershipType.STANDARD, "ethan.hunt@email.com", "555-7890", true,
                "https://randomuser.me/api/portraits/men/5.jpg"));

        // Seed Librarians
        librarians.add(new Librarian(1, "Laura", "Williams", LocalDate.of(2015, 4, 12),
                40000, Department.GENERAL, true, "https://randomuser.me/api/portraits/women/6.jpg"));

        librarians.add(new Librarian(2, "Mark", "Taylor", LocalDate.of(2010, 6, 8),
                45000, Department.RESEARCH, true, "https://randomuser.me/api/portraits/men/7.jpg"));

        librarians.add(new Librarian(3, "Nancy", "Davis", LocalDate.of(2020, 2, 1),
                38000, Department.CHILDRENS_SECTION, true, "https://randomuser.me/api/portraits/women/8.jpg"));

        librarians.add(new Librarian(4, "Oscar", "Martinez", LocalDate.of(2018, 11, 3),
                42000, Department.ARCHIVE, false, "https://randomuser.me/api/portraits/men/9.jpg"));

        librarians.add(new Librarian(5, "Paula", "Evans", LocalDate.of(2012, 8, 21),
                46000, Department.GENERAL, true, "https://randomuser.me/api/portraits/women/10.jpg"));

        // Seed BookCheckouts (many-to-many relationships)
        checkouts.add(new BookCheckout(1, books.get(0), members.get(0), LocalDate.of(2023, 9, 1),
                LocalDate.of(2023, 9, 15), null, false, CheckoutStatus.CHECKED_OUT, librarians.get(0)));
        checkouts.add(new BookCheckout(2, books.get(1), members.get(1), LocalDate.of(2023, 8, 20),
                LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 2), true, CheckoutStatus.RETURNED, librarians.get(1)));
        checkouts.add(new BookCheckout(3, books.get(2), members.get(2), LocalDate.of(2023, 7, 10),
                LocalDate.of(2023, 7, 24), LocalDate.of(2023, 7, 25), false, CheckoutStatus.OVERDUE, librarians.get(2)));
        checkouts.add(new BookCheckout(4, books.get(3), members.get(3), LocalDate.of(2023, 6, 5),
                LocalDate.of(2023, 6, 19), LocalDate.of(2023, 6, 18), false, CheckoutStatus.RETURNED, librarians.get(3)));
        checkouts.add(new BookCheckout(5, books.get(4), members.get(4), LocalDate.of(2023, 5, 12),
                LocalDate.of(2023, 5, 26), null, false, CheckoutStatus.CHECKED_OUT, librarians.get(4)));

        // Link checkouts to books and members
        checkouts.forEach(checkout -> {
            checkout.getBook().addCheckout(checkout);
            checkout.getMember().addCheckout(checkout);
        });
    }
}