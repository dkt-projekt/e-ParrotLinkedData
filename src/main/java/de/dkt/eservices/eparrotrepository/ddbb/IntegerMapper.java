package de.dkt.eservices.eparrotrepository.ddbb;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class IntegerMapper implements RowMapper<Integer> {

	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
		      return rs.getInt("id");
		   }
	   
}
