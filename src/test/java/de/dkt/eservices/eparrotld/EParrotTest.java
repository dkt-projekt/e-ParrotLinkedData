package de.dkt.eservices.eparrotld;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import eu.freme.bservices.testhelper.TestHelper;
import eu.freme.bservices.testhelper.ValidationHelper;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;

/**
 * @author 
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EParrotTest {

	TestHelper testHelper;
	ValidationHelper validationHelper;

	@Before
	public void setup() {
		ApplicationContext context = IntegrationTestSetup
				.getContext(TestConstants.pathToPackage);
		testHelper = context.getBean(TestHelper.class);
		validationHelper = context.getBean(ValidationHelper.class);
	}
	
	private HttpRequestWithBody baseRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-parrot/repository/testURL";
		return Unirest.post(url);
	}
	
	private HttpRequestWithBody request(String urlPart) {
		String url = testHelper.getAPIBaseUrl() + "/e-parrot/"+urlPart;
		return Unirest.post(url);
	}
		
	@Test
	public void test1_SanityCheck() throws UnirestException, IOException,
			Exception {

				HttpResponse<String> response = baseRequest()
				.queryString("informat", "text")
				.queryString("input", "hello world")
				.queryString("outformat", "turtle").asString();
		assertTrue(response.getStatus() == 200);
		assertTrue(response.getBody().length() > 0);
	}
	
	@Test
	public void test2_1_listUsers() throws UnirestException, IOException,
			Exception {
				HttpResponse<String> response = request("listUsers/")
						.asString();
		assertTrue(response.getStatus() == 200);
		assertTrue(response.getBody().length() > 0);
		String expectedOutput = "{\"users\":{\"user1\":{\"password\":\"*3A9588B01510694D53D532A8B2885A00D89F0F5D\",\"name\":\"julian\",\"id\":1,\"email\":\"jmschnei@gmail.com\"},\"user2\":{\"password\":\"*C87302C99F650EFB1654068B217EFE5E69DA6271\",\"name\":\"julian 3\",\"id\":2,\"email\":\"jmschnei3\"}}}";
		Assert.assertEquals(expectedOutput, response.getBody());
	}

	@Test
	public void test3_1_CreateUser_1() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("createUser/")
				.queryString("newUser", "jmschnei2")
				.queryString("newPassword", "jmschnei2")
				.queryString("newUserName", "julian 2")
				.queryString("newUserRole", "normal")
				.queryString("user", "jmschnei")
				.queryString("password", "password")
				.asString();
		assertTrue(response.getStatus() == 200);
		assertTrue(response.getBody().length() > 0);
		Assert.assertEquals("The user jmschnei2 has NOT been created. The process has failed!!!!", response.getBody());
	}
	
//	@Test
//	public void test3_2_CreateUser_2() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response2 = request("createUser/")
//				.queryString("newUser", "jmschnei3")
//				.queryString("newPassword", "jmschnei3")
//				.queryString("newUserName", "julian 3")
//				.queryString("newUserRole", "normal")
//				.queryString("user", "jmschnei@gmail.com")
//				.queryString("password", "passwordJuli")
//				.asString();
//		assertTrue(response2.getStatus() == 200);
//		assertTrue(response2.getBody().length() > 0);
//		Assert.assertTrue(response2.getBody().contains("successfully"));
//	}
//
	
	@Test
	public void test3_3_CheckUser() throws UnirestException, IOException,Exception {
		HttpResponse<String> response2 = request("checkUser/")
				.queryString("user", "jmschnei@gmail.com")
				.queryString("password", "passwordJuli")
				.asString();
		assertTrue(response2.getStatus() == 200);
		assertTrue(response2.getBody().length() > 0);
		System.out.println("DEBUG: "+response2.getBody());
		Assert.assertTrue(response2.getBody().contains("true"));

		response2 = request("checkUser/")
				.queryString("user", "jmschnei2@gmail.com")
				.queryString("password", "passwordJuli")
				.asString();
		assertTrue(response2.getStatus() == 200);
		assertTrue(response2.getBody().length() > 0);
		Assert.assertTrue(response2.getBody().contains("false"));
	}
	
	@Test
	public void test3_4_ExistsUser() throws UnirestException, IOException,Exception {
		HttpResponse<String> response2 = request("existsUser/")
				.queryString("user", "jmschnei@gmail.com")
				.asString();
		assertTrue(response2.getStatus() == 200);
		Assert.assertTrue(response2.getBody().contains("true"));

		response2 = request("existsUser/")
				.queryString("user", "jmschnei3")
				.asString();
		assertTrue(response2.getStatus() == 200);
		Assert.assertTrue(response2.getBody().contains("true"));

		response2 = request("existsUser/")
				.queryString("user", "jmschnei2@gmail.com")
				.asString();
		assertTrue(response2.getStatus() == 200);
		Assert.assertTrue(response2.getBody().contains("false"));
	}
	
//	@Test
//	public void test4_1_CreateCollection() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = request("createCollection/")
//				.queryString("collectionName", "collection1")
//				.queryString("description", "This is the description of the test collection 1")
//				.queryString("user", "jmschnei@gmail.com")
//				.queryString("private", false)
//				.queryString("analysis", "ner")
//				.queryString("users", "")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		assertTrue(response.getBody().length() > 0);
//		Assert.assertTrue(response.getBody().contains("successfully"));
//
//	}

	@Test
	public void test4_2_listCollections() throws UnirestException, IOException,
			Exception {
				HttpResponse<String> response = request("listCollections/")
						.queryString("informat", "text")
						.asString();
		assertTrue(response.getStatus() == 200);
		Assert.assertEquals("{\"users\":{\"collection1\":{\"entitylinking\":\"\",\"private\":false,\"description\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":1,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"},\"collection2\":{\"entitylinking\":\"\",\"private\":false,\"description\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":2,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"},\"collection3\":{\"entitylinking\":\"\",\"private\":false,\"description\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":3,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"}}}", response.getBody());

			HttpResponse<String> response2 = request("listCollections/")
					.queryString("user", "jmschnei")
					.asString();
		assertTrue(response.getStatus() == 200);
		Assert.assertEquals("{\"users\":{\"collection1\":{\"entitylinking\":\"\",\"private\":false,\"description\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":1,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"},\"collection2\":{\"entitylinking\":\"\",\"private\":false,\"description\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":2,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"},\"collection3\":{\"entitylinking\":\"\",\"private\":false,\"description\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":3,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"}}}", response.getBody());
	}
	
	@Test
	public void test4_3_collectionOverview() throws UnirestException, IOException,Exception {
				HttpResponse<String> response = request("collection1/overview").asString();
		assertTrue(response.getStatus() == 200);
		Assert.assertEquals("{\"entitylinking\":\"\",\"private\":false,\"description\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":1,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"}", response.getBody());
		
	}
	
	@Test
	public void test5_1_CreateDocument() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("collection1/addDocument")
				.queryString("documentName", "document1Plaintext")
				.queryString("documentDescription", "This is the description of the document 1 (Plaintext) of collection 1")
				.queryString("user", "jmschnei@gmail.com")
				.queryString("format", "")
				.queryString("informat", "text")
				.queryString("input", TestConstants.doc1PlaintextContent)
//				.queryString("path", "")
				.queryString("analysis", "ner_LOC_en")
				.asString();
		assertTrue(response.getStatus() == 200);
		Assert.assertTrue(response.getBody().contains("successfully"));

//		response = request("collection1/addDocument")
//				.queryString("documentName", "document1Turtle")
//				.queryString("documentDescription", "This is the description of the document 1 (Turtle) of collection 1")
//				.queryString("user", "jmschnei@gmail.com")
//				.queryString("format", "")
//				.queryString("informat", "turtle")
//				.queryString("input", TestConstants.doc1TurtleContent)
////				.queryString("path", "")
//				.queryString("analysis", "ner_LOC_en")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		Assert.assertTrue(response.getBody().contains("successfully"));

	}


//	@Test
//	public void testETimeliningProcessDocument() throws UnirestException, IOException,Exception {
//		String indexPath = "";
//		String sesamePath = "";
//		String OS = System.getProperty("os.name");
//		
//		if(OS.startsWith("Mac")){
//			indexPath = "/Users/jumo04/Documents/DFKI/DKT/dkt-test/tests/luceneindexes/";
//			sesamePath = "/Users/jumo04/Documents/DFKI/DKT/dkt-test/tests/sesamestorages/";
//		}
//		else if(OS.startsWith("Windows")){
//			indexPath = "C://tests/luceneindexes/";
//			sesamePath = "C://tests/sesamestorage/";
//		}
//		else if(OS.startsWith("Linux")){
//			indexPath = "/tmp/storage/documents/index1/";
//			sesamePath = "/tmp/storage/sesame/storage1/";
//		}
//		
//		HttpResponse<String> response = documentRequest()
//				.queryString("informat", "text/turtle")
//				.queryString("input", inputText)
//				.queryString("outformat", "turtle")
//				.queryString("language", "en")
//				.queryString("openNLPAnalysis", "ner")
//				.queryString("openNLPModels", "ner-wikinerEn_LOC;ner-wikinerEn_ORG;ner-wikinerEn_PER;englishDates")
//				.queryString("sesameStorageName", "test2")
//				.queryString("sesameStoragePath", sesamePath)
//				.queryString("sesameCreate", true)
//				.queryString("luceneIndexName", "test2")
//				.queryString("luceneIndexPath", indexPath)
//				.queryString("luceneFields", "content")
//				.queryString("luceneAnalyzers", "standard")
//				.queryString("luceneCreate", true)
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		assertTrue(response.getBody().length() > 0);
//		Assert.assertEquals(expectedOutput, response.getBody());
//		
//
//		
//	}
	
	String expectedOutputRetrieval = "{\"results\":{\"documents\":{\"document1\":{\"score\":0.028767451643943787,\"docId\":1,\"content\":\"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\"}},\"numberResults\":1,\"querytext\":\"content:sanjurjo\"}}";

}
