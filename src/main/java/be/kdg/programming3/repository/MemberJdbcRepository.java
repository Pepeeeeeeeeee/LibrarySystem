package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Member;
import be.kdg.programming3.domain.enums.MembershipType;
import be.kdg.programming3.repository.interfaces.MemberRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@Profile("h2")
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcClient jdbcClient;

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate, JdbcClient jdbcClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcClient =  jdbcClient;
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("membership_date").toLocalDate(),
                    MembershipType.valueOf(rs.getString("membership_type")),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getBoolean("active_membership"),
                    rs.getString("profile_image_url")
            );
        }
    }

    @Override
    public List<Member> findAllMembers() {
        String sql = """
            SELECT id, first_name, last_name, membership_date,
                   membership_type, email, phone_number,
                   active_membership, profile_image_url
            FROM member
            """;

        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    @Override
    public Member findById(int id) {
        String sql = """
            SELECT id, first_name, last_name, membership_date,
                   membership_type, email, phone_number,
                   active_membership, profile_image_url
            FROM member
            WHERE id = ?
            """;

        return jdbcTemplate.query(sql, new MemberRowMapper(), id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Member findByEmail(String email) {
        String sql = "Select * from member where email = :email";
        return jdbcClient.sql(sql).param("email", email).query(rs -> {
           Member member = null;
           while(rs.next()) {
               member = new MemberRowMapper().mapRow(rs, rs.getRow());
           }
           return member;
        });
    }

    @Override
    public int save(String firstName, String lastName, LocalDate membershipDate, MembershipType membershipType, String email, String phoneNumber, boolean activeMembership) {
        String sql = """
            INSERT INTO member (first_name, last_name, membership_date,
                                 membership_type, email, phone_number,
                                 active_membership)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        return jdbcTemplate.update(sql,
                firstName,
                lastName,
                membershipDate,
                membershipType.name(),
                email,
                phoneNumber,
                activeMembership
        );
    }

    @Override
    public int update(Member member) {
        String sql = """
            UPDATE member SET
                first_name = ?,
                last_name = ?,
                membership_date = ?,
                membership_type = ?,
                email = ?,
                phone_number = ?,
                active_membership = ?,
                profile_image_url = ?
            WHERE id = ?
            """;

        return jdbcTemplate.update(sql,
                member.getFirstName(),
                member.getLastName(),
                java.sql.Date.valueOf(member.getMembershipDate()),
                member.getMembershipType().name(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.isActiveMembership(),
                member.getProfileImageUrl(),
                member.getId()
        );
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM member WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
