# Library Management System

## Programming 5
**ACS201 - Peter Duga - peter.duga@student.kdg.be - 0172582-19**

---

## Domain Description

This project is a **Library Management System** that allows users to view available books, manage members, librarians, and track book checkouts.  

### Core Entities & Relationships (High-Level)

- **Book**
    - Represents a book available in the library.
    - Can be checked out by members.
    - Includes a subclass (`Comic`) which adds an extra attribute (`artist`).

- **Member**
    - Represents a library member.
    - A member can exist **without** a user account.
    - Each member has a membership type (at minimum `FREE`).

- **User**
    - Represents an authenticated account.
    - A user **cannot exist without a Member**.
    - Used for authentication and authorization.

- **Librarian**
    - Represents a staff member.
    - One librarian can handle **multiple checkouts**.

- **BookCheckout**
    - Represents the checkout of a book by a member.
    - Each checkout is handled by **exactly one librarian**.

> Note: While a book could theoretically have multiple checkouts over time, this is **not implemented** in the current project and is intentionally not emphasized.

---

## Build & Run Instructions
### Requirements

This project is set to `Java 25`, but shouldn't have compatibility problems until `Java 21` (included).
To check the version of your installed Java version run:
```Bash
java -version
```

> This project uses the Gradle Wrapper (gradlew), so no global Gradle installation is required.

### Docker setup
```Bash
docker-compose up
```

> The table creation and data seeding is done automatically using Hibernate
 
### Build project
#### Build on Linux / macOS
```Bash
./gradlew clean build
```

#### On Windows
```Bash
gradlew.bat clean build
```

> This will compile the code and run the tests, the created JAR file is located in `build/libs/`

### Run application using Gradle
#### Linux/macOS
```Bash
./gradlew bootRun
```

#### Windows
```Bash
gradlew.bat bootRun
```

### Run application using built JAR
```Bash
java -jar build/libs/librarysystem-0.0.1-SNAPSHOT.jar
```
(Replace with your actual JAR name if different.)

### Stop application
```Bash
CTRL + C
```

### Run tests only
```Bash
./gradlew test
```

### Clean the Project
```Bash
./gradlew clean
```

### Build Without Running Tests
```Bash
./gradlew build -x test
```
---

## Week 2

### Fetching all books - OK (200)

Request:
```
GET /api/books HTTP/1.1
Host: localhost:8080
Accept: application/json
```

Response:
```
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "isbn": "978-0312866693",
    "title": "The Odyssey",
    "author": "Homer",
    "publicationDate": "1996-11-01",
    "pages": 560,
    "price": 12.99,
    "genre": "FICTION",
    "available": true,
    "coverImageUrl": "https://covers.openlibrary.org/b/isbn/9780312866693-M.jpg",
    "location": "Shelf 1, Alley A, Floor 1"
  },
  {
    "id": 2,
    "isbn": "978-0261103306",
    "title": "The Hobbit",
    "author": "J.R.R. Tolkien",
    "publicationDate": "2002-09-15",
    "pages": 310,
    "price": 14.50,
    "genre": "FANTASY",
    "available": true,
    "coverImageUrl": "https://covers.openlibrary.org/b/isbn/9780261103306-L.jpg",
    "location": "Shelf 2, Alley B, Floor 1"
  }
]
```

---

### Fetching one book - OK (200)

Request:
```
GET /api/books/1 HTTP/1.1
Host: localhost:8080
Accept: application/json
```

Response:
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 1,
  "isbn": "978-0312866693",
  "title": "The Odyssey",
  "author": "Homer",
  "publicationDate": "1996-11-01",
  "pages": 560,
  "price": 12.99,
  "genre": "FICTION",
  "available": true,
  "coverImageUrl": "https://covers.openlibrary.org/b/isbn/9780312866693-M.jpg",
  "location": "Shelf 1, Alley A, Floor 1"
}
```

---

### Fetching one book - Not Found (404)

Request:
```
GET /api/books/9999 HTTP/1.1
Host: localhost:8080
Accept: application/json
```

Response:
```
HTTP/1.1 404 Not Found
```

---

### Deleting one book - No Content (204)

Request:
```
DELETE /api/books/20 HTTP/1.1
Host: localhost:8080
Accept: application/json
```

Response:
```
HTTP/1.1 204 No Content
```

---

### Searching for books - OK (200)

Request:
```
GET /api/books/filter?fields=author&query=Homer HTTP/1.1
Host: localhost:8080
Accept: application/json
```

Response:
```
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "isbn": "978-0312866693",
    "title": "The Odyssey",
    "author": "Homer",
    "publicationDate": "1996-11-01",
    "pages": 560,
    "price": 12.99,
    "genre": "FICTION",
    "available": true,
    "coverImageUrl": "https://covers.openlibrary.org/b/isbn/9780312866693-M.jpg",
    "location": "Shelf 1, Alley A, Floor 1"
  },
  {
    "id": 13,
    "isbn": "978-0226469379",
    "title": "The Iliad",
    "author": "Homer",
    "publicationDate": "2003-05-21",
    "pages": 704,
    "price": 14.99,
    "genre": "FICTION",
    "available": true,
    "coverImageUrl": "https://covers.openlibrary.org/b/isbn/9780226469379-M.jpg",
    "location": "Shelf 1, Alley A, Floor 1"
  }
]
```

---

### Creating a book - Created (201)

Request:
```
POST /api/books HTTP/1.1
Host: localhost:8080
Accept: application/json
Content-Type: application/json

{
  "isbn": "978-1111111111",
  "title": "New Book",
  "author": "Test Author",
  "publicationDate": "2020-01-01",
  "pages": 300,
  "price": 19.99,
  "genre": "FICTION",
  "available": true,
  "coverImageUrl": "https://example.com/book.jpg",
  "location": "Shelf 9, Alley Z, Floor 3",
  "comic": false
}
```

Response:
```
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 21,
  "isbn": "978-1111111111",
  "title": "New Book",
  "author": "Test Author",
  "publicationDate": "2020-01-01",
  "pages": 300,
  "price": 19.99,
  "genre": "FICTION",
  "available": true,
  "coverImageUrl": "https://example.com/book.jpg",
  "location": "Shelf 9, Alley Z, Floor 3"
}
```

---

### Fetching checkout details - OK (200)

Request:
```
GET /api/books/1/checkout HTTP/1.1
Host: localhost:8080
Accept: application/json
```

Response:
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "checkedOut": true,
  "expectedReturnDate": "2023-09-15",
  "librarianId": 1,
  "librarianName": "Laura Williams",
  "memberId": 1,
  "memberName": "Admin Admin"
}
```

---

### Fetching checkout details - No Content (204)

Request:
```
GET /api/books/6/checkout HTTP/1.1
Host: localhost:8080
Accept: application/json
```

Response:
```
HTTP/1.1 204 No Content
```

---

### Fetching checkout details - Not Found (404)

Request:
```
GET /api/books/9999/checkout HTTP/1.1
Host: localhost:8080
Accept: application/json
```

Response:
```
HTTP/1.1 404 Not Found
```
---

## Spring Profiles and Test Database Configuration

This project utilizes Spring Profiles to manage different configurations for development, production, and testing environments. Specifically, a `test` profile has been introduced to ensure that integration and service layer tests run against a dedicated test database without interfering with the main application's data.

### `application-test.properties`

A new configuration file, `src/main/resources/application-test.properties`, has been created to override default settings when the `test` profile is active. Key configurations in this file include:

-   `spring.sql.init.mode=never`: This setting prevents the automatic execution of `data.sql` (or `schema.sql`) when the `test` profile is active. This is crucial for tests, as it allows test data to be managed programmatically within the test classes (e.g., using `@BeforeEach` or `@DataJpaTest`'s transactional capabilities) rather than relying on a fixed initial dataset.
-   `spring.jpa.hibernate.ddl-auto=none`: This prevents Hibernate from automatically creating or updating the database schema during tests. For integration tests, it's often preferred to have a stable schema, possibly managed by Flyway or Liquibase, or to let `@DataJpaTest` handle schema creation for in-memory databases.
-   `spring.datasource.url=jdbc:postgresql://localhost:5432/pro5_db_test`: This points the test profile to a separate PostgreSQL database named `pro5_db_test`. This ensures complete isolation from the development database (`pro5_db`).

### Test Execution

To ensure tests run with the `test` profile activated, you can use the following command-line arguments:

#### Using Gradle:
```bash
./gradlew test -Dspring.profiles.active=test
```

When using `@SpringBootTest` or `@DataJpaTest` annotations in your test classes, you can directly specify the active profile using `@ActiveProfiles("test")`. This automatically applies the `application-test.properties` configuration for those specific tests.

By following this setup, tests are isolated, repeatable, and do not corrupt or depend on the state of the development database.