package de.dkt.eservices.eparrotrepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.dkt.common.niftools.NIFReader;
import de.dkt.common.niftools.NIFWriter;
import de.dkt.common.tools.ParameterChecker;
import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.rest.NIFParameterSet;

public class ParrotLDService extends eu.freme.common.rest.BaseRestController{

//	Logger logger = Logger.getLogger(ParrotLDService.class);
//	
//	@Autowired
//	ESesameService sesameService;
//	
////	@Autowired
////	ELuceneService luceneService;
//	
//	@Autowired
//	EOpenNLPService openNLPService;
//	
//	
////	@Autowired
////	ESMTService smtService;
//	
//	@Autowired
//	RDFConversionService rdfConversion;
//	
//	public ParrotLDService() {
//	}
//	
//	@RequestMapping(value = "/e-parrotld/testURL", method = { RequestMethod.POST, RequestMethod.GET })
//	public ResponseEntity<String> testURL(
//			@RequestParam(value = "preffix", required = false) String preffix,
//            @RequestBody(required = false) String postBody) throws Exception {
//
//    	HttpHeaders responseHeaders = new HttpHeaders();
//    	responseHeaders.add("Content-Type", "text/plain");
//    	ResponseEntity<String> response = new ResponseEntity<String>("The restcontroller is working properly", responseHeaders, HttpStatus.OK);
//    	return response;
//	}
//	
//	@RequestMapping(value = "/e-parrotld/processDocument", method = { RequestMethod.POST, RequestMethod.GET })
//	public ResponseEntity<String> storeData(
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
//			@RequestParam(value = "storageFileName", required = false) String storageFileName,
//			@RequestParam(value = "inputFilePath", required = false) String inputFilePath,
////			@RequestParam(value = "preffix", required = false) String preffix,
//			@RequestParam(value = "language", required = false) String language,
//			@RequestParam(value = "openNLPAnalysis", required = false) String openNLPAnalysis,
//			@RequestParam(value = "openNLPModels", required = false) String openNLPModels,
//			@RequestParam(value = "sesameStorageName", required = false) String sesameStorageName,
//			@RequestParam(value = "sesameStoragePath", required = false) String sesameStoragePath,
//			@RequestParam(value = "sesameCreate", required = false) boolean sesameCreate,
//			@RequestParam(value = "luceneIndexName", required = false) String luceneIndexName,
//			@RequestParam(value = "luceneIndexPath", required = false) String luceneIndexPath,
//			@RequestParam(value = "luceneFields", required = false) String luceneFields,
//			@RequestParam(value = "luceneAnalyzers", required = false) String luceneAnalyzers,
//			@RequestParam(value = "luceneCreate", required = false) boolean luceneCreate,
//            @RequestBody(required = false) String postBody) throws Exception {
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
//		ParameterChecker.checkNotNullOrEmpty(language, "language");
//		ParameterChecker.checkNotNullOrEmpty(openNLPAnalysis, "open NLP analysis type");
//		ParameterChecker.checkNotNullOrEmpty(sesameStorageName, "sesame storage");
////		ParameterChecker.checkNotNullOrEmpty(luceneIndexName, "lucene index name");
////		ParameterChecker.checkNotNullOrEmpty(luceneFields, "lucene fields");
////		ParameterChecker.checkNotNullOrEmpty(luceneAnalyzers, "lucene analyzers");
//
//		NIFParameterSet parameters = this.normalizeNif(input, informat,
//				outformat, postBody, acceptHeader, contentTypeHeader, prefix);
//
//		// create rdf model
//		String plaintext = null;
//		Model inputModel = ModelFactory.createDefaultModel();
//
//		if (!parameters.getInformat().equals(
//				RDFConstants.RDFSerialization.PLAINTEXT)) {
//			// input is nif
//			try {
//				inputModel = this.unserializeNif(postBody,parameters.getInformat());
//			} catch (Exception e) {
//				logger.error("failed", e);
//				throw new BadRequestException("Error parsing NIF input");
//			}
//		} else {
//			// input is plaintext
//			plaintext = parameters.getInput();
//			getRdfConversionService().plaintextToRDF(inputModel, plaintext,language, parameters.getPrefix());
//		}
//
//		try {
//			String nifString = getRdfConversionService().serializeRDF(inputModel,RDFSerialization.fromValue(informat));
//			
//			String contentString = nifString;
//	    	System.out.println("INPUT FOR NLP"+contentString);
//	    	ResponseEntity<String> response2 = openNLPService.analyzeText(contentString, language, openNLPAnalysis, openNLPModels, informat);
//	    	String content3 = response2.getBody();
//	    	
//	    	System.out.println("INPUT FOR SESAME: "+content3);
//	    	ResponseEntity<String> response3 = sesameService.storeEntitiesFromString(sesameStorageName,
//	    			sesameStoragePath,sesameCreate,content3, "NIF");
//	    	String content4 = response3.getBody();
//	    	System.out.println("OUTPUT OF SESAME: "+content4);
//			
//	    	HttpHeaders responseHeaders = new HttpHeaders();
//	    	responseHeaders.add("Content-Type", "RDF/XML");
//			String rdfString = content4;
//			return new ResponseEntity<String>(rdfString, responseHeaders, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	@RequestMapping(value = "/e-parrotld/processQuery", method = { RequestMethod.POST, RequestMethod.GET })
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
//			@RequestParam(value = "queryText", required = false) String queryText,
//			@RequestParam(value = "language", required = false) String language,
//			@RequestParam(value = "output", required = false) String output,
//			@RequestParam(value = "openNLPAnalysis", required = false) String openNLPAnalysis,
//			@RequestParam(value = "openNLPModels", required = false) String openNLPModels,
//			@RequestParam(value = "sesameStorageName", required = false) String sesameStorageName,
//			@RequestParam(value = "sesameStoragePath", required = false) String sesameStoragePath,
//			@RequestParam(value = "luceneIndexName", required = false) String luceneIndexName,
//			@RequestParam(value = "luceneIndexPath", required = false) String luceneIndexPath,
//			@RequestParam(value = "luceneFields", required = false) String luceneFields,
//			@RequestParam(value = "luceneAnalyzers", required = false) String luceneAnalyzers,
//			@RequestBody(required = false) String postBody) throws Exception {
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
//		ParameterChecker.checkNotNullOrEmpty(language, "language");
//		ParameterChecker.checkNotNullOrEmpty(queryText, "query text");
//		ParameterChecker.checkNotNullOrEmpty(openNLPAnalysis, "open NLP analysis type");
//		ParameterChecker.checkNotNullOrEmpty(openNLPModels, "open NLP models");
//		ParameterChecker.checkNotNullOrEmpty(sesameStorageName, "sesame storage");
////		ParameterChecker.checkNotNullOrEmpty(luceneIndexName, "lucene index name");
////		ParameterChecker.checkNotNullOrEmpty(luceneFields, "lucene fields");
////		ParameterChecker.checkNotNullOrEmpty(luceneAnalyzers, "lucene analyzers");
//
//		NIFParameterSet parameters = this.normalizeNif(input, informat,
//				outformat, postBody, acceptHeader, contentTypeHeader, prefix);
//		
//		Model responseModel = null;
//		try {
//			String contentString = queryText;
//			
//			Model model = ModelFactory.createDefaultModel(); 
//			NIFWriter.addInitialString(model, contentString, "http://dkt.dfki.de/query");
//
//			Model outModel = ModelFactory.createDefaultModel();
//			String content2 = NIFReader.model2String(model,"TTL");
//			
//        	ResponseEntity<String> response2 = openNLPService.analyzeText(content2, language, openNLPAnalysis, openNLPModels, informat);
//           	String content3 = response2.getBody();
//        	
//           	System.out.println("[QUERY ANALYSIS] INPUT FOR SESAME: " + content3);
//
//           	int iterations = 0;
//        	ResponseEntity<String> response3 = sesameService.retrieveEntitiesFromNIFIterative(sesameStorageName, sesameStoragePath, content3, iterations);
//        	String content4 = response3.getBody();
//
//        	//Merge results from document retrieval and NIF??? HOW???
//
//        	//TODO Include the ordering stuff
//			
//			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.RDF_XML.contentType());
//			return new ResponseEntity<String>(content4, responseHeaders, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
//	
//	@RequestMapping(value = "/e-parrotld/processSPARQLQuery", method = { RequestMethod.POST, RequestMethod.GET })
//	public ResponseEntity<String> processSPARQLQuery(
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
//			@RequestParam(value = "queryText", required = false) String queryText,
//			@RequestParam(value = "language", required = false) String language,
//			@RequestParam(value = "output", required = false) String output,
//			@RequestParam(value = "openNLPAnalysis", required = false) String openNLPAnalysis,
//			@RequestParam(value = "openNLPModels", required = false) String openNLPModels,
//			@RequestParam(value = "sesameStorageName", required = false) String sesameStorageName,
//			@RequestParam(value = "sesameStoragePath", required = false) String sesameStoragePath,
//			@RequestParam(value = "luceneIndexName", required = false) String luceneIndexName,
//			@RequestParam(value = "luceneIndexPath", required = false) String luceneIndexPath,
//			@RequestParam(value = "luceneFields", required = false) String luceneFields,
//			@RequestParam(value = "luceneAnalyzers", required = false) String luceneAnalyzers,
//			@RequestBody(required = false) String postBody) throws Exception {
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
//		ParameterChecker.checkNotNullOrEmpty(language, "language");
//		ParameterChecker.checkNotNullOrEmpty(queryText, "query text");
//		ParameterChecker.checkNotNullOrEmpty(sesameStorageName, "sesame storage");
//
//		NIFParameterSet parameters = this.normalizeNif(input, informat,
//				outformat, postBody, acceptHeader, contentTypeHeader, prefix);
//		
//		Model responseModel = null;
//		try {
//			String contentString = queryText;
//			
//			Model model = ModelFactory.createDefaultModel(); 
//			NIFWriter.addInitialString(model, contentString, "http://dkt.dfki.de/query");
//
//			Model outModel = ModelFactory.createDefaultModel();
//			String content2 = NIFReader.model2String(model,"TTL");
//			
//        	ResponseEntity<String> response3 = sesameService.retrieveEntitiesFromSPARQL(sesameStorageName, sesameStoragePath, contentString);
//        	String content4 = response3.getBody();
//
//        	//Merge results from document retrieval and NIF??? HOW???
//
//        	//TODO Include the ordering stuff
//			
//			HttpHeaders responseHeaders = new HttpHeaders();
//			responseHeaders.add("Content-Type", RDFSerialization.JSON.contentType());
//			return new ResponseEntity<String>(content4, responseHeaders, HttpStatus.OK);			
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
}
