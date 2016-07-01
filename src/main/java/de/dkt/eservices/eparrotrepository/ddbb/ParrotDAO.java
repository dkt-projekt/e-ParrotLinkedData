package de.dkt.eservices.eparrotrepository.ddbb;

import java.util.List;

import javax.sql.DataSource;

public interface ParrotDAO {

	public void setDataSource(DataSource ds);

	/****************************************
	 * USERS methods
	 ****************************************/
	
	public int createUser(String newUser, String newPassword, String newUserName, String newUserRole, String user, String password);

	public User getUser(String email);

	public List<User> getUsers();

	public boolean checkUser(String user, String password);

	public boolean existsUser(String user);
		
	/****************************************
	 * COLLECTIONS methods
	 ****************************************/
	
	public int createCollection(String user, String collectionId, String description, boolean priv, String analysis, String sUsers);

	public boolean updateCollection(String collectionName, String timelining, String geolocalization,String semanticexploration, String clustering, String documents);

	public Collection getCollection(String collectionId);

	public Collection getCollectionByName(String collectionName);

	public List<Collection> listCollections(String user);

	public String listTimeliningFromCollection(String collectionId);

	public boolean checkCollectionPermission(String collectionName, String user);

	/****************************************
	 * DOCUMENTS methods
	 ****************************************/
	
	public int createDocument(String collection, String user, String documentName, String description, String analysis, String content, String annotatedContent, String highlightedContent);

	public boolean updateDocument(String documentName, String description, String analysis, String content, String annotatedContent, String highlightedContent);

	public Document getDocument(String documentId);
	
	public List<Document> listDocumentsFromUser(String user, String collectionId, int limit);

	public List<Document> listDocumentsFromUserByName(String user, String collectionName, int limit);

	public List<Document> listDocumentsFromCollection(String collectionId, int limit);
	
	public List<Document> listDocumentsFromCollectionByName(String collectionName, int limit);

	public boolean checkDocumentPermission(String documentId, String documentName, String user);
	
	public void deleteDocumentByName(String documentName);

	public void deleteDocumentById(String documentId);
}
