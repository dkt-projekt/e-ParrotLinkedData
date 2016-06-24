package de.dkt.eservices.eparrotrepository.ddbb;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ParrotJDBCTemplate implements ParrotDAO{

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	@Autowired
	@Override
	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	@Override
	public int createUser(String newUser, String newPassword, String newUserName, String newUserRole, String user, String password) {
		String SQL0 = "select * from Users WHERE user='"+user+"' and password=PASSWORD('"+password+"') and role='admin'";
		List<User> lu = jdbcTemplateObject.query(SQL0, new UserMapper());
//		User u = jdbcTemplateObject.queryForObject(SQL0, new UserMapper());
//		System.out.println(u.getJSONObject().toString());
		if(lu.isEmpty()){
			return -2;
		}
//		String SQL = "insert into Users (userId,user, password, name, role) values (NULL,?, PASSWORD(?), ?, ?)";
//		jdbcTemplateObject.update( SQL, newUser, newPassword, newUserName, newUserRole);
		String SQL2 = "select MAX(userId) id FROM Users";
		Integer i = jdbcTemplateObject.queryForObject(SQL2, new IntegerMapper());
		return i;
	}

	@Override
	public int createCollection(String user, String collectionName, String description, boolean priv, String analysis, String sUsers) {
		String SQL0 = "select userId id from Users WHERE user='"+user+"'";
		int userId = jdbcTemplateObject.queryForObject(SQL0, new IntegerMapper());
		String SQL = "insert into Collections (collectionId, userId, collectionName, description, private, analysis, users, timelining, geolocalization, entitylinking, clustering, documents) "
				+ "values (NULL, ?, ?, ?, ?, ?, ?, '', '', '', '', '')";
		Integer privat = 0;
		if(priv){
			privat = 100;
		}
		else{
			privat = 200;
		}
		jdbcTemplateObject.update( SQL, userId+"", collectionName, description, privat+"", analysis, sUsers);
		String SQL2 = "select MAX(collectionId) id FROM Collections";
		Integer i = jdbcTemplateObject.queryForObject(SQL2, new IntegerMapper());
		return i;
	}


	public boolean updateCollection(String collectionName, String timelining, String geolocalization,String semanticexploration, String clustering, String documents) {
		String SQL = "update Collections SET timelining='?', geolocalization='?', entitylinking='?', clustering='?', documents='?' "
				+ " WHERE collectionName='"+collectionName+"'";
		try{
			jdbcTemplateObject.update( SQL, timelining, geolocalization, semanticexploration, clustering, documents);
		}
		catch(Exception e){
	//		System.out.println("ERROR at updating collection!");
			return false;
		}
		return true;
	}
	
	@Override
	public int createDocument(String collection, String user, String documentName, String description, String analysis, String content, String annotatedContent, String highlightedContent) {
		String SQL0 = "select userId id from Users WHERE user='"+user+"'";
		int userId = jdbcTemplateObject.queryForObject(SQL0, new IntegerMapper());
		String SQL1 = "select collectionId id from Collections WHERE collectionName='"+collection+"'";
		int collectionId = jdbcTemplateObject.queryForObject(SQL1, new IntegerMapper());
		String SQL = "insert into Documents (documentId, collectionId, userId, documentName, description, analysis, content, annotateContent, highlightedContent) "
				+ "values (NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplateObject.update( SQL, collectionId, userId, documentName, description, analysis, content, annotatedContent, highlightedContent);
		String SQL2 = "select MAX(documentId) id from Documents";
		Integer i = jdbcTemplateObject.queryForObject(SQL2, new IntegerMapper());
		return i;
	}
	
	@Override
	public User getUser(String email) {
		String SQL = "select * from Users where user = ?";
		User user = jdbcTemplateObject.queryForObject(SQL, new Object[]{email}, new UserMapper());
		return user;
	}

	@Override
	public List<User> getUsers() {
		String SQL = "select * from Users";
		List <User> users = jdbcTemplateObject.query(SQL, new UserMapper());
		return users;
	}

	@Override
	public Collection getCollection(String collectionId) {
		String SQL = "select * from Collections where collectionId = ?";
		List<Collection> col = jdbcTemplateObject.query(SQL, new Object[]{collectionId}, new CollectionMapper());
		if(col.isEmpty()){
			return null;
		}
		return col.get(0);
	}

	@Override
	public Collection getCollectionByName(String collectionName) {
		String SQL = "select * from Collections where collectionName = ?";
		List<Collection> col = jdbcTemplateObject.query(SQL, new Object[]{collectionName}, new CollectionMapper());
		if(col.isEmpty()){
			return null;
		}
		return col.get(0);
	}

	@Override
	public Document getDocument(String documentId) {
		String SQL = "select * from Documents where documentId = ?";
		Document doc = jdbcTemplateObject.queryForObject(SQL, new Object[]{documentId}, new DocumentMapper());
		return doc;
	}

	@Override
	public List<Collection> listCollections(String user) {
		String SQL = "select * from Collections";
		if(user!=null){
			SQL += " where ";
			SQL += "user='"+user+"' ";
		}
		List <Collection> feedbacks = jdbcTemplateObject.query(SQL, new CollectionMapper());
		return feedbacks;
	}

	@Override
	public List<Document> listDocumentsFromCollection(String collectionId) {
		String SQL = "select * from Documents";
		if(collectionId!=null){
			SQL += " where ";
			SQL += "collectionId='"+collectionId+"' ";
		}
		List <Document> docs = jdbcTemplateObject.query(SQL, new DocumentMapper());
		return docs;
	}

	@Override
	public List<Document> listDocumentsFromCollectionByName(String collectionName) {
		String SQL = "select * from Documents";
		if(collectionName!=null){
			SQL += " where ";
			SQL += "collectionName='"+collectionName+"' ";
		}
		List <Document> docs = jdbcTemplateObject.query(SQL, new DocumentMapper());
		return docs;
	}

	@Override
	public List<Document> listDocumentsFromUser(String user, String collectionId) {
		if(user==null){
			return null;
		}
		String SQL = "select * from Documents,Collections where Documents.collectionId=Collections.collectionId AND Collections.user = '"+user+"' ";
		if(collectionId!=null){
			SQL += " AND ";
			SQL += "collectionId='"+collectionId+"' ";
		}
		List <Document> docs = jdbcTemplateObject.query(SQL, new DocumentMapper());
		return docs;
	}

	@Override
	public List<Document> listDocumentsFromUserByName(String user, String collectionName) {
		if(user==null){
			return null;
		}
		String SQL = "select * from Documents,Collections where Documents.collectionId=Collections.collectionId AND Collections.user = '"+user+"' ";
		if(collectionName!=null){
			SQL += " AND ";
			SQL += "collectionName='"+collectionName+"' ";
		}
		List <Document> docs = jdbcTemplateObject.query(SQL, new DocumentMapper());
		return docs;
	}

	@Override
	public String listTimeliningFromCollection(String collectionId) {
		Collection col = getCollection(collectionId);
		return col.getTimelining();
	}


	@Override
	public void delete(Integer id) {
		String SQL = "delete from Feedback where id = ?";
		jdbcTemplateObject.update(SQL, id);
//		System.out.println("Deleted Record with ID = " + id );
		return;		
	}

	@Override
	public void update(Integer id, String type, String additionalInformation) {
		String SQL = "update Feedback set type = ?, additionalInformation = ? where id = ?";
		jdbcTemplateObject.update(SQL, type, additionalInformation,id);
//		System.out.println("Updated Record with ID = " + id );
		return;
	}

	@Override
	public boolean checkUser(String user, String password) {
		String SQL0 = "select * from Users WHERE user='"+user+"' and password=PASSWORD('"+password+"')";
		List<User> lu = jdbcTemplateObject.query(SQL0, new UserMapper());
//		User u = jdbcTemplateObject.queryForObject(SQL0, new UserMapper());
		if(lu.isEmpty()){
			return false;
		}
		return true;
	}

	@Override
	public boolean existsUser(String user) {
		String SQL0 = "select * from Users WHERE user='"+user+"'";
		List<User> lu = jdbcTemplateObject.query(SQL0, new UserMapper());
		if(lu.isEmpty()){
			return false;
		}
		return true;
	}


	@Override
	public boolean checkCollectionPermission(String collectionName, String user) {
		String SQL0 = "select collectionId id from Collections,Users WHERE Collections.collectionName='"+collectionName+"' AND "
				+ "( ( Users.user='"+user+"' AND Collections.userId=Users.userId ) OR Collections.users LIKE '%"+user+"%' )";
		List<Integer> lu = jdbcTemplateObject.query(SQL0, new IntegerMapper());
//		User u = jdbcTemplateObject.queryForObject(SQL0, new UserMapper());
		if(lu.isEmpty()){
			return false;
		}
		return true;
	}

}
