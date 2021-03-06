package de.dkt.eservices.eparrotrepository.geolocalization;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.niftools.NIFReader;
import de.dkt.eservices.eparrotrepository.geolocalization.GeolocalizedElement.GeoElementType;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.exception.BadRequestException;

public class TimeConversion {

	static Logger logger = Logger.getLogger(TimeConversion.class);

	public static List<Model> generateJENAFromJSON(JSONObject jsonObject) throws Exception {
		try {
			List<Model> outputList = new LinkedList<Model>();
        	JSONArray jsonDocumentsArray = jsonObject.getJSONArray("results");
        	for (int i = 0; i < jsonDocumentsArray.length(); i++) {
				JSONObject obj = jsonDocumentsArray.getJSONObject(i);
				String nifText = obj.getString("content");
				Model m = NIFReader.extractModelFromFormatString(nifText, RDFSerialization.TURTLE);
				outputList.add(m);
			}
			return outputList;
		} catch (BadRequestException e) {
			logger.error(e.getMessage());
            throw e;
		}
	}

	public static JSONObject generateJSONFromJENA(List<Model> listModels) throws Exception {
		try{
        	JSONObject obj = new JSONObject();
			JSONObject joDocuments = new JSONObject();
			int i=0;
        	for (Model m : listModels) {
				JSONObject resultJSON = new JSONObject();
				String uri = NIFReader.extractDocumentURI(m);
				String nifContent = NIFReader.model2String(m, RDFSerialization.TURTLE);
				resultJSON.put("docId", i+1);
				resultJSON.put("docURI", uri);
				resultJSON.put("content", nifContent);
				joDocuments.put("result"+(i+1),resultJSON);
				i++;
			}
			obj.put("documents", joDocuments);
        	return obj;
		} catch (BadRequestException e) {
			logger.error(e.getMessage());
            throw e;
		}
	}


	public static List<GeolocalizedElement> generateGeolocalizedElementsFromJSON(JSONObject jsonObject) throws Exception {
		try {
			List<GeolocalizedElement> outputList = new LinkedList<GeolocalizedElement>();
        	JSONArray jsonDocumentsArray = jsonObject.getJSONArray("results");
        	for (int i = 0; i < jsonDocumentsArray.length(); i++) {
				JSONObject obj = jsonDocumentsArray.getJSONObject(i);
				GeoElementType type = GeoElementType.valueOf(obj.getString("type"));
				String uri = obj.getString("uri");
				Geolocation geolocationExpression = new Geolocation(obj.getString("geolocationexpression"));
				String nifText = obj.getString("content");
				Model m = NIFReader.extractModelFromFormatString(nifText, RDFSerialization.TURTLE);
				GeolocalizedElement tle = new GeolocalizedElement(type, uri, geolocationExpression, m);
				outputList.add(tle);
			}
			return outputList;
		} catch (BadRequestException e) {
			logger.error(e.getMessage());
            throw e;
		}
	}

	public static JSONObject generateJSONFromTimelinedElements(List<GeolocalizedElement> listTimelinedElements) throws Exception {
		try{
        	JSONObject obj = new JSONObject();
			JSONObject joDocuments = new JSONObject();
			int i=0;
        	for (GeolocalizedElement tle : listTimelinedElements) {
				JSONObject resultJSON = new JSONObject();
				Model m = tle.model;
				String uri = NIFReader.extractDocumentURI(m);
				String nifContent = NIFReader.model2String(m, RDFSerialization.TURTLE);
				resultJSON.put("resultId", i+1);
				resultJSON.put("type", tle.type.name());
				resultJSON.put("uri", uri);
				resultJSON.put("temporalexpression", tle.geolocationExpression.toString());
				resultJSON.put("content", nifContent);
				joDocuments.put("result"+(i+1),resultJSON);
				i++;
			}
			obj.put("documents", joDocuments);
        	return obj;
		} catch (BadRequestException e) {
			logger.error(e.getMessage());
            throw e;
		}
	}
}
