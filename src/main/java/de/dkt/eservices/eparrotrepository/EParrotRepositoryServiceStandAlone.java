package de.dkt.eservices.eparrotrepository;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.rest.BaseRestController;
import eu.freme.common.rest.NIFParameterSet;
import scala.collection.parallel.ParIterableLike.Foreach;

@RestController
public class EParrotRepositoryServiceStandAlone extends BaseRestController{

	Logger logger = Logger.getLogger(EParrotRepositoryServiceStandAlone.class);
	
	@Autowired
	EParrotRepositoryService repositoryService;
	
	@Autowired
	RDFConversionService rdfConversionService;

	@RequestMapping(value = "/e-parrot/repository/testURL", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> testURL(
			@RequestParam(value = "preffix", required = false) String preffix,
            @RequestBody(required = false) String postBody) throws Exception {
    	HttpHeaders responseHeaders = new HttpHeaders();
    	responseHeaders.add("Content-Type", "text/plain");
    	ResponseEntity<String> response = new ResponseEntity<String>("The restcontroller is working properly", responseHeaders, HttpStatus.OK);
    	return response;
	}


	@RequestMapping(value = "/e-parrot/checkUser", method = { RequestMethod.POST, RequestMethod.GET})
 	public ResponseEntity<String> checkUser(
			HttpServletRequest request, 
			@RequestParam(value = "user", required = false) String user,
			@RequestParam(value = "password", required = false) String password,
			@RequestBody(required = false) String postBody) throws Exception {
		try {
			String result = "false";
			if(user==null || password==null){
			}
			else{
				result = repositoryService.checkUser(user,password) + "";
			}
			//responseHeaders.add("Content-Type", RDFSerialization.JSON.name());
//			System.out.println("DEBUG2: "+result);
			HttpHeaders responseHeaders = new HttpHeaders();
			ResponseEntity<String> res = new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK); 
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping(value = "/e-parrot/existsUser", method = { RequestMethod.POST})
 	public ResponseEntity<String> existsUser(
			HttpServletRequest request, 
			@RequestParam(value = "user", required = false) String user,
			@RequestBody(required = false) String postBody) throws Exception {
		try {
			String result = "false";
			if(user!=null){
				result = repositoryService.existsUser(user) + "";
			}
//			System.out.println("DEBUG: "+result);
			//responseHeaders.add("Content-Type", RDFSerialization.JSON.name());
//			System.out.println("DEBUG2: "+result);
			HttpHeaders responseHeaders = new HttpHeaders();
			ResponseEntity<String> res = new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK); 
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping(value = "/e-parrot/registerUser", method = { RequestMethod.POST})
 	public ResponseEntity<String> registerUser(
			HttpServletRequest request, 
			@RequestParam(value = "user", required = false) String user,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "name", required = false) String name,
			@RequestBody(required = false) String postBody) throws Exception {
		try {
			String result = "false";
			if(user!=null && !user.equalsIgnoreCase("") && 
					password!=null && !password.equalsIgnoreCase("") && 
					name!=null && !name.equalsIgnoreCase("")){
				int i = repositoryService.createUser(user, password, name, "normal", "dktproject@gmail.com", "dktproject2016");
				if(i>0){
					result="true";
				}
			}
			HttpHeaders responseHeaders = new HttpHeaders();
			ResponseEntity<String> res = new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK); 
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping(value = "/e-parrot/listUsers", method = { RequestMethod.POST, RequestMethod.GET })
 	public ResponseEntity<String> listUsers(
			HttpServletRequest request, 
			@RequestParam(value = "input", required = false) String input,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "informat", required = false) String informat,
			@RequestParam(value = "f", required = false) String f,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestParam(value = "o", required = false) String o,
			@RequestParam(value = "prefix", required = false) String prefix,
			@RequestParam(value = "p", required = false) String p,

			@RequestParam(value = "limit", required = false, defaultValue="0") int limit,

			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
			@RequestBody(required = false) String postBody) throws Exception {

		try {
			String result = repositoryService.listUsers(limit);

			HttpHeaders responseHeaders = new HttpHeaders();
//			System.out.println("DEBUG: "+result);
			//responseHeaders.add("Content-Type", RDFSerialization.JSON.name());
//			System.out.println("DEBUG2: "+result);
			ResponseEntity<String> res = new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK); 
//			System.out.println("DEBUG3: "+result);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw e;
		}
	}

	@RequestMapping(value = "/e-parrot/listCollections", method = { RequestMethod.POST, RequestMethod.GET })
 	public ResponseEntity<String> listCollections(
			HttpServletRequest request, 
			@RequestParam(value = "input", required = false) String input,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "informat", required = false) String informat,
			@RequestParam(value = "f", required = false) String f,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestParam(value = "o", required = false) String o,
			@RequestParam(value = "prefix", required = false) String prefix,
			@RequestParam(value = "p", required = false) String p,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,

			@RequestParam(value = "user", required = false) String user,
			@RequestParam(value = "limit", required = false, defaultValue="0") int limit,
			@RequestParam(value = "collectionId", required = false) String collectionId,

			@RequestBody(required = false) String postBody) throws Exception {

		try {
			String result=null;
			if(collectionId==null){
				result = repositoryService.listCollections(user,limit);
			}
			else{
				JSONObject json = repositoryService.listCollectionsJSON(user, limit);

			    try{
			    	JSONObject collections = json.getJSONObject("collections");
			    	JSONObject newCollections = new JSONObject();

			    	Set<String> keys = collections.keySet();
			    	for (String k : keys) {
						JSONObject col = collections.getJSONObject(k);
					    int colId=col.getInt("collectionId");
					    if(collectionId.equalsIgnoreCase(colId+"")){
					    	
						    newCollections.put(k,col);
						    
					    }
					}
			    	JSONObject resultJSON = new JSONObject();
			    	resultJSON.put("collections", newCollections);
			    	result = resultJSON.toString();
				}
				catch(Exception e){
					//e.printStackTrace();
					result = "{}";
				}
			}
			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.PLAINTEXT.name());
			return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	@RequestMapping(value = "/e-parrot/createUser", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> createUser(
			HttpServletRequest request, 
			@RequestParam(value = "newUser", required = false) String newUser,
			@RequestParam(value = "newPassword", required = false) String newPassword,
			@RequestParam(value = "newUserName", required = false) String newUserName,
			@RequestParam(value = "newUserRole", required = false) String newUserRole,
			@RequestParam(value = "user", required = false) String user,
			@RequestParam(value = "password", required = false) String password,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			String result = "";
			int userId = repositoryService.createUser(newUser,newPassword,newUserName,newUserRole,user,password);
			if(userId>0){//priv, sUsers, sPasswords)){
				result = "The user "+newUser+" [with Id="+userId+"] has been successfully created!!";
			}
			else{
				result = "The user "+newUser+" has NOT been created. The process has failed!!!!";
			}
			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.PLAINTEXT.name());
			return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	@RequestMapping(value = "/e-parrot/createCollection", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> createCollection(
			HttpServletRequest request, 
			@RequestParam(value = "collectionName", required = false) String collectionName,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "private", required = false) boolean priv,
			@RequestParam(value = "user", required = false) String user,
			@RequestParam(value = "analysis", required = false) String analysis,
			@RequestParam(value = "users", required = false) String sUsers,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			String result = "";
			int collectionId = repositoryService.createCollection(collectionName, description, user, priv, analysis, sUsers); 
			if(collectionId>0){//priv, sUsers, sPasswords)){
				result = "The collection "+collectionName+" [with Id="+collectionId+"] has been successfully created!!";
			}
			else{
				result = "The collection "+collectionName+" has NOT been created. The process has failed!!!!";
			}

			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.PLAINTEXT.name());
			return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	
	@RequestMapping(value = "/e-parrot/{collection}/overview", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> getCollectionOverview(
			HttpServletRequest request, 
			@PathVariable(value = "collection") String collectionName,
			@RequestParam(value = "limit", required = false, defaultValue="3") int limit,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			String jsonString = repositoryService.getCollectionOverview(collectionName, limit);

			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.JSON.name());
			return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}	
	
	@RequestMapping(value = "/e-parrot/{collection}/listDocuments", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> listDocumentsFromCollection(
			HttpServletRequest request, 
			@PathVariable(value = "collection") String collectionName,
			@RequestParam(value = "user", required = false) String userName,
			@RequestParam(value = "limit", required = false, defaultValue="0") int limit,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			String result = repositoryService.listDocuments(collectionName, userName, limit);

			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.JSON.name());
			return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping(value = "/e-parrot/{collection}/addDocument", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> addDocumentToCollection(
			HttpServletRequest request, 
			@RequestParam(value = "input", required = false) String input,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "informat", required = false) String informat,
			@RequestParam(value = "f", required = false) String f,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestParam(value = "o", required = false) String o,
			@RequestParam(value = "prefix", required = false) String prefix,
			@RequestParam(value = "p", required = false) String p,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,

			@RequestParam(value = "language", required = false) String language,

			@PathVariable(value = "collection") String collectionName,
			@RequestParam(value = "documentName", required = false) String documentName,
			@RequestParam(value = "documentDescription", required = false) String documentDescription,
			@RequestParam(value = "user", required = false) String user,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "analysis", required = false) String analysis,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			if(input==null){
				input=postBody;
				if(input==null){
	            	logger.error("No text to process.");
	                throw new BadRequestException("No text to process.");
				}
			}
	        NIFParameterSet nifParameters = this.normalizeNif(input, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix);
	        Model inModel = ModelFactory.createDefaultModel();

	        if (nifParameters.getInformat().equals(RDFConstants.RDFSerialization.PLAINTEXT)) {
	            rdfConversionService.plaintextToRDF(inModel, nifParameters.getInput(),language, nifParameters.getPrefix());
	        } else {
	            inModel = rdfConversionService.unserializeRDF(nifParameters.getInput(), nifParameters.getInformat());
	        }
	        
	        
			String result = "";
			int documentId = repositoryService.addDocumentToCollection(collectionName, user, documentName, documentDescription, 
					contentTypeHeader, rdfConversionService.serializeRDF(inModel, RDFSerialization.TURTLE), analysis);
			if(documentId>0){
				result = "The document "+documentName+" [with Id="+documentId+"] has been successfully created!!";
			}
			else{
				result = "The document "+documentName+" has NOT been created. The process has failed!!!!";
			}
			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.PLAINTEXT.name());
			return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	
	@RequestMapping(value = "/e-parrot/{collection}/{document}/overview", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> getDocumentOverview(
			HttpServletRequest request, 
			@PathVariable(value = "collection") String collectionName,
			@PathVariable(value = "document") String documentName,

			@RequestParam(value = "input", required = false) String input,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "informat", required = false) String informat,
			@RequestParam(value = "f", required = false) String f,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestParam(value = "o", required = false) String o,
			@RequestParam(value = "prefix", required = false) String prefix,
			@RequestParam(value = "p", required = false) String p,
			@RequestParam(value = "user", required=false) String user,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			String jsonString = repositoryService.getDocumentOverview(documentName, collectionName, user);

			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.JSON.name());
			return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}	

	
	@RequestMapping(value = "/e-parrot/{collection}/{document}/update", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> updateDocument(
			HttpServletRequest request, 
			@RequestParam(value = "input", required = false) String input,
			@RequestParam(value = "i", required = false) String i,
			@RequestParam(value = "informat", required = false) String informat,
			@RequestParam(value = "f", required = false) String f,
			@RequestParam(value = "outformat", required = false) String outformat,
			@RequestParam(value = "o", required = false) String o,
			@RequestParam(value = "prefix", required = false) String prefix,
			@RequestParam(value = "p", required = false) String p,
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,

			@RequestParam(value = "language", required = false) String language,

			@PathVariable(value = "collection") String collectionName,
			@PathVariable(value = "document") String documentName,
			@RequestParam(value = "documentDescription", required = false) String documentDescription,
			@RequestParam(value = "user", required = false) String user,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "analysis", required = false) String analysis,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			if(input==null){
				input=postBody;
				if(input==null){
	            	logger.error("No text to process.");
	                throw new BadRequestException("No text to process.");
				}
			}
	        NIFParameterSet nifParameters = this.normalizeNif(input, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix);
	        Model inModel = ModelFactory.createDefaultModel();

	        if (nifParameters.getInformat().equals(RDFConstants.RDFSerialization.PLAINTEXT)) {
	            rdfConversionService.plaintextToRDF(inModel, nifParameters.getInput(),language, nifParameters.getPrefix());
	        } else {
	            inModel = rdfConversionService.unserializeRDF(nifParameters.getInput(), nifParameters.getInformat());
	        }
	        
			String result = "";
			boolean done = repositoryService.updateDocument(collectionName, user, documentName, documentDescription, 
					contentTypeHeader, rdfConversionService.serializeRDF(inModel, RDFSerialization.TURTLE), analysis);
			if(done){
				result = "The document "+documentName+" has been successfully updated!!";
			}
			else{
				result = "The document "+documentName+" has NOT been updated. The process has failed!!!!";
			}
			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.PLAINTEXT.name());
			return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	@RequestMapping(value = "/e-parrot/{collection}/deleteDocument", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> deleteDocumentFromCollection(
			HttpServletRequest request, 
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,

			@PathVariable(value = "collection") String collectionName,
			@RequestParam(value = "documentName", required = false) String documentName,
			@RequestParam(value = "documentId", required = false) String documentId,
			@RequestParam(value = "user", required = false) String user,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			String result = null;
			if(documentId!=null){
				repositoryService.deleteDocumentById(documentId,user);
				result = "The document "+documentId+" has been succesfully deleted.";
			}
			else if(documentName!=null){
				repositoryService.deleteDocumentByName(documentName,user);
				result = "The document "+documentName+" has been succesfully deleted.";
			}
			else{
            	logger.error("No document identifier provided.");
                throw new BadRequestException("No document identifier provided.");
			}
			HttpHeaders responseHeaders = new HttpHeaders();
			return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
    
	
	@RequestMapping(value = "/e-parrot/models/add", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> addModel(
			HttpServletRequest request, 
			@RequestParam(value = "modelName", required = false) String modelName,
			@RequestParam(value = "modelType", required = false) String modelType,
			@RequestParam(value = "modelInformat", required = false) String modelInformat,
			@RequestParam(value = "modelOutformat", required = false) String modelOutformat,
			@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "analysis", required = false) String analysis,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "models", required = false) String models,
			@RequestParam(value = "mode", required = false) String mode,
			@RequestParam(value = "content", required = false) String content,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			if(!modelType.equalsIgnoreCase("ner") && !modelType.equalsIgnoreCase("dict") && !modelType.equalsIgnoreCase("translate") && !modelType.equalsIgnoreCase("temp") ){
            	logger.error("Model Type is not valid.");
                throw new BadRequestException("Model Type is not valid.");
			}
			if(modelInformat==null){
				modelInformat = "turtle";
			}
			if(modelOutformat==null){
				modelOutformat = "turtle";
			}
			if(url==null){
				url = "http://dev.digitale-kuratierung.de/api/namedEntityRecognition";
			}

			String result = "";
			if(repositoryService.addModel(modelName, modelType, url, analysis, models, language, modelInformat, modelOutformat, mode, content)){
				result = "The model "+modelName+" has been succesfully created.";
			}
			else{
				result = "The model "+modelName+" has been succesfully created.";
			}
			HttpHeaders responseHeaders = new HttpHeaders();
			return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping(value = "/e-parrot/models/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<String> getModelsList(
			HttpServletRequest request, 
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			String result = repositoryService.getModels();
			HttpHeaders responseHeaders = new HttpHeaders();
			return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
}
