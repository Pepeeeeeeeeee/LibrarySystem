package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.domain.enums.Department;
import be.kdg.programming3.repository.interfaces.LibrarianRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Profile("h2")
public class LibrarianJdbcRepository implements LibrarianRepository {

    private final JdbcTemplate jdbcTemplate;

    public LibrarianJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class LibrarianRowMapper implements RowMapper<Librarian> {
        @Override
        public Librarian mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Librarian(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("hire_date").toLocalDate(),
                    rs.getDouble("salary"),
                    Department.valueOf(rs.getString("department")),
                    rs.getBoolean("full_time"),
                    rs.getString("employee_photo_url"),
                    rs.getString("email"),
                    rs.getInt("phone_number")
            );
        }
    }

    @Override
    public List<Librarian> findAllLibrarians() {
        String sql = """
            SELECT id, first_name, last_name, hire_date, salary, department,
                   full_time, employee_photo_url, email, phone_number
            FROM librarian
            """;

        return jdbcTemplate.query(sql, new LibrarianRowMapper());
    }

    @Override
    public Librarian findById(int id) {
        String sql = """
            SELECT id, first_name, last_name, hire_date, salary, department,
                   full_time, employee_photo_url, email, phone_number
            FROM librarian
            WHERE id = ?
            """;

        return jdbcTemplate.query(sql, new LibrarianRowMapper(), id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public int save(Librarian librarian) {
        String sql = """
            INSERT INTO librarian (first_name, last_name, hire_date, salary,
                                    department, full_time, employee_photo_url,
                                    email, phone_number)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        return jdbcTemplate.update(sql,
                librarian.getFirstName(),
                librarian.getLastName(),
                java.sql.Date.valueOf(librarian.getHireDate()),
                librarian.getSalary(),
                librarian.getDepartment().name(),
                librarian.isFullTime(),
                librarian.getEmployeePhotoUrl(),
                librarian.getEmail(),
                librarian.getPhoneNumber()
        );
    }

    @Override
    public int update(Librarian librarian) {
        String sql = """
            UPDATE librarian SET
                first_name = ?,
                last_name = ?,
                hire_date = ?,
                salary = ?,
                department = ?,
                full_time = ?,
                employee_photo_url = ?,
                email = ?,
                phone_number = ?
            WHERE id = ?
            """;

        return jdbcTemplate.update(sql,
                librarian.getFirstName(),
                librarian.getLastName(),
                java.sql.Date.valueOf(librarian.getHireDate()),
                librarian.getSalary(),
                librarian.getDepartment().name(),
                librarian.isFullTime(),
                librarian.getEmployeePhotoUrl(),
                librarian.getEmail(),
                librarian.getPhoneNumber(),
                librarian.getId()
        );
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM librarian WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
