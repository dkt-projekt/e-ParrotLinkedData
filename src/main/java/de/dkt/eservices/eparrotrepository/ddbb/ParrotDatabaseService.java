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
	
	public Document getDocument(String documentName) throws ExternalServiceFailedException {
        try {
        	return parrotDAO.getDocument(documentName);
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
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

	public List<Document> listDocument(String collectionId,String userName) throws ExternalServiceFailedException {
        try {
        	if(userName==null){
            	List<Document> list = parrotDAO.listDocumentsFromCollection(collectionId);
            	return list;
        	}
        	else{
            	List<Document> list = parrotDAO.listDocumentsFromUser(userName, collectionId);
            	return list;
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
        	logger.error(e.getMessage());
    		throw new ExternalServiceFailedException(e.getMessage());
    	}
	}

	public List<Document> listDocumentByName(String collectionName,String userName) throws ExternalServiceFailedException {
        try {
        	if(userName==null){
            	List<Document> list = parrotDAO.listDocumentsFromCollectionByName(collectionName);
            	return list;
        	}
        	else{
            	List<Document> list = parrotDAO.listDocumentsFromUserByName(userName, collectionName);
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

	public boolean updateCollection(String collectionName, String timelining, String geolocalization, String semanticexploration,String clustering, String documents) {
		return parrotDAO.updateCollection(collectionName, timelining, geolocalization, semanticexploration,clustering, documents);
	}

	public void deleteDocumentByName(String documentName) {
       	parrotDAO.deleteDocumentByName(documentName);
       	return;
	}

	public void deleteDocumentById(String documentId) {
       	parrotDAO.deleteDocumentByName(documentId);
       	return;
	}

}
