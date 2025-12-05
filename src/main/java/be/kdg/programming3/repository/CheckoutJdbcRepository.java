package be.kdg.programming3.repository;

import be.kdg.programming3.domain.*;
import be.kdg.programming3.domain.enums.CheckoutStatus;
import be.kdg.programming3.repository.interfaces.CheckoutRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("h2")
public class CheckoutJdbcRepository implements CheckoutRepository {

    private final JdbcClient jdbcClient;

    public CheckoutJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static class BookCheckoutRowMapper implements RowMapper<BookCheckout> {
        @Override
        public BookCheckout mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getDate("publication_date").toLocalDate(),
                    rs.getInt("pages"),
                    rs.getDouble("price"),
                    Enum.valueOf(be.kdg.programming3.domain.enums.Genre.class, rs.getString("genre")),
                    rs.getBoolean("is_available"),
                    rs.getString("cover_image_url"),
                    rs.getString("location")
            );

            Member member = new Member(
                    rs.getInt("member_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("membership_date").toLocalDate(),
                    Enum.valueOf(be.kdg.programming3.domain.enums.MembershipType.class, rs.getString("membership_type")),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getBoolean("active_membership"),
                    rs.getString("profile_image_url")
            );

            Librarian librarian = null;
            int librarianId = rs.getInt("librarian_id");
            if (!rs.wasNull()) {
                librarian = new Librarian(
                        librarianId,
                        rs.getString("lib_first_name"),
                        rs.getString("lib_last_name"),
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getDouble("salary"),
                        Enum.valueOf(be.kdg.programming3.domain.enums.Department.class, rs.getString("department")),
                        rs.getBoolean("full_time"),
                        rs.getString("employee_photo_url"),
                        rs.getString("lib_email"),
                        rs.getInt("lib_phone")
                );
            }

            return new BookCheckout(
                    rs.getInt("checkout_id"),
                    book,
                    member,
                    rs.getDate("checkout_date").toLocalDate(),
                    rs.getDate("due_date").toLocalDate(),
                    rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                    rs.getBoolean("renewed"),
                    CheckoutStatus.valueOf(rs.getString("status")),
                    librarian
            );
        }
    }

    @Override
    public List<BookCheckout> findAll() {
        String sql = """
            SELECT bc.id AS checkout_id,
                   bc.checkout_date, bc.due_date, bc.return_date, bc.renewed, bc.status,
                   b.id AS book_id, b.isbn, b.title, b.author, b.publication_date, b.pages, b.price, b.genre, b.is_available, b.cover_image_url, b.location,
                   m.id AS member_id, m.first_name, m.last_name, m.membership_date, m.membership_type, m.email, m.phone_number, m.active_membership, m.profile_image_url,
                   l.id AS librarian_id, l.first_name, l.last_name, l.hire_date, l.salary, l.department, l.full_time, l.employee_photo_url, l.email AS lib_email, l.phone_number AS lib_phone
            FROM book_checkout bc
            JOIN book b ON bc.book_id = b.id
            JOIN member m ON bc.member_id = m.id
            LEFT JOIN librarian l ON bc.handled_by_librarian_id = l.id
        """;

        return jdbcClient.sql(sql)
                .query(rs -> {
                    List<BookCheckout> list = new java.util.ArrayList<>();
                    while (rs.next()) {
                        list.add(new BookCheckoutRowMapper().mapRow(rs, rs.getRow()));
                    }
                    return list;
                });
    }

    @Override
    public BookCheckout findById(int id) {
        String sql = """
            SELECT bc.id AS checkout_id,
                   bc.checkout_date, bc.due_date, bc.return_date, bc.renewed, bc.status,
                   b.id AS book_id, b.isbn, b.title, b.author, b.publication_date, b.pages, b.price, b.genre, b.is_available, b.cover_image_url, b.location,
                   m.id AS member_id, m.first_name, m.last_name, m.membership_date, m.membership_type, m.email, m.phone_number, m.active_membership, m.profile_image_url,
                   l.id AS librarian_id, l.first_name, l.last_name, l.hire_date, l.salary, l.department, l.full_time, l.employee_photo_url, l.email AS lib_email, l.phone_number AS lib_phone
            FROM book_checkout bc
            JOIN book b ON bc.book_id = b.id
            JOIN member m ON bc.member_id = m.id
            LEFT JOIN librarian l ON bc.handled_by_librarian_id = l.id
            WHERE bc.id = :id
        """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(rs -> rs.next() ? new BookCheckoutRowMapper().mapRow(rs, rs.getRow()) : null);
    }

    @Override
    public Optional<BookCheckout> findByBookId(int bookId) {
        String sql = """
        SELECT bc.id AS checkout_id,
               bc.checkout_date, bc.due_date, bc.return_date, bc.renewed, bc.status,
               b.id AS book_id, b.isbn, b.title, b.author, b.publication_date, b.pages, b.price, b.genre, b.is_available, b.cover_image_url, b.location,
               m.id AS member_id, m.first_name, m.last_name, m.membership_date, m.membership_type, m.email, m.phone_number, m.active_membership, m.profile_image_url,
               l.id AS librarian_id, l.first_name as lib_first_name, l.last_name as lib_last_name, l.hire_date, l.salary, l.department, l.full_time, l.employee_photo_url, l.email AS lib_email, l.phone_number AS lib_phone
        FROM book_checkout bc
        JOIN book b ON bc.book_id = b.id
        JOIN member m ON bc.member_id = m.id
        LEFT JOIN librarian l ON bc.handled_by_librarian_id = l.id
        WHERE bc.book_id = :bookId
        LIMIT 1
        """;

        return jdbcClient.sql(sql)
                .param("bookId", bookId)
                .query(rs -> {
                    if (!rs.next()) return Optional.empty();
                    return Optional.of(new BookCheckoutRowMapper().mapRow(rs, 0));
                });
    }

    @Override
    public List<Optional<BookCheckout>> findByLibrarianId(int librarianId) {
        String sql = """
        SELECT bc.id AS checkout_id,
               bc.checkout_date, bc.due_date, bc.return_date, bc.renewed, bc.status,
               b.id AS book_id, b.isbn, b.title, b.author, b.publication_date, b.pages, b.price, b.genre, b.is_available, b.cover_image_url, b.location,
               m.id AS member_id, m.first_name, m.last_name, m.membership_date, m.membership_type, m.email, m.phone_number, m.active_membership, m.profile_image_url,
               l.id AS librarian_id, l.first_name as lib_first_name, l.last_name as lib_last_name, l.hire_date, l.salary, l.department, l.full_time, l.employee_photo_url, l.email AS lib_email, l.phone_number AS lib_phone
        FROM book_checkout bc
        JOIN book b ON bc.book_id = b.id
        JOIN member m ON bc.member_id = m.id
        LEFT JOIN librarian l ON bc.handled_by_librarian_id = l.id
        WHERE bc.handled_by_librarian_id = :librarianId
        """;

        return jdbcClient.sql(sql)
                .param("librarianId", librarianId)
                .query(rs -> {
                    List<Optional<BookCheckout>> bookCheckouts = new ArrayList<>();
                    while(rs.next()){
                        bookCheckouts.add(Optional.of(new BookCheckoutRowMapper().mapRow(rs, 0)));
                    }
                    return bookCheckouts;
                });
    }

    @Override
    public List<Optional<BookCheckout>> findByMemberId(int memberId) {
        String sql = """
        SELECT bc.id AS checkout_id,
               bc.checkout_date, bc.due_date, bc.return_date, bc.renewed, bc.status,
               b.id AS book_id, b.isbn, b.title, b.author, b.publication_date, b.pages, b.price, b.genre, b.is_available, b.cover_image_url, b.location,
               m.id AS member_id, m.first_name, m.last_name, m.membership_date, m.membership_type, m.email, m.phone_number, m.active_membership, m.profile_image_url,
               l.id AS librarian_id, l.first_name as lib_first_name, l.last_name as lib_last_name, l.hire_date, l.salary, l.department, l.full_time, l.employee_photo_url, l.email AS lib_email, l.phone_number AS lib_phone
        FROM book_checkout bc
        JOIN book b ON bc.book_id = b.id
        JOIN member m ON bc.member_id = m.id
        LEFT JOIN librarian l ON bc.handled_by_librarian_id = l.id
        WHERE bc.member_id = :memberId
        """;

        return jdbcClient.sql(sql)
                .param("memberId", memberId)
                .query(rs -> {
                    List<Optional<BookCheckout>> bookCheckouts = new ArrayList<>();
                    while(rs.next()){
                        bookCheckouts.add(Optional.of(new BookCheckoutRowMapper().mapRow(rs, 0)));
                    }
                    return bookCheckouts;
                });
    }

    @Override
    public int save(BookCheckout checkout) {
        String sql = """
            INSERT INTO book_checkout
            (id, book_id, member_id, checkout_date, due_date, return_date, renewed, status, handled_by_librarian_id)
            VALUES 
            (:id, :book_id, :member_id, :checkout_date, :due_date, :return_date, :renewed, :status, :librarian_id)
        """;

        return jdbcClient.sql(sql)
                .param("id", checkout.getId())
                .param("book_id", checkout.getBook().getId())
                .param("member_id", checkout.getMember().getId())
                .param("checkout_date", java.sql.Date.valueOf(checkout.getCheckoutDate()))
                .param("due_date", java.sql.Date.valueOf(checkout.getDueDate()))
                .param("return_date", checkout.getReturnDate() != null ? java.sql.Date.valueOf(checkout.getReturnDate()) : null)
                .param("renewed", checkout.isRenewed())
                .param("status", checkout.getStatus().name())
                .param("librarian_id", checkout.getHandledByLibrarian() != null ? checkout.getHandledByLibrarian().getId() : null)
                .update();
    }

    @Override
    public int updateReturnDate(int id, LocalDate returnDate) {
        String sql = "UPDATE book_checkout SET return_date = :return_date WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("return_date", java.sql.Date.valueOf(returnDate))
                .param("id", id)
                .update();
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM book_checkout WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }
}
