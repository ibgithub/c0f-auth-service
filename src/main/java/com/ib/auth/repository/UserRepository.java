package com.ib.auth.repository;

import com.ib.auth.dto.UserDto;
import com.ib.auth.service.AuthService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper
    private final RowMapper<UserDto> rowMapper = (rs, rowNum) -> {
        UserDto u = new UserDto();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        return u;
    };

    public List<UserDto> findAll() {
        return jdbcTemplate.query(
                "SELECT id, username, email FROM users",
                rowMapper
        );
    }

    public UserDto findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, username, email FROM users WHERE id = ?",
                rowMapper,
                id
        );
    }

    public UserDto findByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "SELECT id, username, email, password FROM auth.users WHERE username = ?",
                rowMapper,
                username
        );
    }

    public int save(UserDto user) {
        return jdbcTemplate.update(
                "INSERT INTO users (username, email) VALUES (?, ?)",
                user.getUsername(),
                user.getEmail()
        );
    }

    public int update(UserDto user) {
        return jdbcTemplate.update(
                "UPDATE users SET username = ?, email = ? WHERE id = ?",
                user.getUsername(),
                user.getEmail(),
                user.getId()
        );
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM users WHERE id = ?",
                id
        );
    }
}
