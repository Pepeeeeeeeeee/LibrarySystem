DROP TABLE IF EXISTS BOOK_CHECKOUT;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS BOOK;
DROP TABLE IF EXISTS MEMBER;
DROP TABLE IF EXISTS LIBRARIAN;

CREATE TABLE BOOK (
                      id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      isbn VARCHAR(50) NOT NULL,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL,
                      publication_date DATE NOT NULL,
                      pages INT NOT NULL,
                      price DOUBLE NOT NULL,
                      genre VARCHAR(50) NOT NULL,
                      is_available BOOLEAN NOT NULL,
                      cover_image_url VARCHAR(500),
                      location VARCHAR(255)
);

CREATE TABLE LIBRARIAN (
                           id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           first_name VARCHAR(255) NOT NULL,
                           last_name VARCHAR(255) NOT NULL,
                           hire_date DATE NOT NULL,
                           email VARCHAR(255) NOT NULL,
                           phone_number INT NOT NULL,
                           salary DOUBLE NOT NULL,
                           department VARCHAR(50) NOT NULL,
                           full_time BOOLEAN NOT NULL,
                           employee_photo_url VARCHAR(500)
);

CREATE TABLE MEMBER (
                        id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        first_name VARCHAR(255) NOT NULL,
                        last_name VARCHAR(255) NOT NULL,
                        membership_date DATE NOT NULL,
                        membership_type VARCHAR(50) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        phone_number VARCHAR(50) NOT NULL,
                        active_membership BOOLEAN NOT NULL,
                        profile_image_url VARCHAR(500)
);

CREATE TABLE USERS (
                       id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       member_id INT NOT NULL,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       flags VARCHAR(50) NOT NULL,
                       token VARCHAR(255),

                       CONSTRAINT fk_memberId
                           FOREIGN KEY (member_id) REFERENCES MEMBER(id)
                               ON DELETE CASCADE
);

CREATE TABLE BOOK_CHECKOUT (
                               id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               book_id INT NOT NULL,
                               member_id INT NOT NULL,
                               checkout_date DATE NOT NULL,
                               due_date DATE NOT NULL,
                               return_date DATE,
                               renewed BOOLEAN NOT NULL,
                               status VARCHAR(50) NOT NULL,
                               handled_by_librarian_id INT,

                               CONSTRAINT fk_checkout_book
                                   FOREIGN KEY (book_id) REFERENCES BOOK(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_checkout_member
                                   FOREIGN KEY (member_id) REFERENCES MEMBER(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_checkout_librarian
                                   FOREIGN KEY (handled_by_librarian_id) REFERENCES LIBRARIAN(id)
                                       ON DELETE SET NULL
);

