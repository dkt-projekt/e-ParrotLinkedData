package de.dkt.eservices.eparrotrepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.activemq.filter.function.replaceFunction;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.crsh.shell.impl.command.system.repl;
import org.hibernate.metamodel.relational.CheckConstraint;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import de.dkt.common.niftools.NIFReader;
import de.dkt.eservices.eparrotrepository.ddbb.Collection;
import de.dkt.eservices.eparrotrepository.ddbb.Document;
import de.dkt.eservices.eparrotrepository.ddbb.NLPModel;
import de.dkt.eservices.eparrotrepository.ddbb.ParrotDatabaseService;
import de.dkt.eservices.eparrotrepository.ddbb.User;
import de.dkt.eservices.eparrotrepository.geolocalization.Geolocalization;
import de.dkt.eservices.eparrotrepository.geolocalization.GeolocalizedElement;
import de.dkt.eservices.eparrotrepository.geolocalization.GeolocalizedElement.GeoElementType;
import de.dkt.eservices.eparrotrepository.geolocalization.Geolocation;
import de.dkt.eservices.eparrotrepository.semanticexploration.SemanticEntity;
import de.dkt.eservices.eparrotrepository.timelining.TimeExpressionRange;
import de.dkt.eservices.eparrotrepository.timelining.TimelinedElement;
import de.dkt.eservices.eparrotrepository.timelining.TimelinedElement.TimeElementType;
import de.dkt.eservices.eparrotrepository.timelining.Timelining;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 *
 */
@Component
public class EParrotRepositoryService {

	Logger logger = Logger.getLogger(EParrotRepositoryService.class);

	String repositoriesDirectory = "parrot/repositories/";

	@Autowired
	ParrotDatabaseService databaseService;
    
	HashMap<String, HashMap<String,String>> map;
	
	int collectionOverviewLimit = 3;
	
//	@Autowired
	public EParrotRepositoryService() {
//		initializeModels();
	}
	
	@PostConstruct
	public void initializeModels(){
		map = new HashMap<String, HashMap<String,String>>();
		String baseUrl = "http://dev.digitale-kuratierung.de/api";
		
		List<NLPModel> nlpmodels = databaseService.getModels();
		
		for (NLPModel m : nlpmodels) {
			HashMap<String,String> mapaux = new HashMap<String,String>();
			mapaux.put("url", baseUrl + m.getUrl());
			String type = m.getType();			
			mapaux.put("type", type);
			mapaux.put("informat", m.getInformat());
			mapaux.put("outformat", m.getOutformat());
			if(type.equalsIgnoreCase("ner")){
				mapaux.put("analysis", m.getAnalysis());
				mapaux.put("language", m.getLanguage());
				mapaux.put("models", m.getModels());
				mapaux.put("mode", m.getMode());
			}
			else if(type.equalsIgnoreCase("dict")){
				mapaux.put("analysis", m.getAnalysis());
				mapaux.put("language", m.getLanguage());
				mapaux.put("models", m.getModels());
				mapaux.put("mode", m.getMode());
			}
			else if(type.equalsIgnoreCase("timex")){
				mapaux.put("analysis", m.getAnalysis());
				mapaux.put("language", m.getLanguage());
				mapaux.put("models", m.getModels());
				mapaux.put("mode", m.getMode());
			}
			else if(type.equalsIgnoreCase("translate")){
//				mapaux.put("analysis", m.getAnalysis());
				mapaux.put("source-lang", m.getLanguage());
				mapaux.put("target-lang", m.getModels());
				mapaux.put("Content-type", m.getMode());
			}
			map.put(m.getModelName(), mapaux);
		}
		
/*		HashMap<String,String> mapaux = new HashMap<String,String>();
		mapaux.put("url", baseUrl + "/e-nlp/namedEntityRecognition");
		mapaux.put("analysis", "ner");
		mapaux.put("language", "en");
		mapaux.put("models", "ner-wikinerEn_LOC");
		mapaux.put("informat", "turtle");
		mapaux.put("outformat", "turtle");
		mapaux.put("mode", "spot");
		map.put("ner_LOC_en", mapaux);

		mapaux = new HashMap<String,String>();
		mapaux.put("url", baseUrl + "/e-nlp/namedEntityRecognition");
		mapaux.put("analysis", "ner");
		mapaux.put("language", "en");
		mapaux.put("models", "ner-wikinerEn_PER");
		mapaux.put("informat", "turtle");
		mapaux.put("outformat", "turtle");
		mapaux.put("mode", "spot");
		map.put("ner_PER_en", mapaux);

		mapaux = new HashMap<String,String>();
		mapaux.put("url", baseUrl + "/e-nlp/namedEntityRecognition");
		mapaux.put("analysis", "ner");
		mapaux.put("language", "de");
		mapaux.put("models", "ner-de_aij-wikinerTrainLOC");
		mapaux.put("informat", "turtle");
		mapaux.put("outformat", "turtle");
		mapaux.put("mode", "spot");
		map.put("ner_LOC_de", mapaux);
		
		mapaux = new HashMap<String,String>();
		mapaux.put("url", baseUrl + "/e-nlp/namedEntityRecognition");
		mapaux.put("analysis", "ner");
		mapaux.put("language", "de");
		mapaux.put("models", "ner-de_aij-wikinerTrainPER");
		mapaux.put("informat", "turtle");
		mapaux.put("outformat", "turtle");
		mapaux.put("mode", "spot");
		map.put("ner_PER_de", mapaux);

		mapaux.put("url", baseUrl + "/e-nlp/namedEntityRecognition");
		mapaux.put("analysis", "ner");
		mapaux.put("language", "en");
		mapaux.put("models", "ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC");
		mapaux.put("informat", "turtle");
		mapaux.put("outformat", "turtle");
		mapaux.put("mode", "spot");
		map.put("ner_PER_ORG_LOC_en_spot", mapaux);


		mapaux.put("url", baseUrl + "/e-nlp/namedEntityRecognition");
		mapaux.put("analysis", "ner");
		mapaux.put("language", "en");
		mapaux.put("models", "ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC");
		mapaux.put("informat", "turtle");
		mapaux.put("outformat", "turtle");
		mapaux.put("mode", "link");
		map.put("ner_PER_ORG_LOC_en_link", mapaux);

		mapaux.put("url", baseUrl + "/e-nlp/namedEntityRecognition");
		mapaux.put("analysis", "ner");
		mapaux.put("language", "en");
		mapaux.put("models", "ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC");
		mapaux.put("informat", "turtle");
		mapaux.put("outformat", "turtle");
		mapaux.put("mode", "all");
		map.put("ner_PER_ORG_LOC_en_all", mapaux);

		mapaux.put("url", baseUrl + "/e-nlp/namedEntityRecognition");
		mapaux.put("analysis", "temp");
		mapaux.put("language", "en");
		mapaux.put("models", "englishDates");
		mapaux.put("informat", "turtle");
		mapaux.put("outformat", "turtle");
		map.put("temp_en", mapaux);*/
	}

	public String getUserInformation(String userName, String userPassword){
//		if(databaseService.checkUser(userName, userPassword)){
//			return "{\"errormessage\":\"The user has not permission to access this information\"}";
//		}
		User user = databaseService.getUser(userName);
		return user.getJSONObject().toString();
	}
	

	public String listUsers(int limit){
		return listUsersJSON(limit).toString();
	}
	
	public JSONObject listUsersJSON(int limit){
		List<User> list = databaseService.listUsers();
		List<User> list2 = new LinkedList<User>();
		if(limit>0){
			int counter = 0;
			for (User u: list) {
				if(counter<limit){
					list2.add(u);
					counter++;
				}
			}
			return convertUsersListIntoJSON(list2);
		}
		else{
			return convertUsersListIntoJSON(list);
		}
	}

	public String listCollections(String user,int limit){
		return listCollectionsJSON(user, limit).toString();
	}
	
	public JSONObject listCollectionsJSON(String user,int limit){
		List<Collection> collections = databaseService.listCollections(user);
		List<Collection> list2 = new LinkedList<Collection>();
		if(limit>0){
			int counter = 0;
			for (Collection col: collections) {
				if(counter<limit){
					list2.add(col);
					counter++;
				}
			}
			return convertListIntoJSON(list2);
		}
		else{
			return convertListIntoJSON(collections);
		}
	}

	private JSONObject convertUsersListIntoJSON(List<User> list) {
		JSONObject obj = new JSONObject();
		JSONObject joUsers= new JSONObject();
		int i=0;
		System.out.println(list.size());
		for (User u: list) {
				System.out.println("-------is user:");
				if(u==null){
					System.out.println("u is NULL");
				}
				joUsers.put("user"+(i+1), u.getJSONObject());
			i++;
		}
		if(joUsers.length()>0){
			obj.put("users", joUsers);
		}
		return obj;
	}
	
	private JSONObject convertListIntoJSON(List list) {
		JSONObject obj = new JSONObject();
		JSONObject joCollections = new JSONObject();
		JSONObject joDocuments= new JSONObject();
		JSONObject joUsers= new JSONObject();
		JSONObject joModels= new JSONObject();
		int i=0;
//		System.out.println(list.size());
		for (Object o: list) {
			if(o instanceof User){
//				System.out.println("-------is user:");
				User u = (User) o;
				joUsers.put("user"+(i+1), u.getJSONObject());
			}
			else if(o instanceof Collection){
//				System.out.println("-------is collection:");
				Collection c = (Collection) o;
				joCollections.put("collection"+(i+1), c.getJSONObject());
			}
			else if(o instanceof Document){
//				System.out.println("-------is document:");
				Document d = (Document) o;
				joDocuments.put("document"+(i+1), d.getJSONObject());
			}
			else if(o instanceof NLPModel){
				NLPModel d = (NLPModel) o;
				joModels.put("model"+(i+1), d.getJSONObject());
			}
			else{
				System.out.println("ERROR: element type not supported.");
			}
			i++;
		}
		if(joCollections.length()>0){
			obj.put("collections", joCollections);
		}
		if(joDocuments.length()>0){
			obj.put("documents", joDocuments);
		}
		if(joUsers.length()>0){
			obj.put("users", joUsers);
		}
		if(joModels.length()>0){
			obj.put("models", joModels);
		}
		return obj;		
	}
	
	public int modifyUser(String newUser, String newPassword, String newUserName, String newUserRole, String user, String password) throws ExternalServiceFailedException {
		return databaseService.updateUser(newUser, newPassword, newUserName, newUserRole, user, password);
	}
	
	public int createUser(String newUser, String newPassword, String newUserName, String newUserRole, String user, String password) throws ExternalServiceFailedException {
		return databaseService.storeUser(newUser, newPassword, newUserName, newUserRole, user, password);
	}
	
	public int createCollection(String collectionName, String description, String user, boolean priv, String analysis, String sUsers) throws ExternalServiceFailedException {
		return databaseService.storeCollection(collectionName, user, description, priv, analysis, sUsers);
	}
	
	public String listDocuments(String collectionName,String user,int limit){
		return listDocumentsJSON(collectionName, user, limit).toString();
	}
	
	public JSONObject listDocumentsJSON(String collectionName,String user,int limit){
		List<Document> list = databaseService.listDocumentByName(collectionName, user, limit);
		List<Document> list2 = new LinkedList<Document>();
		if(limit>0){
			int counter = 0;
			for (Document doc: list) {
				if(counter<limit){
					list2.add(doc);
					counter++;
				}
			}
			return convertListIntoJSON(list2);
		}
		else{
			return convertListIntoJSON(list);
		}
	}
	
	public String getCollectionOverview(String collectionName, String userName, int limit){
		return getCollectionOverviewJSON(collectionName, userName, limit).toString();
	}
	
	public JSONObject getCollectionOverviewJSON(String collectionName, String userName, int limit){
		if(databaseService.checkCollectionPermision(collectionName, userName)){
			return new JSONObject("{\"errormessage\":\"The user has not permission to access this collection\"}");
		}
		Collection collection = databaseService.getCollectionByName(collectionName);
		JSONObject result = collection.getJSONObject();
		return result;
	}

	public String getCollectionTimelining(String collectionName, String userName, int limit){
		if(!databaseService.checkCollectionPermision(collectionName, userName)){
			return "The user has not permission to access this collection";
		}
		List<Document> docsList = databaseService.listDocumentByName(collectionName, null,0);
		String timelining = doCollectionTimelining(collectionName,docsList,limit);
		return timelining;
	}
	
	public String getCollectionGeolocalization(String collectionName,String userName, int limit){
		if(!databaseService.checkCollectionPermision(collectionName, userName)){
			return "The user has not permission to access this collection";
		}
		List<Document> docsList = databaseService.listDocumentByName(collectionName, null,0);
		String geolocalization = doCollectionGeolocalization(collectionName,docsList,limit);
		return geolocalization;
	}
	
	public String getCollectionSemanticExploration(String collectionName,String userName, int limit){
		if(!databaseService.checkCollectionPermision(collectionName, userName)){
			return "The user has not permission to access this collection";
		}
		List<Document> docsList = databaseService.listDocumentByName(collectionName, null,0);
		String semanticexploration = doCollectionSemanticExploration(collectionName,docsList,limit);
		return semanticexploration;
	}
	
	public String getCollectionClustering(String collectionName,String userName, int limit){
		if(!databaseService.checkCollectionPermision(collectionName, userName)){
			return "The user has not permission to access this collection";
		}
		List<Document> docsList = databaseService.listDocumentByName(collectionName, null,0);
		String clustering = doCollectionClustering(collectionName,docsList,limit);
		return clustering;
	}
	
	public int addDocumentToCollection(String collectionName, String user, String documentName, String documentDescription, String content, String aContent, String analysis) {
		boolean access = databaseService.checkCollectionPermision(collectionName, user);
		if(!access){
			String msg = "User \""+user+"\" has not rights for accessing the collection \""+collectionName+"\"";
			logger.error(msg);
			throw new ExternalServiceFailedException(msg);
		}
//		boolean access2 = databaseService.checkDocumentPermision(documentName, user);
//		if(!access2){
//			String msg = "User \""+user+"\" has not rights for accessing the document \""+documentName+"\"";
//			logger.error(msg);
//			throw new ExternalServiceFailedException(msg);
//		}

		content = plainContent(aContent);
		String annotatedContent= annotateDocument(aContent,analysis);
//		System.out.println(annotatedContent);
		String highlightedContent = highlighText(annotatedContent);
//		System.out.println(highlightedContent);
		
		int docId = databaseService.storeDocument(documentName, collectionName, user, documentDescription, analysis, content, annotatedContent, highlightedContent);
//		System.out.println("Annotated2: " + annotatedContent);
//		System.out.println("Highlighted: " + highlightedContent);

		if(!updateCollection(collectionName, collectionOverviewLimit)){
			logger.error("The collection has not been updated!!");
		}
		return docId;
	}

	public boolean updateDocument(String collectionName, String user, String documentName, String documentDescription, String content, String aContent, String analysis) {
		boolean access2 = databaseService.checkDocumentPermision(collectionName, user);
		if(!access2){
			String msg = "User \""+user+"\" has not rights for accessing the document \""+documentName+"\"";
			logger.error(msg);
			throw new ExternalServiceFailedException(msg);
		}

		content = aContent;
		String annotatedContent= annotateDocument(aContent,analysis);
//		System.out.println(annotatedContent);
		String highlightedContent = highlighText(annotatedContent);
//		System.out.println(highlightedContent);
		
		boolean doc = databaseService.updateDocument(documentName, collectionName, user, documentDescription, analysis, content, annotatedContent, highlightedContent);
		if(!updateCollection(collectionName, collectionOverviewLimit)){
			logger.error("The collection has not been updated!!");
		}
		return doc;
	}

	public String plainContent(String aContent) {
		try{
			Model model = NIFReader.extractModelFromTurtleString(aContent);
			String result = NIFReader.extractIsString(model);
			return result;
		}
		catch(Exception e){
			logger.error("The plainContent cannot be extracted.",e);
			return aContent;
		}
	}

	public String annotateDocument(String aContent, String analysis) {
		String annotatedContent = aContent;
		String [] analysisParts = analysis.split(",");
		try{
			Unirest.setTimeouts(10000, 10000000);
			for (String an : analysisParts) {
				if(map.containsKey(an)){
					HashMap<String,String> tempMap = map.get(an);
	
					try{
						HttpResponse<String> response =null;
						String type = tempMap.get("type");
						if(type.equalsIgnoreCase("ner")){
							response = Unirest.post(tempMap.get("url"))
		//						.queryString("input", annotatedContent)
								.queryString("analysis", tempMap.get("analysis"))
								.queryString("language", tempMap.get("language"))
								.queryString("models", tempMap.get("models"))
								.queryString("informat", tempMap.get("informat"))
								.queryString("outformat", tempMap.get("outformat"))
								.queryString("mode", tempMap.get("mode"))
								.body(annotatedContent)
								.asString();
						}
						else if(type.equalsIgnoreCase("translate")){
							response = Unirest.post(tempMap.get("url"))
		//						.queryString("input", annotatedContent)
								//.queryString("analysis", tempMap.get("analysis"))
								.queryString("source-lang", tempMap.get("source-lang"))
								.queryString("target-lang", tempMap.get("target-lang"))
								.queryString("informat", tempMap.get("informat"))
								.queryString("outformat", tempMap.get("outformat"))
								.queryString("Content-type", tempMap.get("Content-type"))
								.body(annotatedContent)
								.asString();
						}
						else{
							response = Unirest.post(tempMap.get("url"))
		//						.queryString("input", annotatedContent)
								.queryString("analysis", tempMap.get("analysis"))
								.queryString("language", tempMap.get("language"))
								.queryString("models", tempMap.get("models"))
								.queryString("informat", tempMap.get("informat"))
								.queryString("outformat", tempMap.get("outformat"))
								.body(annotatedContent)
								.asString();
						}
						if(response.getStatus() == 200){
							annotatedContent = response.getBody();
						}
						else{
							String msg = "Error at processing the content of the document with model: "+an;
							logger.error(msg);
						}
					}
					catch(Exception e){
						String msg = "Error at processing the content of the document with model: "+an;
						logger.error(msg, e);
						throw new ExternalServiceFailedException(msg);
					}
				}
			}
		}
		catch(Exception e){
			String msg = "Error at processing the content of the document.";
			logger.error(msg, e);
			throw new ExternalServiceFailedException(msg);
		}
		return annotatedContent;
	}

	public boolean updateCollection(String collectionName, int limit){
		List<Document> docsList = databaseService.listDocumentByName(collectionName, null,0);
		
		String timelining = doCollectionTimelining(collectionName,docsList,limit);
		String geolocalization = doCollectionGeolocalization(collectionName,docsList,limit);
		String semanticexploration = doCollectionSemanticExploration(collectionName,docsList,limit);
		String clustering = doCollectionClustering(collectionName,docsList,limit);
		String documents = doCollectionDocumentsList(collectionName,docsList,limit);

		System.out.println("TIMELINING: " + timelining);
		System.out.println("GEO: " + geolocalization);
		System.out.println("Semantic: " + semanticexploration);
		System.out.println("Clustering: " + clustering);
		System.out.println("DOCUMENTS: " + documents);
		return databaseService.updateCollection(collectionName, timelining,geolocalization,semanticexploration,clustering,documents);
	}

	public String doCollectionTimelining(String collectionName, List<Document> docsList, int limit){
		boolean addElements = false;
		try{
			if(limit==0){
				limit=Integer.MAX_VALUE;
			}
			List<TimelinedElement> inputNIFModels = new LinkedList<TimelinedElement>();
			int counter = 0;
			for (Document d : docsList) {
				Model m = NIFReader.extractModelFromFormatString(d.getAnnotatedContent(), RDFSerialization.TURTLE);
				TimeElementType type = TimeElementType.DOCUMENT;
				String uri = NIFReader.extractDocumentURI(m);

				String dateRange = NIFReader.extractMeanDateRange(m);
				System.out.println("-------DEBUG: "+dateRange);
				if(!dateRange.contains("null") && (counter<limit) ){
					TimeExpressionRange temporalExpression = new TimeExpressionRange(dateRange);
					TimelinedElement tle = new TimelinedElement(type, uri, temporalExpression, m);
					inputNIFModels.add(tle);
					counter++;
				}
			}

			List<TimelinedElement> outputNIFModels = Timelining.generateTimelineFromModels(inputNIFModels,addElements);
//			JSONObject outputJSONObject = TimeConversion.generateJSONFromTimelinedElements(outputNIFModels);
			
			String output = ""
			+ "<ul class=\"timeline\">";
			int count = 0;

			for (TimelinedElement timelinedElement : outputNIFModels) {
				String ir = "";

				if(count % 2 == 0){
					ir += "<li>";
				}
				else{
					ir += "<li class=\"timeline-inverted\">";
				}
				ir +=  "<div class=\"timeline-panel\">";
				ir +=  "<div class=\"timeline-heading\">";
				String docId = timelinedElement.uri;
				String text = NIFReader.extractIsString(timelinedElement.model).substring(0, 35);
				String tempExpression = timelinedElement.temporalExpression.toString();
				ir +=  "<span class=\"label label-default\">"+docId+"</span>";
				ir +=  "<span class=\"label label-info\">"+tempExpression+"</span>";
				ir +=  "<small class=\"text-muted\">   "+text+"...</small>";
				ir +=  "</div>";
				ir +=  "</div>";
				ir +=  "</li>";
				output += ir;
				count++;
			}
			output += "</ul>";
			return output;
		}
		catch(Exception e){
			e.printStackTrace();
			String msg = "Error at generating timelinig for collection: "+collectionName;
			logger.error(msg);
			return "";
		}
	}

	public String doCollectionGeolocalization(String collectionName, List<Document> docsList, int limit){
		boolean addElements = false;
		try{
			if(limit==0){
				limit = Integer.MAX_VALUE;
			}
			int counter=0;
			List<GeolocalizedElement> inputNIFModels = new LinkedList<GeolocalizedElement>();
			float meanLat = 0;
			float meanLong = 0;
			for (Document d : docsList) {
				Model m = NIFReader.extractModelFromFormatString(d.getAnnotatedContent(), RDFSerialization.TURTLE);
				GeoElementType type = GeoElementType.DOCUMENT;
				String uri = NIFReader.extractDocumentURI(m);
				String positionRange = NIFReader.extractMeanPositionRange(m);


				System.out.println("--DEBUG: positon: "+positionRange);
				if(!positionRange.contains("null") && (counter<limit) ){
					Geolocation geoExpression = new Geolocation(positionRange);
					GeolocalizedElement gle = new GeolocalizedElement(type, uri, geoExpression, m);
					
					meanLat += Float.parseFloat(gle.geolocationExpression.latitude.toString());
					meanLong += Float.parseFloat(gle.geolocationExpression.longitude.toString());
					inputNIFModels.add(gle);
					counter++;
				}
				
			}
			meanLat = meanLat/counter;
			meanLong = meanLong/counter;
//			List<GeolocalizedElement> outputNIFModels = Geolocalization.generateGeolocalizationFromModels(inputNIFModels,addElements);
			List<GeolocalizedElement> outputNIFModels = inputNIFModels;

			String output = ""
            + "<div class=\"container2\">"
            + "<div id=\"map-place\" style=\"width: 100%;height: 400px;margin: 0;padding: 1px;\"></div>"
            + "</div>"
            + "<script type=\"text/javascript\">"
            + "var mymap = L.map('map-place').setView(["+meanLat+", "+meanLong+"], 8);"

            + "L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {"
            + "    attribution: '&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors'"
            + "}).addTo(mymap);";

			for (GeolocalizedElement gle: outputNIFModels) {
//				output += "["+gle.geolocationExpression.latitude+","+gle.geolocationExpression.longitude+",\""+gle.uri+"\"]";
	            output += "L.marker(["+gle.geolocationExpression.latitude+", "+gle.geolocationExpression.longitude+"]).addTo(mymap)";
	            output += "    .bindPopup('"+gle.uri+"')";
//	            output += "    //.openPopup();";
	            output += "        ;";
			}
			output += "</script>";
			return output;
		}
		catch(Exception e){
			String msg = "Error at generating geolocalization for collection: "+collectionName;
			logger.error(msg);
			return "";
		}	}

	public String doCollectionSemanticExploration(String collectionName, List<Document> docsList, int limit){
		if(limit==0){
			limit=Integer.MAX_VALUE;
		}

		//TODO
		String output = "";
		
		/*
		                                 <ul id="primaryNav" class="col3">
                                        <li id="home"><a href="http://d-nb.info/gnd/11858071X">Eric Mendelsohn</a></li>
                                        <li><a href="http://www.geonames.org/5128581">New York</a>
                                                <ul>
                                                        <li><a href="http://d-nb.info/gnd/128830751">Louise</a></li>
                                                </ul>
                                        </li>
                                        <li><a href="http://d-nb.info/gnd/128830751">Louise</a>
                                                <ul>
                                                        <li><a href="http://www.geonames.org/4115551">Hudson</a>
                                                                <ul>
                                                                        <li><a href="http://www.geonames.org/5391959">San Francisco</a>
                                                                        </li>
                                                                        <li><a href="http://www.geonames.org/5114221">Croton</a></li>
                                                                </ul>
                                                        </li>
                                                </ul>
                                        </li>
                                        <li><a href="http://www.geonames.org/5391959">San Francisco</a>
                                                <ul>
                                                        <li><a href="http://www.geonames.org/4115551">Hudson</a>
                                                                <ul>
                                                                        <li><a href="http://www.geonames.org/5114221">Croton</a>
                                                                        </li>
                                                                </ul>
                                                        </li>
                                                        <li><a href="http://dkt.dfki.de/ontologies/nif#date=19450101000000_19460101000000">1945</a>
                                                        </li>
                                                </ul>
                                        </li>
                                </ul>
		 */
		return output;
	}

	public String doCollectionClustering(String collectionName, List<Document> docsList, int limit){
		if(limit==0){
			limit=Integer.MAX_VALUE;
		}
		HashMap<String, HashMap<SemanticEntity,Integer>> docsMap = new HashMap<String, HashMap<SemanticEntity,Integer>>();
		List<SemanticEntity> entities = new LinkedList<SemanticEntity>();
		
		for (Document d : docsList) {
//			System.out.println(d.getDocumentName());
			List<SemanticEntity> list = new LinkedList<SemanticEntity>();
			HashMap<SemanticEntity,Integer> entitiesMap = new HashMap<SemanticEntity,Integer>();
			Model model;
			try {
				model = NIFReader.extractModelFromFormatString(d.getAnnotatedContent(), RDFSerialization.TURTLE);
			} catch (Exception e) {
				String msg = "Error parsing document in clustering generation in update collection.";
				logger.error(msg,e);
				return null;
			}
			Map<String,Map<String,String>> map = NIFReader.extractEntitiesExtended(model);
			if (!(map.isEmpty())){
				Set<String> keyset = map.keySet();
				for (String k : keyset) {
					Map<String, String> kMap = map.get(k);
					String anchorOf = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#anchorOf";
					String taIdentRef = "http://www.w3.org/2005/11/its/rdf#taIdentRef";
					System.out.println("\t\t" + kMap.get(anchorOf) + " <---> " + kMap.get(taIdentRef));
					SemanticEntity se1 = new SemanticEntity(kMap.get(anchorOf).replace('\n', ' ').replace('\r', ' '),
							kMap.get(taIdentRef));
					if (entitiesMap.containsKey(se1)) {
						entitiesMap.put(se1, entitiesMap.get(se1) + 1);
					} else {
						entitiesMap.put(se1, 1);
					}
					list.add(se1);
				}
			}
			docsMap.put(d.getDocumentName(), entitiesMap);
			for (SemanticEntity se : list) {
				if(!entities.contains(se)){
					entities.add(se);
				}
			}
		}
		
		//Generate headers for the file.
		String arff = "@RELATION "+collectionName+"_Clustering\n";

		arff += "@ATTRIBUTE DOCUMENT_NAME  STRING\n";
		for (SemanticEntity se : entities) {
			arff += "@ATTRIBUTE "+se.text.replace(' ', '_')+"  NUMERIC\n";
		}
		arff += "\n";
		arff += "@DATA\n";
		Set<String> docsKeys = docsMap.keySet();
		for (String dk : docsKeys) {
			String line = dk+"";
			HashMap<SemanticEntity, Integer> eMaps = docsMap.get(dk);
			for (SemanticEntity se : entities) {
				if(eMaps.containsKey(se)){
					line += ","+eMaps.get(se);
				}
				else{
					line += ",0";
				}
			}
			arff += line+"\n";
		}
		System.out.println("-------------------------------");
		System.out.println(arff);
		System.out.println("-------------------------------");
		
		HttpResponse<String> response = null;
		try {
			response = Unirest.post("http://dev.digitale-kuratierung.de/api/e-clustering/generateClusters")
			.queryString("algorithm", "kmeans")
			.queryString("language", "en")
			//.field("file", new File("/tmp/file"))
			.body(arff).asString();
		} catch (Exception e) {
			String msg = "Error at calling the clustering service for collection: "+collectionName;
			logger.error(msg, e);
			return null;
		}
//		System.out.println(response.getStatus());
//		System.out.println(response.getBody());
		String result = "";
		if(response.getStatus() == 200){
			JSONObject responseJSON = new JSONObject(response.getBody());
			
			result += "";
			result += "<div class=\"pricing-table group\">";
			
			HashMap<String, HashMap<String, Double>> resultHash = new HashMap<String, HashMap<String, Double>>();
			
			JSONObject results = responseJSON.getJSONObject("results");
			JSONObject clusters = results.getJSONObject("clusters");
			int numberClusters = Integer.parseInt(results.get("numberClusters").toString());
			if(numberClusters==-1){
				numberClusters=1;
			}
			else{
				if(limit<numberClusters){
					numberClusters=limit;
				}
			}
			int counter = 0;
			for (Object clusterId : clusters.keySet()){
				if(limit==counter){
					break;
				}
				JSONObject cid = clusters.getJSONObject(clusterId.toString());
				JSONObject entitiesLabels = cid.getJSONObject("entities");
				HashMap<String, Double> innerMap = new HashMap<String, Double>();
				
				String color="personal";
				switch (counter%5) {
				case 0:
					color = "business";
					break;
				case 1:
					color = "professional";
					break;
				case 2:
					color = "holidays";
					break;
				case 3:
					color = "meeting";
					break;
				default:
					color = "personal";
					break;
				}
				result += "<div class=\"block "+color+" fl block"+numberClusters+"\">";
				result += "<h2 class=\"title\">"+clusterId.toString()+"</h2>";
				result += "<ul class=\"features\">";
				for (Object entity : entitiesLabels.keySet()){
					JSONObject jEnt = entitiesLabels.getJSONObject(entity.toString());
					Object label = jEnt.get("label");
					Object meanVal = jEnt.get("meanValue");
					innerMap.put(label.toString(), Double.parseDouble(meanVal.toString()));
					
					result += "<li>"+label.toString().replace('_', ' ')+"</li>";
				}
				result += "</ul>";
				result += "<div class=\"pt-footer\">";
				result += "<p><span>_ _ _ </span></p>";
				result += "</div>";
				result += "</div>";
				resultHash.put(clusterId.toString(), innerMap);
				counter++;
			}
			result += "</div>";
			return result;
		}
		else{
			String msg = "Error at generating clustering for collection: "+collectionName;
			logger.error(msg);
			return null;
		}
	}
	
	public String doCollectionDocumentsList(String collectionName, List<Document> docsList, int limit){
		String finalResult = "";
		if(limit==0){
			limit=Integer.MAX_VALUE;
		}
		int counter=0;
		for (Document document : docsList) {
			if(counter>=limit){
				break;
			}
			String high = document.getHighlightedContent();
			if(high.length()>197){
				high = high.substring(0, 197)+"...";
			}
			String ir = "";
			ir += ""
				+ "<div class=\"row\">"
				+ "<div class=\"col-lg-12\">"
				+ "<div class=\"panel panel-default\">"
				+ "<div class=\"panel-heading\">"
				+ "<i class=\"fa fa-bar-chart-o fa-fw\"></i> "+document.getDocumentName()
				+ "</div>"
				+ "<div class=\"panel-body\">"
				+ "<div class=\"col-lg-12 col-md-6\">"
				+ "<div class=\"row\">"
//				+ "<blockquote>"
				+ "<p>"+high+"</p>"
//				+ "</blockquote>"
				+ "</div>"
//				+ "<div class=\"row\">"
//				+ "<span class=\"label label-default\">Other</span>"
//				+ "<span class=\"label label-primary\">Temporal Expressions</span>"
//				+ "<span class=\"label label-success\">Person</span>"
//				+ "<span class=\"label label-info\">Organization</span>"
//				+ "<span class=\"label label-warning\">Location</span>"
//				//+ "<span class=\"label label-danger\"></span>"
//				+ "</div>"
				+ "</div> </div> </div> </div></div>";
			finalResult += ir;
			counter++;
		}
		return finalResult;
	}

	public String highlighText(String ac){
		try{
			String high = "";
			Model jena = NIFReader.extractModelFromFormatString(ac, RDFSerialization.TURTLE);
			
			String anno = NIFReader.extractIsString(jena);
			//Get all the annotated information that we want to use for highlighting. 
			Map<String,Map<String,String>> map = NIFReader.extractEntitiesExtended(jena);
			
			LinkedList<Map<String,String>> list = new LinkedList<Map<String,String>>();

			String initTag = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#beginIndex";
			String endTag = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#endIndex";
			String typeTag = "http://www.w3.org/2005/11/its/rdf#taClassRef";

			if (!(map.isEmpty())){
				Set<String> keys = map.keySet();
				for (String k : keys) {
					// System.out.println("Key: "+k);
					Map<String, String> internalMap = map.get(k);

					int init = Integer.parseInt(internalMap.get(initTag));
					// int end = Integer.parseInt(internalMap.get(endTag));
					boolean added = false;

					for (int i = 0; i < list.size() && !added; i++) {
						Map<String, String> mapL = list.get(i);
						int auxInit = Integer.parseInt(mapL.get(initTag));
						if (init < auxInit) {
							added = true;
							list.add(i, internalMap);
						}
					}

					if (!added) {
						list.add(internalMap);
					}
					// Set<String> kes2 = internalMap.keySet();
					// for (String k2 : kes2) {
					// System.out.println("\t" + k2 + " <--> " +
					// internalMap.get(k2));
					// }
				}
			}
			
			int offset = 0;
			for (Map<String,String> mm : list) {
				int init = Integer.parseInt(mm.get(initTag));
				int end = Integer.parseInt(mm.get(endTag));
				String type = mm.get(typeTag);
				String label = "";
				if(type.contains("Location")){
					label = "label-warning";
				}
				else if(type.contains("Organisation")){
					label = "label-info";
				}
				else if(type.contains("Person")){
					label = "label-success";
				}
				else if(type.contains("TemporalEntity")){
					label = "label-primary";
				}
				else{
					label = "label-default";
				}

//				System.out.println("\toffset: "+offset+" INIT: "+init+" END: "+end+"  type:"+type);
				
				if(offset>init){
					high = high + "(<span class=\"label "+label+"\">";
					high = high + anno.substring(init, end);
					high = high + "</span>)";

					//TODO Consider painting when the ending is longer than the previous clashing endind.
				}
				else{
					high = high + anno.substring(offset, init);
					high = high + "<span class=\"label "+label+"\">";
					high = high + anno.substring(init, end);
					high = high + "</span>";
				}
				
				offset = end;
//				Set<String> kes2 = mm.keySet();
//				for (String k2 : kes2) {
//					System.out.println("\t" + k2 + " <--> " + mm.get(k2));
//				}
			}
			high = high + anno.substring(offset);
			
			String translated = NIFReader.extractITSRDFTarget(jena);
			String language = NIFReader.extractITSRDFTargetLanguage(jena);
			if(translated!=null){
				high = high + "<div class=\"col-lg-1\"></div>";
				high = high + "<div class=\"translateText col-lg-11 col-md-5 alert alert-danger\">";
				high = high + "<span class=\"label label-default\">"+language+"</span>"+translated+"</div>";
			}
			return high;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			System.out.println("ERROR at generating the highlighted content of the document");
			return null;
		}
	}
	
	public String getDocumentOverview(String documentName, String collectionName, String user) {
		return getDocumentOverviewJSON(documentName, collectionName, user).toString();
	}
		
	public JSONObject getDocumentOverviewJSON(String documentName, String collectionName, String user) {
		Document document = databaseService.getDocument(documentName, user);
		return document.getJSONObject();
	}

	public boolean checkUser(String user,String password) {
		return databaseService.checkUser(user,password);
	}
	
	public boolean existsUser(String user) {
		return databaseService.existsUser(user);
	}
	
	public boolean deleteDocumentByName(String documentName, String user) {
       	try{
       		databaseService.deleteDocumentByName(documentName, user);
       		return true;
       	}
       	catch(Exception e){
       		return false;
       	}
	}

	public boolean deleteDocumentById(String documentId, String user) {
       	try{
       		databaseService.deleteDocumentById(documentId, user);
       		return true;
       	}
       	catch(Exception e){
       		return false;
       	}
	}

	public String getModels(){
		List<NLPModel> models = databaseService.getModels();
		return convertListIntoJSON(models).toString();
	}
	
	public boolean addModel(String name, String type, String url, String analysis, String models, String language, String informat, String outformat, String mode, String content){
		if(type.equalsIgnoreCase("dict")){
			Unirest.setTimeouts(10000, 10000000);
			
			name = name + "_OTHER";
			
			try{
				HttpResponse<String> response = Unirest.post("http://dev.digitale-kuratierung.de/api/e-nlp/trainModel")
//						.queryString("input", annotatedContent)
					.queryString("analysis", "dict")
					.queryString("language", language)
					.queryString("modelName", name)
//					.queryString("input", content)
					.body(content)
					.asString();

				if(response.getStatus() == 200){
					if(analysis==null){
						analysis = "dict";
					}
					if(!databaseService.addModel(name, type, url, analysis, name, language, informat, outformat, mode)){
						return false;
					}
				}
				else{
					String msg = "Error at generating dictinary-based model: "+name;
					System.out.println("DEBUG: "+msg);
					logger.error(msg);
					return false;
				}
			}
			catch(Exception e){
				e.printStackTrace();
				String msg = "Error at generating dictinary-based model: "+name;
				logger.error(msg, e);
				throw new ExternalServiceFailedException(msg);
			}
		}
		else{
			if(!databaseService.addModel(name, type, url, analysis, models, language, informat, outformat, mode)){
				return false;
			}
		}
		initializeModels();
		return true;
	}

}
