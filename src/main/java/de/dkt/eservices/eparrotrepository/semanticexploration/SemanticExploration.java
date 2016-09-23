package de.dkt.eservices.eparrotrepository.semanticexploration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.niftools.DBO;
import de.dkt.common.niftools.NIF;
import de.dkt.common.niftools.NIFReader;
import de.dkt.eservices.eparrotrepository.geolocalization.GeolocalizedElement.GeoElementType;
import de.dkt.eservices.eparrotrepository.timelining.TreeTL;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.exception.ExternalServiceFailedException;

public class SemanticExploration {

	static Logger logger = Logger.getLogger(SemanticExploration.class);

	/*
	 * {
    "Mirabeau(http://dbpedia.org/resource/Mirabeau)": {"carried": {"Draco(http://dbpedia.org/resource/Draco)": 325}},
    "Pakistan(http://dbpedia.org/resource/Pakistan)": {
        "directing": {"Afghanistan(http://dbpedia.org/resource/Afghanistan)": 171},
        "believed": {"Balochistan(http://dbpedia.org/resource/Balochistan)": 946},
        "played": {"Afghanistan(http://dbpedia.org/resource/Afghanistan)": 300}
    },
    "Chesapeake(http://dbpedia.org/resource/Chesapeake)": {"defeated": {"Cornwallis(http://dbpedia.org/resource/Cornwallis)": 300}},
    "Corsica(http://dbpedia.org/resource/Corsica)": {
        "take": {"Carthage(http://dbpedia.org/resource/Carthage)": 1056},
        "led": {
            "Carthage(http://dbpedia.org/resource/Carthage)": 1830,
            "Hamilcar(http://dbpedia.org/resource/Hamilcar)": 1830
        },
        "annexed": {"Rome(http://dbpedia.org/resource/Rome)": 231}
    },
    "Byzantium(http://dbpedia.org/resource/Byzantium)": {
        "remained": {"Pausanias(http://dbpedia.org/resource/Pausanias)": 171},
        "gave": {
            "Europe(http://dbpedia.org/resource/Europe)": 780,
            "Asia(http://dbpedia.org/property/asia)": 780
        },
        "is": {"Allies(http://dbpedia.org/resource/Allies)": 351},
        "returned": {"Pausanias(http://dbpedia.org/resource/Pausanias)": 325}
    },
    "McClellan(http://dbpedia.org/resource/McClellan)": {
        "halted": {"Johnston(http://dbpedia.org/resource/Johnston)": 2500},
        "defeated": {"Lee(http://dbpedia.org/resource/Lee)": 2550},
        "fought": {
            "Sharpsburg(http://dbpedia.org/resource/Sharpsburg)": 465,
            "Maryland(http://dbpedia.org/resource/Maryland)": 465
        },
        "reached": {"Richmond(http://dbpedia.org/resource/Richmond)": 2550},
        "restored": {"Lincoln(http://dbpedia.org/resource/Lincoln)": 45},
        "attacked": {
            "Richmond(http://dbpedia.org/resource/Richmond)": 703,
            "Lincoln(http://dbpedia.org/resource/Lincoln)": 666,
            "Virginia(http://dbpedia.org/resource/Virginia)": 703
        },
        "returned": {"Lee(http://dbpedia.org/resource/Lee)": 136},
        "resisted": {"Lee(http://dbpedia.org/resource/Lee)": 630}
    },
    "Hanno(http://dbpedia.org/resource/Hanno)": {"conquered": {
        "Carthage(http://dbpedia.org/resource/Carthage)": 496,
        "Hamilcar(http://dbpedia.org/resource/Hamilcar)": 528
    }},
    "Coalition(http://dbpedia.org/resource/Coalition)": {"encounter": {"Chechnyan(http://dbpedia.org/resource/Chechnyan)": 496}},
    "Manchuria(http://dbpedia.org/resource/Manchuria)": {
        "speculated": {
            "Truman(http://dbpedia.org/resource/Truman)": 3570,
            "Korea(http://dbpedia.org/resource/Korea)": 3570
        },
        "wanted": {
            "Stalin(http://dbpedia.org/resource/Stalin)": 5852,
            "Mao(http://dbpedia.org/resource/Mao)": 5852
        },
        "left": {"USSR(http://dbpedia.org/resource/USSR)": 378},
        "airlifted": {
            "KMT(http://dbpedia.org/resource/KMT)": 105,
            "China(http://dbpedia.org/resource/China)": 105
        },
        "cross": {"Yalu(http://dbpedia.org/resource/Yalu)": 300},
        "focused": {"Irkutsk(http://dbpedia.org/resource/Irkutsk)": 1035},
        "continued": {"Chiang(http://dbpedia.org/resource/Chiang)": 5852},
        "had": {
            "PRC(http://dbpedia.org/property/prc)": 6972,
            "KMT(http://dbpedia.org/resource/KMT)": 36
	 */
	public static List<String> generateGeolocalization(List<String> listTexts, boolean addElements) throws ExternalServiceFailedException {
		try{
			List<GeolocalizedElement> inputModels = new LinkedList<GeolocalizedElement>();
			for (String t : listTexts) {
				GeolocalizedElement tle = new GeolocalizedElement(t);
				inputModels.add(tle);
			}
			List<GeolocalizedElement> outputTimelinedElements = generateGeolocalizationFromModels(inputModels,addElements);
			List<String> outputStrings = new LinkedList<String>();
			for (GeolocalizedElement te : outputTimelinedElements) {
				outputStrings.add(te.toString());
			}
			return outputStrings;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

	public static List<GeolocalizedElement> generateGEGeolocalization(List<String> listTexts, boolean addElements) throws ExternalServiceFailedException {
		try{
			List<GeolocalizedElement> inputModels = new LinkedList<GeolocalizedElement>();
			for (String t : listTexts) {
				GeolocalizedElement tle = new GeolocalizedElement(t);
				inputModels.add(tle);
			}
			List<GeolocalizedElement> outputTimelinedElements = generateGeolocalizationFromModels(inputModels,addElements);
			return outputTimelinedElements;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

	public static List<GeolocalizedElement> generateGeolocalizationFromModels(List<GeolocalizedElement> listNifModels, boolean addElements) throws ExternalServiceFailedException {
		try{
			TreeTL<Geolocation, GeolocalizedElement> tree = generateGeolocalizationTreeFromModels(listNifModels, addElements);
			List<GeolocalizedElement> orderedModels = tree.getInorder();
	        return orderedModels;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	public static TreeTL<Geolocation, GeolocalizedElement> generateGeolocalizationTreeFromModels(List<GeolocalizedElement> listNifModels, boolean addElements) throws ExternalServiceFailedException {
		try{
			TreeTL<Geolocation, GeolocalizedElement> tree = new TreeTL<Geolocation, GeolocalizedElement>();
			for (GeolocalizedElement tle: listNifModels) {
				Model model = tle.model;
				String meanDate = NIFReader.extractMeanDateRange(model);
				if(meanDate!=null){
					Geolocation ter = new Geolocation(meanDate);
					GeolocalizedElement te = new GeolocalizedElement(GeoElementType.DOCUMENT, NIFReader.extractDocumentURI(model), ter, model);
					tree.addElement(ter, te);
				}
				if(addElements){
					List<GeolocalizedElement> times = generateGeolocalizedConceptsFromModel(model);
					for (GeolocalizedElement t : times) {
						tree.addElement(t.geolocationExpression, t);
					}
				}
			}
	        return tree;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}

	public static List<SemanticEntity> generateEntitiesListFromModel(Model model, boolean addElements) throws ExternalServiceFailedException {
		try{
			List<SemanticEntity> list = new LinkedList<SemanticEntity>();
			Map<String,Map<String,String>> map = NIFReader.extractEntitiesExtended(model);
			Set<String> keyset = map.keySet();
			for (String k : keyset) {
				Map<String,String> kMap = map.get(k);
				String anchorOf = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#anchorOf";
				String taIdentRef = "http://www.w3.org/2005/11/its/rdf#taIdentRef";
				SemanticEntity se1 = new SemanticEntity(kMap.get(anchorOf),kMap.get(taIdentRef));
				list.add(se1);
			}
			return list;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}

	public static void main(String[] args) throws Exception {
		String inputText = "@prefix geo:   <http://www.w3.org/2003/01/geo/wgs84_pos/> .\n" +
						"@prefix dbo:   <http://dbpedia.org/ontology/> .\n" +
						"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
						"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\n" +
						"@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\n" +
						"@prefix dfkinif: <http://dkt.dfki.de/ontologies/nif#> .\n" +
						"@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n" +
						"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=650,662>\n" +
						"        a                     nif:RFC5147String , nif:String ;\n" +
						"        nif:anchorOf          \"Nationalists\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"650\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"662\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:organization ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Kuomintang> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=18,26>\n" +
						"        a                     nif:String , nif:RFC5147String ;\n" +
						"        dbo:birthDate         \"1872-03-28\"^^xsd:date ;\n" +
						"        dbo:deathDate         \"1936-07-20\"^^xsd:date ;\n" +
						"        nif:anchorOf          \"Sanjurjo\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"18\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"26\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:person ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/José_Sanjurjo> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=277,282>\n" +
						"        a                     nif:RFC5147String , nif:String ;\n" +
						"        nif:anchorOf          \"Spain\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"277\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"282\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:location ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        geo:lat               \"40.43333333333333\"^^xsd:double ;\n" +
						"        geo:long              \"-3.7\"^^xsd:double ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Spain> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=254,260>\n" +
						"        a                     nif:RFC5147String , nif:String ;\n" +
						"        nif:anchorOf          \"Ferrol\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"254\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"260\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:location ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        geo:lat               \"43.46666666666667\"^^xsd:double ;\n" +
						"        geo:long              \"-8.25\"^^xsd:double ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Ferrol,_Galicia> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=704,710>\n" +
						"        a                     nif:String , nif:RFC5147String ;\n" +
						"        nif:anchorOf          \"Bilbao\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"704\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"710\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:location ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        geo:lat               \"43.25694444444444\"^^xsd:double ;\n" +
						"        geo:long              \"-2.923611111111111\"^^xsd:double ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Bilbao> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=201,213>\n" +
						"        a                     nif:RFC5147String , nif:String ;\n" +
						"        nif:anchorOf          \"Nationalists\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"201\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"213\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:organization ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Kuomintang> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=0,805>\n" +
						"        a                         nif:String , nif:Context , nif:RFC5147String ;\n" +
						"        dfkinif:averageLatitude   \"42.17561833333333\"^^xsd:double ;\n" +
						"        dfkinif:averageLongitude  \"-4.075817777777778\"^^xsd:double ;\n" +
						"        dfkinif:standardDeviationLatitude\n" +
						"                \"1.4446184970002673\"^^xsd:double ;\n" +
						"        dfkinif:standardDeviationLongitude\n" +
						"                \"2.202362615152696\"^^xsd:double ;\n" +
						"        nif:beginIndex            \"0\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex              \"805\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:isString              \"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipuzcoa from July to September. The capture of Guipuzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irun, closing the French border to the Republicans. On 13 September, the Basques surrendered Madrid to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\"^^xsd:string .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=543,547>\n" +
						"        a                     nif:String , nif:RFC5147String ;\n" +
						"        nif:anchorOf          \"Irun\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"543\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"547\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:location ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        geo:lat               \"43.33781388888889\"^^xsd:double ;\n" +
						"        geo:long              \"-1.788811111111111\"^^xsd:double ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Irun> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=372,393>\n" +
						"        a                     nif:RFC5147String , nif:String ;\n" +
						"        nif:anchorOf          \"Campaign of Guipuzcoa\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"372\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"393\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:organization ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Political_campaign> , <http://dbpedia.org/resource/Gipuzkoa> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=146,151>\n" +
						"        a                     nif:RFC5147String , nif:String ;\n" +
						"        nif:anchorOf          \"South\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"146\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"151\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:location ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Southern_United_States> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=636,642>\n" +
						"        a                     nif:RFC5147String , nif:String ;\n" +
						"        nif:anchorOf          \"Madrid\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"636\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"642\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:location ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        geo:lat               \"40.38333333333333\"^^xsd:double ;\n" +
						"        geo:long              \"-3.716666666666667\"^^xsd:double ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Madrid> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=21,25>\n" +
						"        a                  nif:RFC5147String , nif:String ;\n" +
						"        nif:anchorOf       \"2016\"^^xsd:string ;\n" +
						"        nif:beginIndex     \"21\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex       \"25\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity         <http://dkt.dfki.de/ontologies/nif#date> ;\n" +
						"        itsrdf:taIdentRef  <http://dkt.dfki.de/ontologies/nif#date=20160101000000_20170101000000> .\n" +
						"\n" +
						"<http://dkt.dfki.de/documents/#char=345,356>\n" +
						"        a                     nif:RFC5147String , nif:String ;\n" +
						"        dbo:birthDate         \"1887-06-09\"^^xsd:date ;\n" +
						"        dbo:deathDate         \"1937-06-03\"^^xsd:date ;\n" +
						"        nif:anchorOf          \"Emilio Mola\"^^xsd:string ;\n" +
						"        nif:beginIndex        \"345\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:endIndex          \"356\"^^xsd:nonNegativeInteger ;\n" +
						"        nif:entity            dfkinif:person ;\n" +
						"        nif:referenceContext  <http://dkt.dfki.de/documents/#char=0,805> ;\n" +
						"        itsrdf:taIdentRef     <http://dbpedia.org/resource/Emilio_Mola> .\n" +
						"";
		Model m = NIFReader.extractModelFromFormatString(inputText, RDFSerialization.TURTLE);
		SemanticExploration.generateGeolocalizedConceptsFromModel(m);
	}
}
