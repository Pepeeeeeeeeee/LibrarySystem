package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Book;
import be.kdg.programming3.domain.enums.Genre;
import be.kdg.programming3.repository.interfaces.BookRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Profile("h2")
public class BookJdbcRepository implements BookRepository {
    private final JdbcClient jdbcClient;

    public BookJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getInt("id"),
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getDate("publication_date").toLocalDate(),
                    rs.getInt("pages"),
                    rs.getDouble("price"),
                    Genre.valueOf(rs.getString("genre")),
                    rs.getBoolean("is_available"),
                    rs.getString("cover_image_url"),
                    rs.getString("location")
            );
        }
    }

    public List<Book> findAllBooks() {
        String sql = "SELECT * FROM book";
        return jdbcClient.sql(sql)
                .query(rs -> {
                    List<Book> list = new java.util.ArrayList<>();
                    while (rs.next()) {
                        list.add(new BookRowMapper().mapRow(rs, rs.getRow()));
                    }
                    return list;
                });
    }

    public Book findById(int id) {
        String sql = "SELECT * FROM book WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query(rs -> rs.next() ? new BookRowMapper().mapRow(rs, rs.getRow()) : null);
    }

    public int save(Book book) {
        String sql = """
                INSERT INTO book
                (id, isbn, title, author, publication_date, pages, price, genre, is_available, cover_image_url, location)
                VALUES 
                (:id, :isbn, :title, :author, :publication_date, :pages, :price, :genre, :is_available, :cover_image_url, :location)
                """;

        return jdbcClient.sql(sql)
                .param("id", book.getId())
                .param("isbn", book.getIsbn())
                .param("title", book.getTitle())
                .param("author", book.getAuthor())
                .param("publication_date", java.sql.Date.valueOf(book.getPublicationDate()))
                .param("pages", book.getPages())
                .param("price", book.getPrice())
                .param("genre", book.getGenre().name())
                .param("is_available", book.isAvailable())
                .param("cover_image_url", book.getCoverImageUrl())
                .param("location", book.getLocation())
                .update();
    }

    public int updateAvailability(int id, boolean available) {
        String sql = "UPDATE book SET is_available = :available WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("available", available)
                .param("id", id)
                .update();
    }

    public int delete(int id) {
        String sql = "DELETE FROM book WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }

    @Override
    public int updateBook(Book book) {
            String sql = """
            UPDATE book SET isbn = :isbn, title = :title, author = :author, publication_date = :publicationDate, pages = :pages, price = :price,
            genre = :genre, is_available = :isAvailable, cover_image_url = :coverImageUrl, location = :location WHERE id = :id
            """;

            return jdbcClient.sql(sql)
                    .param("isbn", book.getIsbn())
                    .param("title", book.getTitle())
                    .param("author", book.getAuthor())
                    .param("publicationDate", book.getPublicationDate())
                    .param("pages", book.getPages())
                    .param("price", book.getPrice())
                    .param("genre", book.getGenre().name())
                    .param("isAvailable", book.isAvailable())
                    .param("coverImageUrl", book.getCoverImageUrl())
                    .param("location", book.getLocation())
                    .param("id", book.getId())
                    .update();
        }

    @Override
    public int deleteBookById(int id) {
        String sql = "DELETE FROM book WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }

}
