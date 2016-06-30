package de.dkt.eservices.eparrotrepository.ddbb;

import java.util.LinkedList;
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
		String SQL = "update Collections SET timelining='"+timelining+"', geolocalization='"+geolocalization+"', entitylinking='"+semanticexploration+"',"
				+ " clustering='"+clustering+"', documents='"+documents+"' "
				+ " WHERE collectionName='"+collectionName+"'";
		try{
			jdbcTemplateObject.update( SQL );
		}
		catch(Exception e){
			e.printStackTrace();
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
		
		if(annotatedContent==null){
			annotatedContent="";
		}
		if(highlightedContent==null){
			highlightedContent="";
		}
		String SQL = "insert into Documents (documentId, collectionId, userId, documentName, description, analysis, content, annotatedContent, highlightedContent) "
				+ "values (NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplateObject.update( SQL, collectionId, userId, documentName, description, analysis, content, annotatedContent, highlightedContent);
		String SQL2 = "select MAX(documentId) id from Documents";
		Integer i = jdbcTemplateObject.queryForObject(SQL2, new IntegerMapper());
		return i;
	}
	
	@Override
	public boolean updateDocument(String documentName, String description, String analysis, String content, String annotatedContent, String highlightedContent) {
		if(documentName==null){
			return false;
		}
		String SQL = "UPDATE Documents SET ";
		if(description!=null){
			SQL += "description='"+description+"', ";
		}
		if(analysis!=null){
			SQL += "analysis='"+analysis+"', ";
		}
		if(content!=null){
			SQL += "content='"+content+"', ";
		}
		if(annotatedContent!=null){
			SQL += "annotatedContent='"+annotatedContent+"', ";
		}
		if(highlightedContent!=null){
			SQL += "highlightedContent='"+highlightedContent+"', ";
		}
		SQL = SQL.substring(0, SQL.lastIndexOf(','));
		SQL += " WHERE documentName='"+documentName+"'";
		jdbcTemplateObject.update( SQL );
		return true;
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
	public Document getDocument(String documentName) {
		String SQL = "select * from Documents where documentName = ?";
		List<Document> docs = jdbcTemplateObject.query(SQL, new Object[]{documentName}, new DocumentMapper());
		if(docs.isEmpty()){
			return null;
		}
		return docs.get(0);
	}

	@Override
	public List<Collection> listCollections(String user) {
		List <Collection> feedbacks = new LinkedList<Collection>();
		String SQL = "select * from Collections";
		if(user!=null){
			String SQL0 = "select userId id from Users WHERE user='"+user+"'";
			List<Integer> users = jdbcTemplateObject.query(SQL0, new IntegerMapper());
			if(users.isEmpty()){
				return feedbacks;
			}
			SQL += " where ";
			SQL += "userId='"+users.get(0)+"' ";
		}
		feedbacks = jdbcTemplateObject.query(SQL, new CollectionMapper());
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
		String SQL = "select * from Collections where collectionName = ?";
		List<Collection> col = jdbcTemplateObject.query(SQL, new Object[]{collectionName}, new CollectionMapper());
		if(col.isEmpty()){
			return null;
		}
		int colId = col.get(0).getCollectionId();

		String SQL2 = "select * from Documents";
		if(collectionName!=null){
			SQL2 += " where ";
			SQL2 += "collectionId='"+colId+"' ";
		}
		List <Document> docs = jdbcTemplateObject.query(SQL2, new DocumentMapper());
		return docs;
	}

	@Override
	public List<Document> listDocumentsFromUser(String user, String collectionId) {
		if(user==null){
			return null;
		}
		String SQL0 = "select userId id from Users WHERE user='"+user+"'";
		List<Integer> users = jdbcTemplateObject.query(SQL0, new IntegerMapper());
		String SQL = "select d.documentId, d.collectionId, d.userId, d.documentName, d.description, d.analysis, d.content, d.annotatedContent, d.highlightedContent "
				+ "from Documents d,Collections where d.collectionId=Collections.collectionId AND Collections.userId = '"+users.get(0)+"' ";
		if(collectionId!=null){
			SQL += " AND ";
			SQL += "Collections.collectionId='"+collectionId+"' ";
		}
		System.out.println(SQL);
		List <Document> docs = jdbcTemplateObject.query(SQL, new DocumentMapper());
		System.out.println();
		System.out.println(docs.size());
		System.out.println();
		return docs;
	}

	@Override
	public List<Document> listDocumentsFromUserByName(String user, String collectionName) {
		if(user==null){
			return null;
		}
		String SQL0 = "select userId id from Users WHERE user='"+user+"'";
		List<Integer> users = jdbcTemplateObject.query(SQL0, new IntegerMapper());
		String SQL = "select d.documentId, d.collectionId, d.userId, d.documentName, d.description, d.analysis, d.content, d.annotatedContent, d.highlightedContent "
				+ "from Documents d,Collections where d.collectionId=Collections.collectionId AND Collections.userId = '"+users.get(0)+"' ";
		if(collectionName!=null){
			SQL += " AND ";
			SQL += "Collections.collectionName='"+collectionName+"' ";
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

	@Override
	public boolean checkDocumentPermission(String documentId, String documentName, String user) {
		String SQL0 = "select documentId id from Documents,Collections,Users WHERE Documents.collectionId=Collections.collectionId AND "
				+ "( ( Users.user='"+user+"' AND Collections.userId=Users.userId ) OR Collections.users LIKE '%"+user+"%' )";
		if(documentId!=null){
			SQL0 += " AND Documents.documentId='"+documentId+"'";
		}
		else{
			SQL0 += " AND Documents.documentName='"+documentName+"'";
		}

		System.out.println(SQL0);
		
		List<Integer> lu = jdbcTemplateObject.query(SQL0, new IntegerMapper());
//		User u = jdbcTemplateObject.queryForObject(SQL0, new UserMapper());
		System.out.println(lu.size());
		if(lu.isEmpty()){
			return false;
		}
		return true;
	}
	
	@Override
	public void deleteDocumentByName(String documentName) {
		String SQL = "delete from Documents where documentName = ?";
		jdbcTemplateObject.update(SQL, documentName);
		return;		
	}

	@Override
	public void deleteDocumentById(String documentId) {
		String SQL = "delete from Documents where documentId = ?";
		jdbcTemplateObject.update(SQL, documentId);
		return;		
	}
	
	
	/**
	 * MODELS methods
	 */
	
	public List<NLPModel> getModels(){
		String SQL0 = "select * from Models";
		List<NLPModel> lu = jdbcTemplateObject.query(SQL0, new NLPModelMapper());
		if(lu.isEmpty()){
			return null;
		}
		return lu;
	}
	
	public boolean addModel(String name, String type, String url, String analysis, String models, String language, String informat, String outformat, String mode){
		try {
			String SQL = "insert into Models (modelId, modelName, modelType, url, analysis, models, language, informat, outformat, mode) "
					+ "values (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplateObject.update( SQL, name, type, url, analysis, models, language, informat, outformat, mode);
			return true;
		} catch (Exception e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	
}
