package de.dkt.eservices.eparrotld;

public class TestConstants {
	
	public static final String pathToPackage = "rdftest/eparrotrepository-test-package.xml";
	
	public static final String doc1PlaintextContent = "1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n";
	
	public static final String doc1TurtleContent = "@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\n" + 
"@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\n" + 
"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\n" + 
"@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n" + 
"\n" + 
"<http://dkt.dfki.de/examples/#char=0,813>\n" + 
"        a                    nif:Context , nif:RFC5147String , nif:String ;\n" + 
"        nif:beginIndex       \"0\"^^xsd:nonNegativeInteger ;\n" + 
"        nif:endIndex         \"813\"^^xsd:nonNegativeInteger ;\n" + 
"        nif:isString         \"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\"^^xsd:string ;\n" + 
"\n";

}
