package de.dkt.eservices.eparrotrepository.ddbb;

import java.util.List;

import javax.sql.DataSource;

public interface ParrotDAO {

	public void setDataSource(DataSource ds);

	public int createUser(String newUser, String newPassword, String newUserName, String newUserRole, String user, String password);

	public int createCollection(String user, String collectionId, String description, boolean priv, String analysis, String sUsers);

	public boolean updateCollection(String collectionName, String timelining, String geolocalization,String semanticexploration, String clustering, String documents);

	public int createDocument(String collection, String user, String documentName, String description, String analysis, String content, String annotatedContent, String highlightedContent);

//	public void create(LoggingInformation info);

	public Collection getCollection(String collectionId);

	public Collection getCollectionByName(String collectionName);

	public Document getDocument(String documentId);
	
	public User getUser(String email);

	public List<User> getUsers();

	public List<Collection> listCollections(String user);

	public List<Document> listDocumentsFromUser(String user, String collectionId);

	public List<Document> listDocumentsFromUserByName(String user, String collectionName);

	public List<Document> listDocumentsFromCollection(String collectionId);
	
	public List<Document> listDocumentsFromCollectionByName(String collectionName);

	public String listTimeliningFromCollection(String collectionId);

	public boolean checkUser(String user, String password);

	public boolean existsUser(String user);
	
	public boolean checkCollectionPermission(String collectionName, String user);

	
	public void delete(Integer id);

	public void update(Integer id, String type, String additionalInformation);
	
	public void deleteDocumentByName(String documentName);

	public void deleteDocumentById(String documentId);
}
