package de.dkt.eservices.eparrotrepository.ddbb;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DocumentMapper implements RowMapper<Document> {

	public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
		      Document doc = new Document();
		      doc.setDocumentId(rs.getInt("documentId"));
		      doc.setCollectionId(rs.getInt("collectionId"));
		      doc.setDocumentName(rs.getString("documentName"));
		      doc.setDescription(rs.getString("description"));
		      doc.setContent(rs.getString("content"));
		      doc.setAnnotatedContent(rs.getString("annotatedContent"));
		      doc.setHighlightedContent(rs.getString("highlightedContent"));
		      return doc;
		   }
	   
}
