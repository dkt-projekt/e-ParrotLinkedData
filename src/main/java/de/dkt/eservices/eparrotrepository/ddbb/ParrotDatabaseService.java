package de.dkt.eservices.eparrotrepository.ddbb;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.dkt.eservices.eparrotrepository.EParrotRepositoryServiceStandAlone;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 */
@Component
public class ParrotDatabaseService {
    
	Logger logger = Logger.getLogger(EParrotRepositoryServiceStandAlone.class);
	
	@Autowired
	ParrotJDBCTemplate parrotDAO;

	public ParrotDatabaseService(){
	}
	
	public int updateUser(String newUser, String newPassword, String newUserName, String newUserRole, String user, String password) throws ExternalServiceFailedException {
        try {
        	return parrotDAO.updateUser(newUser, newPassword, newUserName, newUserRole, user, password);
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}
	
	public int storeUser(String newUser, String newPassword, String newUserName, String newUserRole, String user, String password) throws ExternalServiceFailedException {
        try {
        	return parrotDAO.createUser(newUser, newPassword, newUserName, newUserRole, user, password);
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}
	
	public int storeCollection(String collectionId, String user, String description, boolean priv, String analysis, String sUsers) throws ExternalServiceFailedException {
        try {
        	return parrotDAO.createCollection(user, collectionId, description, priv, analysis, sUsers);
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
    }

	public int storeDocument(String documentName, String collection, String user, String description, String analysis, String content, String annotatedContent, String highlightedContent) throws ExternalServiceFailedException {
        try {
        	return parrotDAO.createDocument(collection, user, documentName, description, analysis, content, annotatedContent, highlightedContent);
    	} catch (Exception e) {
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
    }


	public boolean updateDocument(String documentName, String collection, String user, String description, String analysis, String content, String annotatedContent, String highlightedContent) throws ExternalServiceFailedException {
        try {
        	return parrotDAO.updateDocument(documentName, description, analysis, content, annotatedContent, highlightedContent);
    	} catch (Exception e) {
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
    }
	
	public User getUser(String email) throws ExternalServiceFailedException {
        try {
        	return parrotDAO.getUser(email);
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}

	public Collection getCollection(String collectionId) throws ExternalServiceFailedException {
        try {
        	return parrotDAO.getCollection(collectionId);
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}

	public Collection getCollectionByName(String collectionName) {
        try {
        	return parrotDAO.getCollectionByName(collectionName);
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}
	
	public Document getDocument(String documentName, String user) throws ExternalServiceFailedException {
		if(parrotDAO.checkDocumentPermission(null, documentName, user)){
			try {
				return parrotDAO.getDocument(documentName);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ExternalServiceFailedException(e.getMessage());
			}
		}
		throw new ExternalServiceFailedException("The user has not permission to read this document.");
	}
	
	public List<User> listUsers() throws ExternalServiceFailedException {
        try {
        	List<User> list = parrotDAO.getUsers();
        	return list;
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}

	public List<Collection> listCollections(String user) throws ExternalServiceFailedException {
        try {
        	List<Collection> list = parrotDAO.listCollections(user);
        	return list;
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}

	public List<Document> listDocument(String collectionId,String userName, int limit) throws ExternalServiceFailedException {
        try {
        	if(userName==null){
            	List<Document> list = parrotDAO.listDocumentsFromCollection(collectionId, limit);
            	return list;
        	}
        	else{
            	List<Document> list = parrotDAO.listDocumentsFromUser(userName, collectionId, limit);
            	return list;
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}

	public List<Document> listDocumentByName(String collectionName,String userName,int limit) throws ExternalServiceFailedException {
        try {
        	if(userName==null){
            	List<Document> list = parrotDAO.listDocumentsFromCollectionByName(collectionName, limit);
            	return list;
        	}
        	else{
            	List<Document> list = parrotDAO.listDocumentsFromUserByName(userName, collectionName, limit);
            	return list;
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}

	public String getJSONTimelining(String collectionId) throws ExternalServiceFailedException {
        try {
           	String result = parrotDAO.listTimeliningFromCollection(collectionId);
           	return result;
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}

	public boolean checkUser(String user, String password) {
       	return  parrotDAO.checkUser(user,password);
	}

	public boolean existsUser(String user) {
       	return parrotDAO.existsUser(user);
	}


	public boolean checkCollectionPermision(String collectionName, String user) {
       	return parrotDAO.checkCollectionPermission(collectionName,user);
	}

	public boolean checkDocumentPermision(String documentName, String user) {
       	return parrotDAO.checkDocumentPermission(null,documentName,user);
	}

	public boolean updateCollection(String collectionName, String timelining, String geolocalization, String semanticexploration,String clustering, String documents) {
		return parrotDAO.updateCollection(collectionName, timelining, geolocalization, semanticexploration,clustering, documents);
	}

	public void deleteDocumentByName(String documentName, String user) {
		if(parrotDAO.checkDocumentPermission(null, documentName, user)){
	       	parrotDAO.deleteDocumentByName(documentName);
	       	return;
		}
		throw new ExternalServiceFailedException("The user has not permission to delete this document.");
	}

	public void deleteDocumentById(String documentId,String user) {
		if(parrotDAO.checkDocumentPermission(documentId,null, user)){
	       	parrotDAO.deleteDocumentByName(documentId);
	       	return;
		}
		throw new ExternalServiceFailedException("The user has not permission to delete this document.");
	}
	
	public List<NLPModel> getModels(){
		return parrotDAO.getModels();
	}
	
	public boolean addModel(String name, String type, String url, String analysis, String models, String language, String informat, String outformat, String mode){
		return parrotDAO.addModel(name, type, url, analysis, models, language, informat, outformat, mode);
	}

}
