package de.dkt.eservices.eparrotrepository.ddbb;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<User> {

	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setUserId(rs.getInt("userId"));
			user.setEmail(rs.getString("user"));
			user.setPassword(rs.getString("password"));
			user.setName(rs.getString("name"));
			return user;
	}

}
