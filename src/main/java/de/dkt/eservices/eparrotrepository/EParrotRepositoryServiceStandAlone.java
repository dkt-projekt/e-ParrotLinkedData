package de.dkt.eservices.eparrotrepository;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
			@RequestHeader(value = "Accept", required = false) String acceptHeader,
			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,

			@RequestHeader(value = "limit", required = false, defaultValue="0") int limit,

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

			@RequestHeader(value = "user", required = false) String user,
			@RequestHeader(value = "limit", required = false, defaultValue="0") int limit,

			@RequestBody(required = false) String postBody) throws Exception {

		try {
			String result = repositoryService.listCollections(user,limit);

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
			@RequestParam(value = "user", required = false) String user,
			@RequestParam(value = "limit", required = false, defaultValue="0") int limit,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			String result = repositoryService.listDocuments(collectionName, user, limit);

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
				result = "The collection "+documentName+" has NOT been created. The process has failed!!!!";
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

			@PathVariable(value = "collection") String collectionId,
			@PathVariable(value = "document") String documentId,
			@RequestParam(value = "limit", required = false, defaultValue="3") int limit,
            @RequestBody(required = false) String postBody) throws Exception {
		try {
			String jsonString = repositoryService.getDocumentOverview(documentId, collectionId, limit);

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", RDFSerialization.JSON.name());
			return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}	
	
//	@RequestMapping(value = "/e-parrot/repository/addDocumentFromString", method = { RequestMethod.POST, RequestMethod.GET })
//	public ResponseEntity<String> addDocumentToTimelining(
//			HttpServletRequest request, 
//			@RequestParam(value = "input", required = false) String input,
//			@RequestParam(value = "i", required = false) String i,
//			@RequestParam(value = "informat", required = false) String informat,
//			@RequestParam(value = "f", required = false) String f,
//			@RequestParam(value = "outformat", required = false) String outformat,
//			@RequestParam(value = "o", required = false) String o,
//			@RequestParam(value = "prefix", required = false) String prefix,
//			@RequestParam(value = "p", required = false) String p,
//			@RequestHeader(value = "Accept", required = false) String acceptHeader,
//			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
//
//			@RequestParam(value = "repositoryName", required = false) String repositoryName,
//			@RequestParam(value = "repositoryPath", required = false) String repositoryPath,
//			@RequestParam(value = "repositoryCreate", required = false) boolean repositoryCreate,
//			@RequestHeader(value = "repositoryAddElements", required = false) boolean repositoryAddElements,
//			@RequestParam(value = "language", required = false) String language,
//			@RequestParam(value = "private", required = false) boolean priv,
//			@RequestParam(value = "users", required = false) String sUsers,
//			@RequestParam(value = "passwords", required = false) String sPasswords,
//
//            @RequestBody(required = false) String postBody) throws Exception {
//
//		String users[] = sUsers.split(";");
//		String passwords[] = sPasswords.split(";");
//		if(!repositoryCreate){
//			//TODO Check users
//			if(users.length!=1 || passwords.length!=1){
//				logger.error("User and Password must have the same length (1 in case of not creating the index) [WARNING NOTE: neither user nor password can contain ';']");
//				throw new IllegalAccessException("User and Password must have the same length (1 in case of not creating the index) [WARNING NOTE: neither user nor password can contain ';']");
//			}
//			if(!UserAuthentication.authenticateUser(users[0], passwords[0], "file", "parrotRepository", repositoryName, false)){
//				logger.error("User ["+users[0]+"] is not allow to use the index ["+repositoryName+"] ");
//				throw new IllegalAccessException("User ["+users[0]+"] is not allow to use the index ["+repositoryName+"] ");
//			}
//		}
//		ParameterChecker.checkNotNullOrEmpty(repositoryName, "repository name", logger);
//
//		if (input == null) {
//			input = i;
//		}
//		if (informat == null) {
//			informat = f;
//		}
//		if (outformat == null) {
//			outformat = o;
//		}
//		if (prefix == null) {
//			prefix = p;
//		}
//    	
//		NIFParameterSet parameters = this.normalizeNif(input, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix);
//
//		String plaintext = null;
//		Model inputModel = ModelFactory.createDefaultModel();
//
//		if (!parameters.getInformat().equals(RDFConstants.RDFSerialization.PLAINTEXT)) {
//			try {
//				inputModel = this.unserializeNif(parameters.getInput(),parameters.getInformat());
//			} catch (Exception e) {
//				logger.error("BAD REQUEST: error parsing NIF input", e);
//				throw new BadRequestException("Error parsing NIF input");
//			}
//		} else {
//			// input is plaintext
//			plaintext = parameters.getInput();
//			getRdfConversionService().plaintextToRDF(inputModel, plaintext,language, parameters.getPrefix());
//		}
//		try {
//			
//			String openNLPAnalysisType = "ner;date";
//			String sesameStorageName = "sesame";
//			String semaseStoragePath = repositoryPath+repositoryName+File.separator+"sesameStorage" + File.separator;
//			boolean sesameStorageCreate = repositoryCreate;
//			String luceneFields = "all";
//			String luceneAnalyzers = "standard";
//			String luceneIndexName = "lucene";
//			String luceneIndexPath = repositoryPath+repositoryName+File.separator+"luceneIndexes" + File.separator;
//			boolean luceneIndexCreate = repositoryCreate;
//			String timeliningName = "timelining";
//			String timeliningPath = repositoryPath+repositoryName+File.separator+"timelining" + File.separator;
//			boolean timeliningCreate = repositoryCreate;
//			boolean addElements = repositoryAddElements;
//			
//			//Classification information
//			
//			//Clustering information
//			
//			//
//
//			String nifDocument = repositoryService.addDocument(input, informat, language, 
//					openNLPAnalysisType, 
//					sesameStorageName, semaseStoragePath, sesameStorageCreate, 
//					luceneFields, luceneAnalyzers, luceneIndexName, luceneIndexPath, luceneIndexCreate, 
//					timeliningName, timeliningPath, timeliningCreate, addElements);
//
//			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.TURTLE.name());
//			return new ResponseEntity<String>(nifDocument, responseHeaders, HttpStatus.OK);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			throw e;
//		}
//	}
//
//	@RequestMapping(value = "/e-timelining/processQuery", method = { RequestMethod.POST, RequestMethod.GET })
//	public ResponseEntity<String> processQuery(
//			@RequestParam(value = "input", required = false) String input,
//			@RequestParam(value = "i", required = false) String i,
//			@RequestParam(value = "informat", required = false) String informat,
//			@RequestParam(value = "f", required = false) String f,
//			@RequestParam(value = "outformat", required = false) String outformat,
//			@RequestParam(value = "o", required = false) String o,
//			@RequestParam(value = "prefix", required = false) String prefix,
//			@RequestParam(value = "p", required = false) String p,
//			@RequestHeader(value = "Accept", required = false) String acceptHeader,
//			@RequestHeader(value = "Content-Type", required = false) String contentTypeHeader,
//
//			@RequestParam(value = "repositoryName", required = false) String repositoryName,
//			@RequestParam(value = "repositoryPath", required = false) String repositoryPath,
//			@RequestHeader(value = "repositoryAddElements", required = false) boolean repositoryReturnElements,
//			@RequestParam(value = "language", required = false) String language,
//			@RequestParam(value = "users", required = false) String sUser,
//			@RequestParam(value = "passwords", required = false) String sPassword,
//
//            @RequestBody(required = false) String postBody) throws Exception {
//
//		if(!UserAuthentication.authenticateUser(sUser, sPassword, "file", "parrotRepository", repositoryName, false)){
//			logger.error("User ["+sUser+"] is not allow to use the index ["+repositoryName+"] ");
//			throw new IllegalAccessException("User ["+sUser+"] is not allow to use the index ["+repositoryName+"] ");
//		}
//
//		if (input == null) {
//			input = i;
//		}
//		if (informat == null) {
//			informat = f;
//		}
//		if (outformat == null) {
//			outformat = o;
//		}
//		if (prefix == null) {
//			prefix = p;
//		}
//		
//		ParameterChecker.checkNotNullOrEmpty(input, "input", logger);
//		ParameterChecker.checkNotNullOrEmpty(repositoryName, "repository name", logger);
//		ParameterChecker.checkNotNullOrEmpty(repositoryPath, "repository path", logger);
//
//		NIFParameterSet parameters = this.normalizeNif(input, informat,
//				outformat, postBody, acceptHeader, contentTypeHeader, prefix);
//
//		String plaintext = null;
//		Model inputModel = ModelFactory.createDefaultModel();
//
//		if (!parameters.getInformat().equals(RDFConstants.RDFSerialization.PLAINTEXT)) {
//			try {
//				inputModel = this.unserializeNif(parameters.getInput(),parameters.getInformat());
//			} catch (Exception e) {
//				logger.error("BAD REQUEST: error parsing NIF input", e);
//				throw new BadRequestException("Error parsing NIF input");
//			}
//		} else {
//			// input is plaintext
//			plaintext = parameters.getInput();
//			getRdfConversionService().plaintextToRDF(inputModel, plaintext, language, parameters.getPrefix());
//		}
//		try {
//			String openNLPAnalysisType = "ner;date";
//			String sesameStorageName = "sesame1";
//			String semaseStoragePath = repositoryPath+repositoryName+File.separator+"sesameStorage" + File.separator;
//			String luceneFields = "all";
//			String luceneAnalyzers = "standard";
//			String luceneIndexName = "lucene1";
//			String luceneIndexPath = repositoryPath+repositoryName+File.separator+"luceneIndexes" + File.separator;
//			String timeliningName = "timelining1";
//			String timeliningPath = repositoryPath+repositoryName+File.separator+"timelining" + File.separator;
//			boolean returnElements = repositoryReturnElements;
//
//			JSONObject obj = repositoryService.processQuery(input, informat, language, 
//					openNLPAnalysisType, 
//					sesameStorageName, semaseStoragePath, 
//					luceneFields, luceneAnalyzers, luceneIndexName, luceneIndexPath, 
//					timeliningName, timeliningPath, returnElements);
//
//			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.JSON.contentType());
//			return new ResponseEntity<String>(obj.toString(), responseHeaders, HttpStatus.OK);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			throw e;
//		}
//	}
//
//    public static void main(String[] args) {
//		Model responseModel = null;
//		try {
//			String input = "{\"results\":{\"documents\":{\"document1000\":{\"score\":0.028767451643943787,\"docId\":1,\"content\":\"1936\n\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\n\"},\"document1\":{\"score\":0.028767451643943787,\"docId\":1,\"content\":\"1936\n\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\n\"}},\"numberResults\":1,\"querytext\":\"content:sanjurjo\"}}";
//			ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes());
//			responseModel = ModelFactory.createDefaultModel();
//			responseModel.read(bis, "JSON");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
    
}
