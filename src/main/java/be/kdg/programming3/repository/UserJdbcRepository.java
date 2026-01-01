package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Member;
import be.kdg.programming3.domain.User;
import be.kdg.programming3.domain.enums.Flags;
import be.kdg.programming3.repository.interfaces.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Profile("h2")
public class UserJdbcRepository implements UserRepository {
    private final JdbcClient jdbcClient;

    public  UserJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();

            user.setId(rs.getInt("id"));

            Member member = new Member();
            member.setId(rs.getInt("member_id"));
            user.setMember(member);

            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setFlags(Flags.valueOf(rs.getString("flags")));
            user.setToken(rs.getString("token"));

            return user;
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcClient.sql(sql)
                .query(rs -> {
                    List<User> list = new java.util.ArrayList<>();
                    while (rs.next()) {
                        list.add(new UserRowMapper().mapRow(rs, rs.getRow()));
                    }
                    return list;
                });
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public String getToken(int id) {
        String sql = "SELECT token FROM users WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query(rs -> {
                    rs.next();
                    return rs.getString("token");
                });
    }

    @Override
    public int updateToken(String token, int id) {
        String sql =  "UPDATE users SET token = :token WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("token", token)
                .param("id", id)
                .update();
    }

    @Override
    public User getUserById(int id) {
        String sql = "Select * FROM users WHERE id = :id";
        return jdbcClient.sql(sql).param("id", id).query(rs -> {
            User user = null;
            while(rs.next()){
                user = new UserRowMapper().mapRow(rs, rs.getRow());
            }
            return user;
        });
    }

    @Override
    public User getUserByMemberId(int id) {
        String sql = "Select * FROM users WHERE member_id = :id";
        return jdbcClient.sql(sql).param("id", id).query(new UserRowMapper()).optional().orElse(null);
    }

    @Override
    public User getUserByCredentials(String username, String password) {
        String sql = "Select * FROM users WHERE username = :username AND password = :password";
        return jdbcClient.sql(sql)
                .param("username", username)
                .param("password", password)
                .query(new UserRowMapper())
                .optional()
                .orElse(null);
    }

    @Override
    public User getUserByToken(String token) {
        String sql = "Select * FROM users WHERE token = :token";
        return jdbcClient.sql(sql).param("token", token).query(new UserRowMapper()).optional().orElse(null);
    }

    @Override
    public User registerUser(int id, String username, String password) {
        String sql = "INSERT INTO users (member_id, username, password, flags)VALUES (:member_id, :username, :password, :flags)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param("member_id", id)
                .param("username", username)
                .param("password", password)
                .param("flags", "USER")
                .update(keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            return null;
        }

        int generatedId = key.intValue();

        return getUserById(generatedId);
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        String sql = "Select * from  users WHERE username = :username";
        return jdbcClient.sql(sql).param("username", username).query(ResultSet::next);
    }

    @Override
    public int updateUsername(int id, String username) {
        String sql = "UPDATE users SET username = :username WHERE id = :id";
        return jdbcClient.sql(sql).param("username", username).param("id", id).update();
    }

    @Override
    public int updatePassword(int id, String password) {
        String sql = "UPDATE users SET password = :password WHERE id = :id";
        return jdbcClient.sql(sql).param("password", password).param("id", id).update();
    }
}
