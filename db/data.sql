INSERT INTO books (isbn, title, author, publication_date, pages, price, genre, is_available, cover_image_url, location) VALUES
    ('978-0312866693', 'The Odyssey', 'Homer', '1996-11-01', 560, 12.99, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780312866693-M.jpg', 'Shelf 1, Alley A, Floor 1'),
    ('978-0261103306', 'The Hobbit', 'J.R.R. Tolkien', '2002-09-15', 310, 14.50, 'FANTASY', TRUE, 'https://covers.openlibrary.org/b/isbn/9780261103306-L.jpg', 'Shelf 2, Alley B, Floor 1'),
    ('978-0553380163', 'A Brief History of Time', 'Stephen Hawking', '1998-03-01', 212, 18.00, 'SCIENCE', TRUE, 'https://covers.openlibrary.org/b/isbn/9780553380163-M.jpg', 'Shelf 5, Alley C, Floor 2'),
    ('978-1408703748', 'Steve Jobs', 'Walter Isaacson', '2011-10-24', 656, 20.00, 'BIOGRAPHY', TRUE, 'https://covers.openlibrary.org/b/isbn/9781408703748-M.jpg', 'Shelf 3, Alley D, Floor 1'),
    ('978-0439064873', 'Harry Potter and the Chamber of Secrets', 'J.K. Rowling', '1999-06-02', 341, 9.99, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780439064873-M.jpg', 'Shelf 4, Alley E, Floor 1'),
    ('978-0439136365', 'Harry Potter and the Prisoner of Azkaban', 'J.K. Rowling', '2001-07-08', 435, 10.99, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780439136365-M.jpg', 'Shelf 4, Alley E, Floor 1'),
    ('978-0345339706', 'The Lord of the Rings', 'J.R.R. Tolkien', '2004-08-15', 1178, 25.00, 'FANTASY', TRUE, 'https://covers.openlibrary.org/b/isbn/9780345339706-M.jpg', 'Shelf 2, Alley B, Floor 1'),
    ('978-0399590504', 'Educated', 'Tara Westover', '2018-02-20', 334, 16.00, 'BIOGRAPHY', TRUE, 'https://covers.openlibrary.org/b/isbn/9780399590504-M.jpg', 'Shelf 3, Alley D, Floor 2'),
    ('978-0062316110', 'Sapiens', 'Yuval Noah Harari', '2015-09-01', 498, 22.00, 'HISTORY', TRUE, 'https://covers.openlibrary.org/b/isbn/9780062316110-M.jpg', 'Shelf 5, Alley C, Floor 2'),
    ('978-0307265432', 'The Road', 'Cormac McCarthy', '2006-09-26', 287, 12.00, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780307265432-M.jpg', 'Shelf 1, Alley A, Floor 2'),
    ('978-0007492190', 'The Alchemist', 'Paulo Coelho', '2014-04-15', 208, 11.99, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780007492190-M.jpg', 'Shelf 1, Alley A, Floor 2'),
    ('978-0521825146', 'Pride and Prejudice', 'Jane Austen', '2002-03-10', 279, 9.50, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780521825146-M.jpg', 'Shelf 1, Alley A, Floor 1'),
    ('978-0226469379', 'The Iliad', 'Homer', '2003-05-21', 704, 14.99, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780226469379-M.jpg', 'Shelf 1, Alley A, Floor 1'),
    ('978-0743275064', 'Angels & Demons', 'Dan Brown', '2000-05-01', 616, 15.00, 'MYSTERY', TRUE, 'https://covers.openlibrary.org/b/isbn/9780743275064-M.jpg', 'Shelf 2, Alley B, Floor 1'),
    ('978-0307269751', 'The Girl with the Dragon Tattoo', 'Stieg Larsson', '2008-08-01', 672, 13.50, 'MYSTERY', TRUE, 'https://covers.openlibrary.org/b/isbn/9780307269751-M.jpg', 'Shelf 2, Alley B, Floor 2'),
    ('978-0451524935', '1984', 'George Orwell', '2003-05-06', 328, 10.99, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780451524935-M.jpg', 'Shelf 1, Alley A, Floor 2'),
    ('978-0142424179', 'The Fault in Our Stars', 'John Green', '2012-01-10', 313, 12.50, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780142424179-M.jpg', 'Shelf 1, Alley A, Floor 2'),
    ('978-0061120084', 'To Kill a Mockingbird', 'Harper Lee', '2006-05-23', 336, 11.99, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780061120084-M.jpg', 'Shelf 1, Alley A, Floor 1'),
    ('978-0151002177', 'Animal Farm', 'George Orwell', '2004-06-12', 112, 7.99, 'FICTION', TRUE, 'https://covers.openlibrary.org/b/isbn/9780151002177-M.jpg', 'Shelf 1, Alley A, Floor 1'),
    ('978-0450032202', 'The Shining', 'Stephen King', '2005-01-28', 447, 13.00, 'HORROR', TRUE, 'https://covers.openlibrary.org/b/isbn/9780450032202-M.jpg', 'Shelf 6, Alley F, Floor 2');

INSERT INTO members (first_name, last_name, membership_date, membership_type, email, phone_number, active_membership, profile_image_url) VALUES
    ('Admin', 'Admin', '2020-01-01', 'ADMIN', 'admin.admin@email.com', '000-0000', TRUE, 'https://randomuser.me/api/portraits/man/2.jpg'),
    ('Alice', 'Johnson', '2020-01-15', 'PREMIUM', 'alice.johnson@email.com', '555-1234', TRUE, 'https://randomuser.me/api/portraits/women/1.jpg'),
    ('Bob', 'Smith', '2019-05-23', 'STANDARD', 'bob.smith@email.com', '555-5678', TRUE, 'https://randomuser.me/api/portraits/men/2.jpg'),
    ('Charlie', 'Brown', '2021-03-10', 'STUDENT', 'charlie.brown@email.com', '555-9012', TRUE, 'https://randomuser.me/api/portraits/men/3.jpg'),
    ('Diana', 'Prince', '2018-07-19', 'PREMIUM', 'diana.prince@email.com', '555-3456', FALSE, 'https://randomuser.me/api/portraits/women/4.jpg'),
    ('Ethan', 'Hunt', '2022-09-05', 'STANDARD', 'ethan.hunt@email.com', '555-7890', TRUE, 'https://randomuser.me/api/portraits/men/5.jpg');


INSERT INTO librarians (first_name, last_name, hire_date, salary, department, full_time, employee_photo_url, email, phone_number) VALUES
    ('Laura', 'Williams', '2015-04-12', 40000, 'GENERAL', TRUE, 'https://randomuser.me/api/portraits/women/6.jpg', 'laura.williams@email.com', 1234567),
    ('Mark', 'Taylor', '2010-06-08', 45000, 'RESEARCH', TRUE, 'https://randomuser.me/api/portraits/men/7.jpg', 'mark.taylor@email.com', 1234567),
    ('Nancy', 'Davis', '2020-02-01', 38000, 'CHILDRENS_SECTION', TRUE, 'https://randomuser.me/api/portraits/women/8.jpg', 'nancy.davis@email.com', 1234567),
    ('Oscar', 'Martinez', '2018-11-03', 42000, 'ARCHIVE', FALSE, 'https://randomuser.me/api/portraits/men/9.jpg', 'oscar.martinez@email.com', 1234567),
    ('Paula', 'Evans', '2012-08-21', 46000, 'GENERAL', TRUE, 'https://randomuser.me/api/portraits/women/10.jpg', 'paula.evans@email.com', 1234567);

INSERT INTO book_checkouts (book_id, member_id, checkout_date, due_date, return_date, renewed, status, handled_by_librarian_id) VALUES
    (1, 1, '2023-09-01', '2023-09-15', NULL, FALSE, 'CHECKED_OUT', 1),
    (2, 2, '2023-08-20', '2023-09-03', '2023-09-02', TRUE,  'RETURNED',     2),
    (3, 3, '2023-07-10', '2023-07-24', '2023-07-25', FALSE, 'OVERDUE',      3),
    (4, 4, '2023-06-05', '2023-06-19', '2023-06-18', FALSE, 'RETURNED',     4),
    (5, 5, '2023-05-12', '2023-05-26', NULL, FALSE, 'CHECKED_OUT', 5);

INSERT INTO users (member_id, username, password, flags, token) VALUES
    (1, 'admin', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=', 'ADMIN', '897f115a-e6a0-4583-ae68-8c7e6982bf11');
