package de.dkt.eservices.eparrotrepository;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import de.dkt.common.filemanagement.FileFactory;
import de.dkt.common.niftools.NIFReader;
import de.dkt.eservices.eparrotrepository.ddbb.Collection;
import de.dkt.eservices.eparrotrepository.ddbb.Document;
import de.dkt.eservices.eparrotrepository.ddbb.ParrotDatabaseService;
import de.dkt.eservices.eparrotrepository.ddbb.User;
import de.dkt.eservices.eparrotrepository.geolocalization.Geolocalization;
import de.dkt.eservices.eparrotrepository.geolocalization.GeolocalizedElement;
import de.dkt.eservices.eparrotrepository.geolocalization.GeolocalizedElement.GeoElementType;
import de.dkt.eservices.eparrotrepository.semanticexploration.SemanticEntity;
import de.dkt.eservices.eparrotrepository.geolocalization.Geolocation;
import de.dkt.eservices.eparrotrepository.timelining.TimeConversion;
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
	
	
//	@Autowired
//	EDocumentStorageService docStorageService;
//	
//	@Autowired
//	ESesameService sesameService;
//	
//	@Autowired
//	ELuceneService luceneService;
//	
//	@Autowired
//	EOpenNLPService openNLPService;
//	
//	@Autowired
//	EWekaService wekaService;
//
//	@Autowired
//	ETimeliningService timeliningService;
//	
//	@Autowired
//	RDFConversionService rdfConversion = new JenaRDFConversionService();
    
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
			return obj;
		
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
	
	public JSONObject listDocumentsJSON(String collectionId,String user,int limit){
		List<Document> list = databaseService.listDocument(collectionId, user);
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
	
	public String getCollectionOverview(String collectionId,int limit){
		return getCollectionOverviewJSON(collectionId, limit).toString();
	}
	
	public JSONObject getCollectionOverviewJSON(String collectionName,int limit){
		Collection collection = databaseService.getCollectionByName(collectionName);
		JSONObject result = collection.getJSONObject();
		return result;
	}

	public int addDocumentToCollection(String collectionName, String user, String documentName, String documentDescription, String content, String aContent, String analysis) {
		HashMap<String, HashMap<String,String>> map = new HashMap<String, HashMap<String,String>>();
		String baseUrl = "http://dev.digitale-kuratierung.de/api";
		HashMap<String,String> mapaux = new HashMap<String,String>();
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
		
		boolean access = databaseService.checkCollectionPermision(collectionName, user);
		if(!access){
			String msg = "User \""+user+"\" has not rights for accessing the collection \""+collectionName+"\"";
			logger.error(msg);
			throw new ExternalServiceFailedException(msg);
		}

		String annotatedContent = aContent;
		content = aContent;
//		System.out.println("Annotated1: " + annotatedContent);
		String [] analysisParts = analysis.split(",");

		try{
			Unirest.setTimeouts(10000, 10000000);
			for (String an : analysisParts) {
				HashMap<String,String> tempMap = map.get(an);

				try{
					HttpResponse<String> response = Unirest.post(tempMap.get("url"))
//						.queryString("input", annotatedContent)
						.queryString("analysis", tempMap.get("analysis"))
						.queryString("language", tempMap.get("language"))
						.queryString("models", tempMap.get("models"))
						.queryString("informat", tempMap.get("informat"))
						.queryString("outformat", tempMap.get("outformat"))
						.queryString("mode", tempMap.get("mode"))
						.body(annotatedContent)
						.asString();

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
		catch(Exception e){
			String msg = "Error at processing the content of the document.";
			logger.error(msg, e);
			throw new ExternalServiceFailedException(msg);
		}

		String highlightedContent = highlighText(annotatedContent);
		
		int docId = databaseService.storeDocument(documentName, collectionName, user, documentDescription, analysis, content, annotatedContent, highlightedContent);
//		System.out.println("Annotated2: " + annotatedContent);
//		System.out.println("Highlighted: " + highlightedContent);

		if(!updateCollection(collectionName)){
			logger.error("The collection has not been updated!!");
		}
//		return docId;
		return 102;
	}

	public boolean updateCollection(String collectionName){
		List<Document> docsList = databaseService.listDocumentByName(collectionName, null);
		
		// Get all the documents of the collection, and two options:
		//		1. Send them as JSON.
		//		2. Store them into files and send the files.		
		
		String timelining = doCollectionTimelining(collectionName,docsList);
		String geolocalization = doCollectionGeolocalization(collectionName,docsList);
		String semanticexploration = doCollectionSemanticExploration(collectionName,docsList);
		String clustering = doCollectionSemanticExploration(collectionName,docsList);
		String documents = doCollectionDocumentsList(collectionName,docsList);

		System.out.println("TIMELINING: " + timelining);
		System.out.println("GEO: " + geolocalization);
		System.out.println("Semantic: " + semanticexploration);
		System.out.println("Clustering: " + clustering);
		System.out.println("DOCUMENTS: " + documents);
		return databaseService.updateCollection(collectionName, timelining,geolocalization,semanticexploration,clustering,documents);
	}

	public String doCollectionTimelining(String collectionName, List<Document> docsList){
		boolean addElements = false;
		try{
			List<TimelinedElement> inputNIFModels = new LinkedList<TimelinedElement>();
			for (Document d : docsList) {
				Model m = NIFReader.extractModelFromFormatString(d.getAnnotatedContent(), RDFSerialization.TURTLE);
				TimeElementType type = TimeElementType.DOCUMENT;
				String uri = NIFReader.extractDocumentURI(m);

				String dateRange = NIFReader.extractMeanDateRange(m);
				TimeExpressionRange temporalExpression = new TimeExpressionRange(dateRange);
				TimelinedElement tle = new TimelinedElement(type, uri, temporalExpression, m);
				inputNIFModels.add(tle);
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
				ir +=  "<span class=\"label label-default\">"+docId+"</span>";
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

	public String doCollectionGeolocalization(String collectionName, List<Document> docsList){
		boolean addElements = false;
		try{
			List<GeolocalizedElement> inputNIFModels = new LinkedList<GeolocalizedElement>();
			for (Document d : docsList) {
				Model m = NIFReader.extractModelFromFormatString(d.getAnnotatedContent(), RDFSerialization.TURTLE);
				GeoElementType type = GeoElementType.DOCUMENT;
				String uri = NIFReader.extractDocumentURI(m);

				String dateRange = NIFReader.extractMeanDateRange(m);
				Geolocation geolocationExpression = new Geolocation(dateRange);
				GeolocalizedElement gle = new GeolocalizedElement(type, uri, geolocationExpression, m);
				inputNIFModels.add(gle);
			}

			List<GeolocalizedElement> outputNIFModels = Geolocalization.generateGeolocalizationFromModels(inputNIFModels,addElements);

			String output = "var locations = [";
			for (GeolocalizedElement gle: outputNIFModels) {
				output += "["+gle.geolocationExpression.latitude+","+gle.geolocationExpression.longitude+",\""+gle.uri+"\"]";
			}
			output += "]";
			return output;
		}
		catch(Exception e){
			String msg = "Error at generating geolocalization for collection: "+collectionName;
			logger.error(msg);
			return "";
		}	}

	public String doCollectionSemanticExploration(String collectionName, List<Document> docsList){
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

	public String doCollectionClustering(String collectionName, List<Document> docsList){

		HashMap<String, List<SemanticEntity>> docsMap = new HashMap<String, List<SemanticEntity>>();
		List<SemanticEntity> entities = new LinkedList<SemanticEntity>();
		
		for (Document d : docsList) {
			List<SemanticEntity> list = new LinkedList<SemanticEntity>();
			Model model;
			try {
				model = NIFReader.extractModelFromFormatString(d.getAnnotatedContent(), RDFSerialization.TURTLE);
			} catch (Exception e) {
				String msg = "Error parsing document in clustering generation in update collection.";
				logger.error(msg,e);
				return null;
			}
			Map<String,Map<String,String>> map = NIFReader.extractEntitiesExtended(model);
			Set<String> keyset = map.keySet();
			for (String k : keyset) {
				Map<String,String> kMap = map.get(k);
				String anchorOf = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#anchorOf";
				String taIdentRef = "http://www.w3.org/2005/11/its/rdf#taIdentRef";
				SemanticEntity se1 = new SemanticEntity(kMap.get(anchorOf),kMap.get(taIdentRef));
				list.add(se1);
			}
			docsMap.put(d.getDocumentName(), list);
			for (SemanticEntity se : list) {
				if(!entities.contains(se)){
					entities.add(se);
				}
			}
		}
		
		//Generate headers for the file.
		String arff = "@RELATION "+collectionName+"_Clustering\n";

		for (SemanticEntity se : entities) {
			arff += "@ATTRIBUTE "+se.text.replace(' ', '_')+"  NUMERIC\n";
		}
		arff += "\n";
		arff += "@DATA\n";
		Set<String> docsKeys = docsMap.keySet();
		for (String dk : docsKeys) {
			String line = "";
			
			for (SemanticEntity se : entities) {
				if(docsMap.get(dk).contains(se)){
					line += ",1";
				}
				else{
					line += ",0";
				}
			}
			//Substring added to delete the first ,
			arff = line.substring(1)+"\n";
		}
//		//TODO Convert information into ARFF file.
//		String systemTemporalPath = "";
//		BufferedWriter bw = FileFactory.generateBufferedWriterInstance(systemTemporalPath+"", "utf-8", false);
		HttpResponse<String> response = null;
		try {
			response = Unirest.post("http://dev.digitale-kuratierung.de/api/e-timelining/")
					.queryString("algorithm", "em")
					.queryString("language", "en")
					.body(arff)
					.asString();
		} catch (UnirestException e) {
			String msg = "Error at calling the clustering service for collection: "+collectionName;
			logger.error(msg, e);
			return null;
		}

		String result = null;
		if(response.getStatus() == 200){
			result = response.getBody();
			
			
			//TODO The result must be modified to adapt to the interface.
			
			
			
			
			return result;
		}
		else{
			String msg = "Error at generating clustering for collection: "+collectionName;
			logger.error(msg);
			return null;
		}
	}

	public String doCollectionDocumentsList(String collectionName, List<Document> docsList){
		String finalResult = "";
		for (Document document : docsList) {
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
				+ "<blockquote>"
				+ "<p>"+document.getHighlightedContent()+"</p>"
				+ "</blockquote>"
				+ "</div>"
				+ "<div class=\"row\">"
				+ "<span class=\"label label-default\">Other</span>"
				+ "<span class=\"label label-primary\">Temporal Expressions</span>"
				+ "<span class=\"label label-success\">Person</span>"
				+ "<span class=\"label label-info\">Location</span>"
				+ "<span class=\"label label-warning\">Organization</span>"
				//+ "<span class=\"label label-danger\"></span>"
				+ "</div>"
				+ "</div> </div> </div> </div></div>";
			finalResult += ir;
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

			Set<String> keys = map.keySet();
			for (String k : keys) {
//				System.out.println("Key: "+k);
				Map<String,String> internalMap = map.get(k);
				
				int init = Integer.parseInt(internalMap.get(initTag));
				boolean added=false;
				
				for (int i=0;i<list.size() && !added;i++) {
					Map<String,String> mapL = list.get(i);
					int auxInit = Integer.parseInt(mapL.get(initTag));
					if(init<auxInit){
						added = true;
						list.add(i, internalMap);
					}
				}
				
				if(!added){
					list.add(internalMap);
				}
//				Set<String> kes2 = internalMap.keySet();
//				for (String k2 : kes2) {
//					System.out.println("\t" + k2 + " <--> " + internalMap.get(k2));
//				}
			}

			int offset = 0;
			for (Map<String,String> mm : list) {
				int init = Integer.parseInt(mm.get(initTag));
				int end = Integer.parseInt(mm.get(endTag));
				String type = mm.get(typeTag);
				high = high + anno.substring(offset, init);
				
				String label = "";
				if(type.contains("Location")){
					label = "label-warning";
				}
				else if(type.contains("Organization")){
					label = "label-info";
				}
				else if(type.contains("Person")){
					label = "label-success";
				}
				else if(type.contains("Date")){
					label = "label-primary";
				}
				else{
					label = "label-default";
				}
				
				high = high + "<span class=\"label "+label+"\">";
				high = high + anno.substring(init, end);
				high = high + "</span>";
				offset = end;
//				Set<String> kes2 = mm.keySet();
//				for (String k2 : kes2) {
//					System.out.println("\t" + k2 + " <--> " + mm.get(k2));
//				}
			}
			high = high + anno.substring(offset);
			return high;
		}
		catch(Exception e){
			System.out.println("ERROR at generating the highlighted content of the document");
		}
		return null;
	}
	
	public String getDocumentOverview(String documentId, String collectionId, int limit) {
		return getDocumentOverviewJSON(documentId, collectionId, limit).toString();
	}
		
	public JSONObject getDocumentOverviewJSON(String documentId, String collectionId, int limit) {
		Document document = databaseService.getDocument(documentId);
		return document.getJSONObject();
	}

	public boolean checkUser(String user,String password) {
		return databaseService.checkUser(user,password);
	}
	
	public boolean existsUser(String user) {
		return databaseService.existsUser(user);
	}
	
	public void deleteDocumentByName(String documentName) {
       	databaseService.deleteDocumentByName(documentName);
       	return;
	}

	public void deleteDocumentById(String documentId) {
       	databaseService.deleteDocumentByName(documentId);
       	return;
	}
}
