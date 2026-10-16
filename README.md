# Library Management System

## Author
**Peter Duga**

---

## Domain Description

This project is a **Library Management System** that allows users to view available books, manage members, librarians, and track book checkouts.  
The system supports both a **web interface (Thymeleaf)** and a **console-based interface**, depending on the active Spring profile.

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

## Spring Profiles

The project supports multiple Spring profiles, each serving a different purpose:

| Profile | Description |
|-------|------------|
| `h2` | Uses an H2 database with **JDBC repositories** |
| `jpa` | Uses **PostgreSQL** with **JPA repositories** |
| `web` | Web application using **Thymeleaf**, backed by **in-memory repositories** |
| `console` | Console-based Spring application using **in-memory repositories** |

> Note: To change the active profile please modify the `spring.profiles.default=jpa` in `application.properties`
---

## Database Configuration

### PostgreSQL (JPA profile)
```properties
spring.datasource.url=jdbc:postgresql:pro3_db
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD:Student_1234}
```

A `docker-compose.yml` file is provided to create the PostgreSQL database. It reads the same
`DB_PASSWORD` variable (falling back to `Student_1234` for local use if unset), so both sides
stay in sync — set `DB_PASSWORD` in your shell or a `.env` file to override it.

---

### H2 Configuration (JDBC profile)
```properties
app.mode=h2

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.datasource.url=jdbc:h2:file:./db/h2repo
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

spring.thymeleaf.cache=false
spring.messages.basename=messages
server.port=8080
```

---
## Running the Project

### Recommended Setup

- The project is typically run using the **IntelliJ “Run” button**.
- Default start URL for the web application:
```
http://localhost:8080/
```

### PostgreSQL Setup (Important)

To start PostgreSQL:
```bash
  docker compose up
```
**Important Note**  
Because database tables are created by JPA and data seeding is attempted immediately, tables may not exist yet when `data.sql` runs.

If this happens, manually seed the database:
```bash
  docker compose exec -T db psql -U postgres -d pro3_db < ./db/data.sql
```

Run this command from the **root of the project**.

---

## Authentication & Users

- There is **no default non-admin user**.
- Non-logged-in users currently have nearly the same access as logged-in users.
- An **admin user** exists:
```
Username: admin
Password: admin
```

- User registration is implemented and functional if additional users are needed.

---

## Assignment Completion Status

### Completed
- Multiple Spring profiles
- JDBC, JPA, and in-memory repositories
- Entity inheritance (Book → Comic)
- Custom authentication system
- Reflection-based dynamic filtering service
- Web & console interfaces
- Exception handling and custom error pages

### Not Fully Used
- Repository method queries and custom `@Query` methods from assignment 10  
  These methods **exist**, but are **not used anywhere** in the application due to lack of a real functional need.

---

## Unique Aspects of This Project

- **Dynamic reflection-based filtering service**  
  Allows filtering entities by arbitrary fields and operators at runtime.

- **Custom authentication system**  
  Built manually using cookies and tokens instead of Spring Security.

- **Overengineering where appropriate**  
  Combination of very basic parts and intentionally overengineered components for learning purposes

---

## Final Notes

If something fails, double-check:
- Active Spring profile
- Database availability
- Docker containers running
