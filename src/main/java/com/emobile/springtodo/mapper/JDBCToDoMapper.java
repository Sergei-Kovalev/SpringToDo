package com.emobile.springtodo.mapper;

import com.emobile.springtodo.entity.ToDo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class JDBCToDoMapper implements RowMapper<ToDo> {

    @Override
    public ToDo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ToDo.builder()
                .id(UUID.fromString(rs.getString("id")))
                .description(rs.getString("description"))
                .expirationDate(rs.getObject("expiration_date", LocalDateTime.class))
                .isDone(rs.getBoolean("is_done"))
                .build();
    }
}
