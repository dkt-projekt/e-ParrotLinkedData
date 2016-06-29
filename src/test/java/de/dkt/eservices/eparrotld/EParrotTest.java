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
//	
//	@Test
//	public void test2_1_listUsers() throws UnirestException, IOException,
//			Exception {
//				HttpResponse<String> response = request("listUsers/")
//						.asString();
//		assertTrue(response.getStatus() == 200);
//		assertTrue(response.getBody().length() > 0);
//		String expectedOutput = "{\"users\":{\"user1\":{\"password\":\"*3A9588B01510694D53D532A8B2885A00D89F0F5D\",\"name\":\"julian\",\"id\":1,\"email\":\"jmschnei@gmail.com\"},\"user2\":{\"password\":\"*C87302C99F650EFB1654068B217EFE5E69DA6271\",\"name\":\"julian 3\",\"id\":2,\"email\":\"jmschnei3\"}}}";
//		Assert.assertEquals(expectedOutput, response.getBody());
//	}

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
	public void test4_2_listCollections() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("listCollections/")
				.queryString("informat", "text")
				.asString();
		assertTrue(response.getStatus() == 200);
		
		Assert.assertEquals("{\"collections\":{\"collection1\":{\"entitylinking\":\"\",\"private\":false,\"documents\":\"<div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Plaintext<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div><div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Turtle<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div>\",\"collectionDescription\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":1,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"},\"collection2\":{\"entitylinking\":\"\",\"private\":false,\"documents\":\"\",\"collectionDescription\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":2,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection2\"},\"collection3\":{\"entitylinking\":\"\",\"private\":false,\"documents\":\"\",\"collectionDescription\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":3,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection3\"}}}", response.getBody());

		HttpResponse<String> response2 = request("listCollections/")
				.queryString("user", "jmschnei@gmail.com")
				.asString();
		assertTrue(response2.getStatus() == 200);
		Assert.assertEquals("{\"collections\":{\"collection1\":{\"entitylinking\":\"\",\"private\":false,\"documents\":\"<div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Plaintext<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div><div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Turtle<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div>\",\"collectionDescription\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":1,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"},\"collection2\":{\"entitylinking\":\"\",\"private\":false,\"documents\":\"\",\"collectionDescription\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":2,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection2\"},\"collection3\":{\"entitylinking\":\"\",\"private\":false,\"documents\":\"\",\"collectionDescription\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":3,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection3\"}}}", response2.getBody());

		HttpResponse<String> response3 = request("listCollections/")
				.queryString("user", "jmschnei@gmail.com")
				.queryString("collectionId", "1")
				.asString();
		assertTrue(response3.getStatus() == 200);
		Assert.assertEquals("{\"collections\":{\"collection1\":{\"entitylinking\":\"\",\"private\":false,\"documents\":\"<div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Plaintext<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div><div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Turtle<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div>\",\"collectionDescription\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":1,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"}}}", response3.getBody());

		HttpResponse<String> response4 = request("listCollections/")
				.queryString("user", "jmschnei")
				.queryString("collectionId", "1")
				.asString();
		assertTrue(response4.getStatus() == 200);
		Assert.assertEquals("{}", response4.getBody());

		HttpResponse<String> response5 = request("listCollections/")
				.queryString("user", "jmschnei")
				.asString();
		assertTrue(response5.getStatus() == 200);
		Assert.assertEquals("{}", response5.getBody());
	}
	
	@Test
	public void test4_3_collectionOverview() throws UnirestException, IOException,Exception {
				HttpResponse<String> response = request("collection1/overview").asString();
		assertTrue(response.getStatus() == 200);
		
		Assert.assertEquals("{\"entitylinking\":\"\",\"private\":false,\"documents\":\"<div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Plaintext<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div><div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Turtle<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div>\",\"collectionDescription\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":1,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"}", response.getBody());
		
	}
	
	@Test
	public void test5_1_DeleteDocument() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("collection1/deleteDocument")
				.queryString("documentName", "document1Plaintext")
				.queryString("user", "jmschnei@gmail.com")
				.asString();
		assertTrue(response.getStatus() == 200);
		System.out.println("BODY: "+ response.getBody());
//		Assert.assertTrue(response.getBody().contains("successfully"));

		response = request("collection1/deleteDocument")
				.queryString("documentName", "document1Turtle")
				.queryString("user", "jmschnei@gmail.com")
				.asString();
		assertTrue(response.getStatus() == 200);
		System.out.println("BODY: "+ response.getBody());
//		Assert.assertTrue(response.getBody().contains("successfully"));

	}
	
	@Test
	public void test5_2_CreateDocument() throws UnirestException, IOException,Exception {
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

		response = request("collection1/addDocument")
				.queryString("documentName", "document1Turtle")
				.queryString("documentDescription", "This is the description of the document 1 (Turtle) of collection 1")
				.queryString("user", "jmschnei@gmail.com")
				.queryString("format", "")
				.queryString("informat", "turtle")
				.queryString("input", TestConstants.doc1TurtleContent)
//				.queryString("path", "")
				.queryString("analysis", "ner_LOC_en")
				.asString();
		assertTrue(response.getStatus() == 200);
		Assert.assertTrue(response.getBody().contains("successfully"));

		response = request("collection1/addDocument")
				.queryString("documentName", "document1Turtle")
				.queryString("documentDescription", "This is the description of the document 1 (Turtle) of collection 1")
				.queryString("user", "jmschnei")
				.queryString("format", "")
				.queryString("informat", "turtle")
				.queryString("input", TestConstants.doc1TurtleContent)
//				.queryString("path", "")
				.queryString("analysis", "ner_LOC_en")
				.asString();
		assertTrue(response.getStatus() == 502);
		System.out.println("BODY: "+response.getBody());
		Assert.assertTrue(response.getBody().contains("has not rights for accessing the collection"));
	}
	
	@Test
	public void test5_3_ListDocuments() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("collection1/listDocuments")
				.queryString("user", "jmschnei@gmail.com")
				.asString();
		assertTrue(response.getStatus() == 200);
		
		String expectedDocuments = "{\"documents\":{\"document2\":{\"documentDescription\":\"This is the description of the document 1 (Turtle) of collection 1\",\"highlightedContent\":\"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\",\"documentId\":17,\"documentName\":\"document1Turtle\",\"documentContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:String , nif:RFC5147String , nif:Context ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=636,649>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"636\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"649\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=146,151>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"146\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"151\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:Context , nif:RFC5147String , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\\n<http://dkt.dfki.de/examples/#char=711,717>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"711\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"717\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=277,282>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"277\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"282\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=254,260>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"254\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"260\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\"},\"document1\":{\"documentDescription\":\"This is the description of the document 1 (Plaintext) of collection 1\",\"highlightedContent\":\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\",\"documentId\":16,\"documentName\":\"document1Plaintext\",\"documentContent\":\"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://freme-project.eu/#char=0,816>\\n        a               nif:RFC5147String , nif:Context , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"816\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\\\\\n\\\\\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\\\\\n\\\" .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://freme-project.eu/#char=713,719>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"713\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"719\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=638,651>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"638\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"651\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=279,284>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"279\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"284\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=256,262>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"256\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"262\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=0,816>\\n        a               nif:String , nif:Context , nif:RFC5147String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"816\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\\\\\n\\\\\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\\\\\n\\\" .\\n\\n<http://freme-project.eu/#char=148,153>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"148\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"153\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\"}}}";
		Assert.assertEquals(expectedDocuments,response.getBody());

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

	@Test
	public void test6_1_documentOverview() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("collection1/document1Turtle/overview").asString();
		assertTrue(response.getStatus() == 502);
		Assert.assertTrue(response.getBody().contains("The user has not permission to read this document"));
	}
	
	@Test
	public void test6_2_documentOverview() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("collection1/document1Turtle/overview")
				.queryString("user", "jmschnei@gmail.com")
				.asString();
		assertTrue(response.getStatus() == 200);
		
		String expectedDocuments = "{\"documentDescription\":\"This is the description of the document 1 (Turtle) of collection 1\",\"highlightedContent\":\"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\",\"documentId\":17,\"documentName\":\"document1Turtle\",\"documentContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:String , nif:RFC5147String , nif:Context ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=636,649>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"636\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"649\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=146,151>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"146\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"151\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:Context , nif:RFC5147String , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\\n<http://dkt.dfki.de/examples/#char=711,717>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"711\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"717\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=277,282>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"277\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"282\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=254,260>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"254\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"260\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\"}";
		
		Assert.assertEquals(expectedDocuments,response.getBody());
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
