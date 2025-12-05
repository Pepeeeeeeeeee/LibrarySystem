package be.kdg.programming3.config;

import be.kdg.programming3.domain.*;
import be.kdg.programming3.domain.enums.*;
import java.time.LocalDate;
import java.util.List;

public class DataFactory {
    public static void seed(List<Book> books, List<Member> members, List<Librarian> librarians, List<BookCheckout> checkouts, List<User> users) {
        // Seed Books
        books.add(new Book(1, "978-0312866693", "The Odyssey", "Homer",
                LocalDate.of(1996, 11, 1), 560, 12.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780312866693-M.jpg", "Shelf 1, Alley A, Floor 1"));
        books.add(new Book(2, "978-0261103306", "The Hobbit", "J.R.R. Tolkien",
                LocalDate.of(2002, 9, 15), 310, 14.50, Genre.FANTASY,
                true, "https://covers.openlibrary.org/b/isbn/9780261103306-L.jpg", "Shelf 2, Alley B, Floor 1"));
        books.add(new Book(3, "978-0553380163", "A Brief History of Time", "Stephen Hawking",
                LocalDate.of(1998, 3, 1), 212, 18.00, Genre.SCIENCE,
                true, "https://covers.openlibrary.org/b/isbn/9780553380163-M.jpg", "Shelf 5, Alley C, Floor 2"));
        books.add(new Book(4, "978-1408703748", "Steve Jobs", "Walter Isaacson",
                LocalDate.of(2011, 10, 24), 656, 20.00, Genre.BIOGRAPHY,
                true, "https://covers.openlibrary.org/b/isbn/9781408703748-M.jpg", "Shelf 3, Alley D, Floor 1"));
        books.add(new Book(5, "978-0439064873", "Harry Potter and the Chamber of Secrets", "J.K. Rowling",
                LocalDate.of(1999, 6, 2), 341, 9.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780439064873-M.jpg", "Shelf 4, Alley E, Floor 1"));
        books.add(new Book(6, "978-0439136365", "Harry Potter and the Prisoner of Azkaban", "J.K. Rowling",
                LocalDate.of(2001, 7, 8), 435, 10.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780439136365-M.jpg", "Shelf 4, Alley E, Floor 1"));
        books.add(new Book(7, "978-0345339706", "The Lord of the Rings", "J.R.R. Tolkien",
                LocalDate.of(2004, 8, 15), 1178, 25.00, Genre.FANTASY,
                true, "https://covers.openlibrary.org/b/isbn/9780345339706-M.jpg", "Shelf 2, Alley B, Floor 1"));
        books.add(new Book(8, "978-0399590504", "Educated", "Tara Westover",
                LocalDate.of(2018, 2, 20), 334, 16.00, Genre.BIOGRAPHY,
                true, "https://covers.openlibrary.org/b/isbn/9780399590504-M.jpg", "Shelf 3, Alley D, Floor 2"));
        books.add(new Book(9, "978-0062316110", "Sapiens", "Yuval Noah Harari",
                LocalDate.of(2015, 9, 1), 498, 22.00, Genre.HISTORY,
                true, "https://covers.openlibrary.org/b/isbn/9780062316110-M.jpg", "Shelf 5, Alley C, Floor 2"));
        books.add(new Book(10, "978-0307265432", "The Road", "Cormac McCarthy",
                LocalDate.of(2006, 9, 26), 287, 12.00, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780307265432-M.jpg", "Shelf 1, Alley A, Floor 2"));
        books.add(new Book(11, "978-0007492190", "The Alchemist", "Paulo Coelho",
                LocalDate.of(2014, 4, 15), 208, 11.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780007492190-M.jpg", "Shelf 1, Alley A, Floor 2"));
        books.add(new Book(12, "978-0521825146", "Pride and Prejudice", "Jane Austen",
                LocalDate.of(2002, 3, 10), 279, 9.50, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780521825146-M.jpg", "Shelf 1, Alley A, Floor 1"));
        books.add(new Book(13, "978-0226469379", "The Iliad", "Homer",
                LocalDate.of(2003, 5, 21), 704, 14.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780226469379-M.jpg", "Shelf 1, Alley A, Floor 1"));
        books.add(new Book(14, "978-0743275064", "Angels & Demons", "Dan Brown",
                LocalDate.of(2000, 5, 1), 616, 15.00, Genre.MYSTERY,
                true, "https://covers.openlibrary.org/b/isbn/9780743275064-M.jpg", "Shelf 2, Alley B, Floor 1"));
        books.add(new Book(15, "978-0307269751", "The Girl with the Dragon Tattoo", "Stieg Larsson",
                LocalDate.of(2008, 8, 1), 672, 13.50, Genre.MYSTERY,
                true, "https://covers.openlibrary.org/b/isbn/9780307269751-M.jpg", "Shelf 2, Alley B, Floor 2"));
        books.add(new Book(16, "978-0451524935", "1984", "George Orwell",
                LocalDate.of(2003, 5, 6), 328, 10.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780451524935-M.jpg", "Shelf 1, Alley A, Floor 2"));
        books.add(new Book(17, "978-0142424179", "The Fault in Our Stars", "John Green",
                LocalDate.of(2012, 1, 10), 313, 12.50, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780142424179-M.jpg", "Shelf 1, Alley A, Floor 2"));
        books.add(new Book(18, "978-0061120084", "To Kill a Mockingbird", "Harper Lee",
                LocalDate.of(2006, 5, 23), 336, 11.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780061120084-M.jpg", "Shelf 1, Alley A, Floor 1"));
        books.add(new Book(19, "978-0151002177", "Animal Farm", "George Orwell",
                LocalDate.of(2004, 6, 12), 112, 7.99, Genre.FICTION,
                true, "https://covers.openlibrary.org/b/isbn/9780151002177-M.jpg", "Shelf 1, Alley A, Floor 1"));
        books.add(new Book(20, "978-0450032202", "The Shining", "Stephen King",
                LocalDate.of(2005, 1, 28), 447, 13.00, Genre.HORROR,
                true, "https://covers.openlibrary.org/b/isbn/9780450032202-M.jpg", "Shelf 6, Alley F, Floor 2"));


        // Seed Members
        members.add(new Member(0, "Admin", "Admin", LocalDate.of(2020, 1, 1),
                MembershipType.ADMIN, "admin.admin@email.com", "000-0000", true,
                "https://randomuser.me/api/portraits/man/6.jpg"));
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
                40000, Department.GENERAL, true, "https://randomuser.me/api/portraits/women/6.jpg", "laura.williams@email.com", 1234567));

        librarians.add(new Librarian(2, "Mark", "Taylor", LocalDate.of(2010, 6, 8),
                45000, Department.RESEARCH, true, "https://randomuser.me/api/portraits/men/7.jpg", "mark.taylor@email.com", 1234567));

        librarians.add(new Librarian(3, "Nancy", "Davis", LocalDate.of(2020, 2, 1),
                38000, Department.CHILDRENS_SECTION, true, "https://randomuser.me/api/portraits/women/8.jpg", "nancy.davis@email.com", 1234567));

        librarians.add(new Librarian(4, "Oscar", "Martinez", LocalDate.of(2018, 11, 3),
                42000, Department.ARCHIVE, false, "https://randomuser.me/api/portraits/men/9.jpg", "oscar.martinez@email.com", 1234567));

        librarians.add(new Librarian(5, "Paula", "Evans", LocalDate.of(2012, 8, 21),
                46000, Department.GENERAL, true, "https://randomuser.me/api/portraits/women/10.jpg", "paula.evans@email.com", 1234567));

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

        users.add(new User(1, 0, "admin", "admin", Flags.ADMIN, ""));

        // Link checkouts to books and members
        checkouts.forEach(checkout -> {
            checkout.getBook().addCheckout(checkout);
            checkout.getMember().addCheckout(checkout);
        });
    }
}