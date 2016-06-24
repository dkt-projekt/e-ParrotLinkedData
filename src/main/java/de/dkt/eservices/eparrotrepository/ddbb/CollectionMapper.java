package de.dkt.eservices.eparrotrepository.ddbb;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CollectionMapper implements RowMapper<Collection> {

	public Collection mapRow(ResultSet rs, int rowNum) throws SQLException {
		      Collection col = new Collection();
		      col.setCollectionId(rs.getInt("collectionId"));
		      col.setUserId(rs.getInt("userId"));
		      col.setCollectionName(rs.getString("collectionName"));
		      col.setDescription(rs.getString("description"));
		      col.setAnalysis(rs.getString("analysis"));
		      col.setUsers(rs.getString("users"));
		      col.setTimelining(rs.getString("timelining"));
		      col.setGeolocation(rs.getString("geolocalization"));
		      col.setEntitylinking(rs.getString("entitylinking"));
		      col.setClustering(rs.getString("clustering"));
		      
		      int priv = rs.getInt("private");
		      if(priv==100){
		    	  col.setPriv(true);
		      }
		      else{
		    	  col.setPriv(false);
		      }
		      return col;
		   }
	   
}
