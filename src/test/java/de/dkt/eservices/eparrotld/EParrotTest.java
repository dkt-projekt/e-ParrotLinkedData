package de.dkt.eservices.eparrotld;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestParam;

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
	public void test5_9_ListDocuments() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("CondatTest/listDocuments")
				.queryString("user", "jmschnei@gmail.com")
				.asString();
		assertTrue(response.getStatus() == 200);
		
		String expectedDocuments = "{\"documents\":{\"document2\":{\"documentDescription\":\"This is the description of the document 1 (Turtle) of collection 1\",\"highlightedContent\":\"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\",\"documentId\":17,\"documentName\":\"document1Turtle\",\"documentContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:String , nif:RFC5147String , nif:Context ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=636,649>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"636\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"649\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=146,151>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"146\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"151\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:Context , nif:RFC5147String , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\\n<http://dkt.dfki.de/examples/#char=711,717>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"711\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"717\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=277,282>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"277\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"282\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=254,260>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"254\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"260\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\"},\"document1\":{\"documentDescription\":\"This is the description of the document 1 (Plaintext) of collection 1\",\"highlightedContent\":\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\",\"documentId\":16,\"documentName\":\"document1Plaintext\",\"documentContent\":\"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://freme-project.eu/#char=0,816>\\n        a               nif:RFC5147String , nif:Context , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"816\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\\\\\n\\\\\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\\\\\n\\\" .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://freme-project.eu/#char=713,719>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"713\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"719\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=638,651>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"638\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"651\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=279,284>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"279\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"284\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=256,262>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"256\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"262\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=0,816>\\n        a               nif:String , nif:Context , nif:RFC5147String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"816\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\\\\\n\\\\\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\\\\\n\\\" .\\n\\n<http://freme-project.eu/#char=148,153>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"148\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"153\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\"}}}";
		Assert.assertEquals(expectedDocuments,response.getBody());
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
	
	
	@Test
	public void test8_4_getCollectionClustering() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("CondatTest6/clustering")
				.queryString("user", "jmschnei@gmail.com")
				.asString();
		assertTrue(response.getStatus() == 200);
//		String expectedDocuments = "{\"models\":{\"model4\":{\"modelName\":\"temp_en\",\"models\":\"englishDates\",\"informat\":\"turtle\",\"modelId\":4,\"language\":\"en\",\"modelType\":\"timex\",\"analysis\":\"temp\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model3\":{\"mode\":\"all\",\"modelName\":\"ner_PER_ORG_LOC_en_all\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":3,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model2\":{\"mode\":\"link\",\"modelName\":\"ner_PER_ORG_LOC_en_link\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":2,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model1\":{\"mode\":\"spot\",\"modelName\":\"ner_PER_ORG_LOC_en_spot\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":1,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"}}}";
//		Assert.assertEquals(expectedDocuments,response.getBody());
		String expectedDocuments = "";
		Assert.assertEquals(expectedDocuments, response.getBody());
	}

	
	
	
	@Test
	public void test9_1_listModels() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("listmodels")
				.asString();
		assertTrue(response.getStatus() == 200);
//		String expectedDocuments = "{\"models\":{\"model4\":{\"modelName\":\"temp_en\",\"models\":\"englishDates\",\"informat\":\"turtle\",\"modelId\":4,\"language\":\"en\",\"modelType\":\"timex\",\"analysis\":\"temp\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model3\":{\"mode\":\"all\",\"modelName\":\"ner_PER_ORG_LOC_en_all\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":3,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model2\":{\"mode\":\"link\",\"modelName\":\"ner_PER_ORG_LOC_en_link\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":2,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model1\":{\"mode\":\"spot\",\"modelName\":\"ner_PER_ORG_LOC_en_spot\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":1,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"}}}";
//		Assert.assertEquals(expectedDocuments,response.getBody());
		String expectedDocuments = "{\"models\":{\"model4\":{\"modelName\":\"temp_en\",\"models\":\"englishDates\",\"informat\":\"turtle\",\"modelId\":4,\"language\":\"en\",\"modelType\":\"timex\",\"analysis\":\"temp\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model3\":{\"mode\":\"all\",\"modelName\":\"ner_PER_ORG_LOC_en_all\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":3,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model2\":{\"mode\":\"link\",\"modelName\":\"ner_PER_ORG_LOC_en_link\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":2,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model1\":{\"mode\":\"spot\",\"modelName\":\"ner_PER_ORG_LOC_en_spot\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":1,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"";
		Assert.assertTrue(response.getBody().startsWith(expectedDocuments));
	}

	@Test
	public void test9_2_addModel() throws UnirestException, IOException,Exception {
		
		String dictionaryContent = "Armentières	http://www.geonames.org/3036903\n"
				+"Hampstead Heath	http://www.geonames.org/6618525/hampstead-heath.html\n"
				+"Haarlem 	http://www.geonames.org/2755003\n"
				+"England	http://www.geonames.org/6269131\n"
				+"Lourds	http://www.geonames.org/2997395/lourdes.html\n"
				+"Graswang	http://www.geonames.org/2917958\n"
				+"Straßburg	http://www.geonames.org/ 2764186\n"
				+"La Spezia	http://www.geonames.org/3175081\n"
				+"Aland Islands 	http://www.geonames.org/661883\n"
				+"Sonnenspitze 	http://www.geonames.org/2764900\n"
				+"Luxor 	http://www.geonames.org/360502\n"
				+"San Sebastian	http://www.geonames.org/3110044\n"
				+"Rudolstadt	http://www.geonames.org/2843355\n"
				+"Mount Scopus	http://www.geonames.org/281956\n"
				+"Molokai	http://www.geonames.org/5851218/moloka-i.html";

		Date d = new Date();
		HttpResponse<String> response = request("addmodels")
				.queryString("modelName", "dict_test_"+d.getTime())
				.queryString("modelType", "dict")
				.queryString("modelInformat", "turtle")
				.queryString("modelOutformat", "turtle")
				.queryString("url", "http://dev.digitale-kuratierung.de/e-smt/")
				.queryString("analysis", "dict")
				.queryString("language", "en")
				.queryString("models", "")
				.queryString("mode", "")
				.queryString("content", dictionaryContent)
				.body("")
				.asString();
		assertTrue(response.getStatus() == 200);
		Assert.assertTrue(response.getBody().contains("successfully"));
	}
	
//	@Test
//	public void testX_1_CreateDocument() throws UnirestException, IOException,Exception {
//String input2 = "	**************************** Page 1****************************"
//+ "		  Vikings"
//+ "		  For other uses, see Viking (disambiguation) ."
//+ "		  extended into the Mediterranean littoral, North Africa , the Middle East and Central Asia . Following extended phases of (primarily sea- or river-borne) exploration, ex- pansion and settlement, Viking (Norse) communities and politieswereestablishedindiverseareasofnorth-western Europe, European Russia , the North Atlantic islands and as far as the north-eastern coast of North America . This period of expansion witnessed the wider dissemination ofNorseculture, whilesimultaneouslyintroducingstrong foreign cultural influences into Scandinavia itself, with profound developmental implications in both directions. Popular, modern conceptions of the Vikings—the term frequently applied casually to their modern descen- dants and the inhabitants of modern Scandinavia—often strongly differ from the complex picture that emerges from archaeology and historical sources. A romanticized picture of Vikings as noble savages began to emerge in the18thcentury; thisdevelopedandbecamewidelyprop-"
//+ "		  ceived views of the Vikings as alternatively violent, pi- ratical heathens or as intrepid adventurers owe much to conflicting varieties of the modern Viking myth that had taken shape by the early 20th century. Current popular representations of the Vikings are typically based on cul- tural clichés and stereotypes, complicating modern ap- preciation of the Viking legacy. "
//+ "		  1 Etymology"
//+ "		  Sea-faring Danes depicted invading England. Illuminated illus- tration from the 12th century Miscellany on the Life of St. Ed- mund . Pierpont Morgan Library. "
//+ "		  Vikings ( Norwegian and Danish : Vikinger ; Swedish and Nynorsk : Vikingar ; Icelandic : Víkingar ),from OldNorse víkingr , were Germanic Norse seafarers, speaking the Old Norse language, who raided and traded from their Scandinavian homelands across wide areas of northern and central Europe, as well as European Russia, dur- "
//+ "		  also commonly extended in modern English and other vernaculars to the inhabitants of Viking home communi- ties during what has become known as the Viking Age . This period of Norse military, mercantile and demo- graphicexpansionconstitutesanimportantelementinthe early medieval history of Scandinavia , the British Isles , "
//+ "		  Facilitated by advanced seafaring skills, and charac- terised by the longship , Viking activities at times also"
//+ "		  meaning “creek, inlet, small bay”. [6] Various theories have been offered that the word viking may be derived from the name of the historical Norwegian district of Viken (or Víkin in Old Norse ), meaning “a person from Viken \". According to this theory, the word simply de- scribed persons from this area, and it is only in the last few centuries that it has taken on the broader sense of early medieval Scandinavians in general. However, there are a few major problems with this theory. People from the Viken area were not called 'Viking' in Old Norse manuscripts, butarereferredtoasvíkverir(ModernNor- wegian: vikvær), 'Vík dwellers’. In addition, that ex- planation could only explain the masculine (Old Scan- dinavian víkingr) and ignore the feminine (Old Norse víking), which is a serious problem because the mascu- line is easily derived from the feminine but hardly vice "
//+ "		  some Swedish rune stones. There is little indication of"
//+ "		  1"
//+ "		  agated during the 19th-century Viking revival . [4] [5] Per-"
//+ "		One etymology derives víking from the feminine vík ,"
//+ "		ing the late 8th to late 11th centuries. [1] [2] The term is"
//+ "		Ireland , France , Kievan Rus’ and Sicily . [3]"
//+ "		versa. [7] [8] [9] The form also occurs as a personal name on"
//+ ""
//+ ""
//+ "	**************************** Page 2****************************"
//+ "		  2 HISTORY"
//+ "		  any negative connotation in the term before the end of the Viking Age. Another etymology (supported by, among others, the "
//+ "		  viking from the same root as ON vika , f. ‘sea mile’, originally ‘the distance between two shifts of rowers’, from the root *weik or *wîk, as in the Proto-Germanic verb *wîkan, ‘to recede’. This is found in the Proto- Nordic verb *wikan, ‘to turn’, similar to Old Icelandic víkja (ýkva, víkva) ‘to move, to turn’, with well-attested "
//+ "		  attested, [11] and the term most likely predates the use of the sail by the Germanic peoples of North-Western Eu- rope,becausetheOldFrisianspellingshowsthattheword waspronouncedwithapalatalkandthusinallprobability existed in North-Western Germanic before that palatal- ization happened, that is, in the 5th century or before (in "
//+ "		  seemstobethatthetiredrowermovesasidefortherested roweronthethwart whenherelieves him. The OldNorse feminine víking (as in the phrase fara í víking) may origi- nallyhavebeenaseajourneycharacterizedbytheshifting of rowers, i.e. a long-distance sea journey, because in the pre-sailera,theshiftingofrowerswoulddistinguishlong- distance sea journeys. A víkingr (the masculine) would then originally have been a participant on a sea journey characterized by the shifting of rowers. In that case, the word Viking was not originally connected to Scandina- vian seafarers but assumed this meaning when the Scan- "
//+ "		  In OldEnglish ,theword wicing appearsfirstinthe Anglo- Saxon poem, Widsith , which probably dates from the 9th century. In Old English, and in the history of the arch- bishops of Hamburg-Bremen written by Adam of Bre- men in about 1070, the term generally referred to Scan- dinavian pirates or raiders. As in the Old Norse usages, the term is not employed as a name for any people or cul- tureingeneral. Theworddoesnotoccurinanypreserved Middle English texts. The word Viking was introduced into Modern English during the 18th-century Viking re- vival, atwhichpointitacquiredromanticisedheroicover- tones of \" barbarian warrior” or noble savage. During the 20th century, the meaning of the term was expanded to refer not only to seaborne raiders from Scandinavia and other places settled by them (like Iceland and the Faroe Islands), but secondarily to any member of the culture that produced said raiders during the period from the late 8thtothemid-11thcenturies, ormorelooselyfromabout 700 to as late as about 1100. As an adjective, the word is used to refer to ideas, phenomena, or artefacts con- nected with those people and their cultural life, produc- ing expressions like Viking age , Viking culture , Viking art ,"
//+ "		  1.1 Other names "
//+ "		  The Vikings were known as Ascomanni “ashmen” by the "
//+ "		  by the Gaels , [17] and Dene by the Anglo-Saxons. [18] "
//+ "		  the Rus’ or Rhōs , [19] probably derived from various uses of rōþs- , “related to rowing”, or derived from the area of Roslagen in east-central Sweden, where most of the Vikings who visited the Slavic lands came from. Some archaeologists and historians of today believe that these Scandinavian settlements in the Slavic lands played a sig- nificant role in the formation of the Kievan Rus’ federa- "
//+ "		  Belarus . [20] [21] [22] The modern day name for Sweden in several neighbouring countries is possibly derived from rōþs- , Ruotsi in Finnish and Rootsi in Estonian. TheSlavsandtheByzantinesalsocalledthem Varangians ( Russian : варяги, from Old Norse Væringjar , meaning “sworn men”, from vàr - “confidence, vow of fealty,” re- lated to Old English wær “agreement, treaty, promise,” "
//+ "		  bodyguardsofthe Byzantineemperors wereknownasthe Varangian Guard . Anglo-Scandinavian is an academic term referring to the people, and archaeological and historical periods during the 8th to 13th centuries in which there was migration to - and occupation of - the British Isles by Scandina- vian peoples generally known in English as Vikings. It is used in distinction from Anglo-Saxon . Similar terms ex- ist for other areas, such as Hiberno-Norse for Ireland and Scotland . "
//+ "		  2 History "
//+ "		  2.1 Viking Age "
//+ "		  Main article: Viking Age "
//+ "		  The period from the earliest recorded raids in the 790s until the Norman conquest of England in 1066 is commonly known as the Viking Age of Scandinavian "
//+ "		  Sea for sea routes to the south. The Normans were de- scended from Vikings who were given feudal overlord- ship of areas in northern France—the Duchy of Nor- mandy —in the 10th century. In that respect, descen- dants of the Vikings continued to have an influence in northern Europe. Likewise, King Harold Godwinson , the last Anglo-Saxon king of England, had Danish ancestors. Two Vikings even ascended to the throne of England, with Sweyn Forkbeard claiming the English throne from 1013 - 1014 and his son Cnut the Great becoming king"
//+ "		  recognized etymologist Anatoly Liberman [10] ) derives"
//+ "		Germansforthe ash woodoftheirboats, [16] Lochlannach"
//+ "		nautical usages. [11] Linguistically, this theory is better"
//+ "		the western branch). [12] [13] In that case, the idea behind it"
//+ "		dinavians begun to dominate the seas. [14]"
//+ "		The Slavs , the Arabs and the Byzantines knew them as"
//+ "		tion, and hence the names and early states of Russia and"
//+ "		Old High German wara “faithfulness” [19] ). Scandinavian"
//+ "		Viking religion , Viking ship , and so on. [15]"
//+ "		history. [23] Vikings used the Norwegian Sea and Baltic"
//+ "		of England from 1016 - 1035. [24] [25] [26] [27] [28]"
//+ ""
//+ ""
//+ "	**************************** Page 3****************************"
//+ "		  2.2 Viking expansion 3"
//+ "		  Geographically, a Viking Age may be assigned not only to Scandinavian lands (modern Denmark, Norway and Sweden), but also to territories under North Germanic dominance, mainly the Danelaw , including Scandinavian York , the administrative centre of the remains of the "
//+ "		  Anglia . [30] Viking navigators opened the road to new lands to the north, west and east, resulting in the founda- "
//+ "		  and Faroe Islands ; Iceland ; Greenland ; [31] and L'Anse "
//+ "		  circa 1000. [32] They may have been deliberately sought out, perhaps on the basis of the accounts of sailors who had seen land in the distance. The Greenland settlement "
//+ "		  Viking dynasty also took control of territories in Slavic and Finno-Ugric -dominated areas of Eastern Europe, an- nexed Kiev in 882 to serve as the capital of the Kievan "
//+ "		  As early as 839, when Swedish emissaries are first known to have visited Byzantium, Scandinavians served as mer- "
//+ "		  late 10th century, a new unit of the imperial bodyguard formed. Traditionally containing large numbers of Scan- dinavians, it was known as the Varangian Guard. The word Varangian may have originated in Old Norse, but in Slavic and Greek it could refer either to Scandinavians or Franks. The most eminent Scandinavian to serve in the Varangian Guard was Harald Hardrada , who subse- quently established himself as king of Norway (1047– 66). Important trading ports during the period include Birka , Hedeby , Kaupang , Jorvik , Staraya Ladoga , Novgorod , and Kiev . There is archaeological evidence that Vikings reached "
//+ "		  regularly plied the Volga with their trade goods: furs, tusks, seal fat for boat sealant, and slaves . Generally speaking, the Norwegians expanded to the north and west to places such as Ireland, Scotland, Ice- land, and Greenland; the Danes to England and France, settling in the Danelaw (northern/eastern England) and Normandy ; and the Swedes to the east, founding the Kievan Rus , the original Russia. Among the Swedish runestones mentioning expeditions overseas, almost half tell of raids and travels to western Europe. According to the Icelandic sagas, many Norwegian Vikings also went to eastern Europe. In the Viking Age, the present day na- tions of Norway, Sweden and Denmark did not exist, but were largely homogeneous and similar in culture and lan- guage, although somewhat distinct geographically. The names of Scandinavian kings are reliably known only for the later part of the Viking Age. After the end of the Viking Age the separate kingdoms gradually acquired distinct identities as nations, which went hand-in-hand with their Christianization . Thus the end of the Viking"
//+ "		  Age for the Scandinavians, also marks the start of their relatively brief Middle Ages. "
//+ "		  2.2 Viking expansion "
//+ "		  Main article: Viking expansion The Vikings explored the northern islands and coasts "
//+ "		  Travels of the Vikings "
//+ "		  of the North Atlantic, ventured south to North Africa and east to Russia, Constantinople, and the Middle "
//+ "		  mercenaries. [38] Vikings under Leif Ericson , heir to Erik the Red , reached North America and set up short-lived settlements in present-day L'Anse aux Meadows , New- foundland, and Labrador, Canada. Viking expansion into continental Europe was limited. Their realm was bordered by powerful cultures to the south. Early on it was the Saxons , who occupied Old Saxony , located in what is now Northern Germany. The Saxons were a fierce and powerful people and were of- ten in conflict with the Vikings. To counter the Saxon aggression and solidify their own presence, the Danes "
//+ "		  in and around Hedeby . [39] The Vikings soon witnessed the violent subduing of the Saxons by Charlemagne , in the thirty-year Saxon Wars in 772-804. The Saxon de- feat resulted in their forced christening and absorption of Old Saxony in the Carolingian Empire . Fearing the Franks , this led the Vikings to further expand Danevirke and the defence constructions were in use throughout the "
//+ "		  of the Baltic Sea was ruled by the Obotrites , a federa- tion of Slavic tribes loyal to the Carolingians and later the Frankish empire . The Vikings – led by King Gudfred – destroyed the Obotrite city of Reric on the southern Baltic coast in 808 AD and transferred the merchants and traders to Hedeby. This secured their supremacy in the Baltic Sea, which remained throughout the Viking Age."
//+ "		  Kingdom of Northumbria , [29] parts of Mercia , and East"
//+ "		tion of independent settlements in the Shetland , Orkney ,"
//+ "		aux Meadows , a short-lived settlement in Newfoundland ,"
//+ "		eventually died out, possibly due to climate change . [33]"
//+ "		Rus’ . [34]"
//+ "		cenaries in the service of the Byzantine Empire. [35] In the"
//+ "		East. [21] [22] [37] They raided and pillaged, but also en-"
//+ "		gagedintrade,settledwide-rangingcolonies,andactedas"
//+ "		Baghdad , the centre of the Islamic Empire . [36] The Norse"
//+ "		constructed the huge defence fortification of Danevirke"
//+ "		Viking Age and even up until 1864. [40] The south coast"
//+ ""
//+ ""
//+ "	**************************** Page 4****************************"
//+ "		  4 3 CULTURE "
//+ "		  2.2.1 Motives "
//+ "		  The motives driving the Viking expansion are a topic of much debate in Nordic history. One common the- ory posits that Charlemagne “used force and terror to Christianise all pagans”, leading to baptism, conversion "
//+ "		  gans resisted and wanted revenge. [41] [42] [43] [44] [45] Pro- fessor Rudolf Simek states that “it is not a coincidence if the early Viking activity occurred during the reign of"
//+ "		  Scandinavia led to serious conflict dividing Norway for "
//+ "		  Another explanation is that the Vikings exploited a mo- ment of weakness in the surrounding regions. England suffered from internal divisions and was relatively easy prey given the proximity of many towns to the sea or to navigable rivers. Lack of organised naval opposi- tion throughout Western Europe allowed Viking ships to travel freely, raiding or trading as opportunity permitted. The decline in the profitability of old trade routes could also have played a role. Trade between western Europe and the rest of Eurasia suffered a severe blow when the "
//+ "		  of Islam in the 7th century had also affected trade with "
//+ "		  Raids in Europe, including raids and settlements from Scandinavia, were not something new and also seen long before the Vikings came. The Jutes invaded the British Isles three centuries earlier, pouring out from Jutland during the Age of Migrations , before the Danes settled there. The Saxons and the Angles did the same, embark- ing from mainland Europe. The Viking raids were the first to be documented in writing by eyewitnesses, and they were much larger in scale and frequency than in pre- "
//+ "		  2.3 End of the Viking Age "
//+ "		  During the Viking Age, Scandinavian men and women travelled to many parts of Europe and beyond, in a cul- tural diaspora that left its traces from Newfoundland to Byzantium . This period of energetic activity also had a pronounced effect in the Scandinavian homelands, which "
//+ "		  300 years from the late 8th century, when contemporary chroniclers first commented on the appearance of Viking raiders, to the end of the 11th century, Scandinavia un- derwent profound cultural changes. By the late 11th century, royal dynasties legitimised by the Catholic Church (which had had little influence in Scandinavia 300 years earlier) were asserting their power with increasing authority and ambition, and the three kingdoms of Denmark, Norway, and Sweden had taken shape. Towns appeared that functioned as secular and ec- clesiastical administrative centres and market sites, and"
//+ "		  Blar a' Bhuailte , site of the Vikings’ last stand in Skye "
//+ "		  monetary economies began to emerge based on English "
//+ "		  silver from the East had been absent for more than a cen- tury, and the flow of English silver had come to an end in "
//+ "		  Denmark and Norway with the establishment of dioceses during the 11th century, and the new religion was begin- ning to organise and assert itself more effectively in Swe- den. Foreign churchmen and native elites were energetic in furthering the interests of Christianity, which was now no longer operating only on a missionary footing, and old ideologies and lifestyles were transforming. By 1103, the first archbishopric was founded in Scandinavia, at Lund , Scania, then part of Denmark. The assimilation of the nascent Scandinavian kingdoms into the cultural mainstream of European Christendom altered the aspirations of Scandinavian rulers and of Scandinavians able to travel overseas, and changed their relations with their neighbours. One of the primary sources of profit for the Vikings had been slave-taking. The medieval Church held that Christians should not own fellow Christians as slaves, so chattel slavery diminished as a practice throughout northern Europe. This took much of the economic incentive out of raiding, though sporadic slaving activity continued into the 11th cen- tury. Scandinavian predation in Christian lands around the North and Irish Seas diminished markedly. The kings of Norway continued to assert power in parts of northern Britain and Ireland, and raids continued into the 12th century, but the military ambitions of Scandi- navian rulers were now directed toward new paths. In 1107, Sigurd I of Norway sailed for the eastern Mediter- raneanwithNorwegiancrusaderstofightforthenewlyes- tablished Kingdom of Jerusalem , and Danes and Swedes participated energetically in the Baltic Crusades of the "
//+ "		  3 Culture "
//+ "		  A variety of sources illuminate the culture, activities, and beliefs of the Vikings. Although they were generally a"
//+ "		  or execution, and as a result, Vikings and other pa-"
//+ "		Charlemagne”. [41] [46] ThepenetrationofChristianityinto"
//+ "		almost a century. [47]"
//+ "		Roman Empire fell in the 5th century. [48] The expansion"
//+ "		western Europe. [49]"
//+ "		vious times. [50]"
//+ "		and German models. [52] By this time the influx of Islamic"
//+ "		the mid-11th century. [53] Christianity had taken root in"
//+ "		were subject to a variety of new influences. [51] In the"
//+ "		12th and 13th centuries. [54]"
//+ ""
//+ ""
//+ "	**************************** Page 5****************************"
//+ "		  3.1 Literature and language 5"
//+ "		  non-literate culture that produced no literary legacy, they had an alphabet and described themselves and their world on runestones . Most contemporary literary and written sources on the Vikings come from other cultures that "
//+ "		  tury, archaeological findings have built a more complete "
//+ "		  The archaeological record is particularly rich and varied, providing knowledge of their rural and urban settlement, craftsandproduction,shipsandmilitaryequipment,trad- ing networks, as well as their pagan and Christian reli- gious artefacts and practices. "
//+ "		  3.1 Literature and language "
//+ "		  See also: Old Norse and The Norse Sagas The most important primary sources on the Vikings are"
//+ "		  native written sources begin to appear, in Latin and Old Norse. In the Viking colony of Iceland, an extraordi- nary vernacular literature blossomed in the 12th through 14th centuries, and many traditions connected with the Viking Age were written down for the first time in the Icelandic sagas . A literal interpretation of these medieval prose narratives about the Vikings and the Scandinavian past is of course doubtful, but many specific elements re- main worthy of consideration, such as the great quantity of skaldic poetry attributed to court poets of the 10th and 11th centuries, the exposed family trees, the self images, the ethical values, all included in these literary writings. Indirectly the Vikings have also left a window open to their language, culture and activities, through many Old Norse place names and words, found in their former sphere of influence. Some of these place names and words are still in direct use today, almost unchanged, and sheds light on where they settled and what spe- cific places meant to them, as seen in place names like Egilsay (from Eigils Ø meaning Eigil’s Island), Ormskirk (from Ormr kirkja meaning Orms Church or Church of the Worm), Meols (from merl meaning Sand Dunes), Snaefell (Snow Fell), Ravenscar (Ravens Rock), Vinland (Land of Wine or Land of Winberry ), Kaupanger (Mar- ket Harbour) and Tórshavn (Thor’s Harbour) or the re- ligious centre of Odense , meaning a place where Odin was worshipped. It is also evident in concepts like the present day Tynwald on the Isle of Man . Common words in everyday English language, like some of the week- days ( Thursday means Thor’s day), axle , crook , raft , knife , plough , leather , bylaw , thorp , skerry , ombudsman , husband , heathen , Hell , Norman and ransack stem from the Old Norse of the Vikings, and give us an opportu- nity to understand their interactions with the people and"
//+ "		  One of the few surviving manuscript leaves from the Heimskringla Sagas , written by Snorri Sturluson c. 1260. The leaf tells of King Ólafur . "
//+ "		  contemporary texts from Scandinavia and regions where "
//+ "		  introduced to Scandinavia with Christianity, so there are few native documentary sources from Scandinavia before "
//+ "		  vians did write inscriptions in runes , but these are usually very short and formulaic. Most contemporary documen- tary sources consist of texts written in Christian and Is- lamic communities outside Scandinavia, often by authors who had been negatively affected by Viking activity. Later writings on the Vikings and the Viking Age can also be important for understanding them and their cul- ture, although they need to be treated cautiously. After the consolidation of the church and the assimilation of Scandinavia and its colonies into the mainstream of me- dieval Christian culture in the 11th and 12th centuries,"
//+ "		  Shetland and Orkney, Old Norse completely replaced the local languages and over time evolved into the now ex- tinct Norn language . Some modern words and names only emerge and contribute to our understanding after a moreintenseresearchoflinguisticsourcesfrommedieval or later records, such as York (Horse Bay), Swansea ( Sveinn 's Isle) or some of the place names in Northern "
//+ "		  etymological studies continue to provide a vital source of information on the Viking culture, their social structure and history and how they interacted with the people and cultures they met, traded, attacked or lived with in over- "
//+ "		  place names on the west coast of southern France might "
//+ "		  Taillebourg ( Trelleborg , meaning City of Thralls or Cas- "
//+ "		  chaeology at present. [67] A lot of Old Norse connections "
//+ "		  Norwegian , Danish , Faroese and Icelandic . [68] Old Norse did not exert any great influence on the Slavic languages in the Viking settlements of Eastern Europe. It has been"
//+ "		  were in contact with them. [55] Since the mid-20th cen-"
//+ "		and balanced picture of the lives of the Vikings. [56] [57]"
//+ "		cultures of the British Isles. [60] In the Northern Isles of"
//+ "		the Vikings were active. [58] Writing in Latin letters was"
//+ "		the late 11th and early 12th centuries. [59] The Scandina-"
//+ "		France like Tocqueville (Toki’s farm). [61] Linguistic and"
//+ "		seas settlements. [62] [63] It has been speculated that several"
//+ "		also stem from Viking activities. [64] Place names like"
//+ "		tle of Thralls) exist as far south as the Charente River . [65]"
//+ "		Gascony and vicinity [66] is an active area of Viking ar-"
//+ "		are evident in the modern-day languages of Swedish ,"
//+ ""
//+ ""
//+ "	**************************** Page 6****************************"
//+ "		  6 3 CULTURE"
//+ "		  speculated that the reason was the great differences be- tweenthetwolanguages, combinedwiththeRus’Vikings more peaceful businesses in these areas and the fact that they were outnumbered. The Norse named some of the rapids on the Dnieper , but this can hardly be seen from "
//+ "		  A consequence of the available written sources, which may have coloured how we perceive the Viking Age as a historical period, is that we know a lot more of the Vikings’ activities in western Europe than in the East. One reason is that the cultures of north-eastern Europe at the time were non-literate, and did not produce a legacy of literature. Another is that the vast majority of written sourcesonScandinaviaintheVikingAgecomefromIce- land, a nation originally settled by Norwegian colonists. As a result, there is much more material from the Viking Age concerning Norway than Sweden, which apart from many runic inscriptions, has almost no written sources from the early Middle Ages. "
//+ "		  3.1.1 Runestones "
//+ "		  Main article: Runestone The Viking peoples could read and write and used a non-"
//+ "		  century, used in parallel with the Latin alphabet. The majority of runic inscriptions from the Viking pe- riod are found in Sweden and date from the 11th century. TheoldeststonewithrunicinscriptionswasfoundinNor- way and dates to the 4th century, suggesting that runic in- scriptions pre-date the Viking period. Many runestones inScandinaviarecordthenamesofparticipantsinViking expeditions, such as the Kjula runestone that tells of ex- tensivewarfareinWesternEuropeandthe TuringeRune- stone , which tells of a war band in Eastern Europe. Other runestones mention men who died on Viking expedi- tions. Among them are around 25 Ingvar runestones in the Mälardalen district of Sweden, erected to commemo- rate members of a disastrous expedition into present-day Russia in the early 11th century. Runestones are impor- tant sources in the study of Norse society and early me- dieval Scandinavia, not only of the Viking segment of the"
//+ "		  Runic inscriptions on the larger of the Jelling Stones "
//+ "		  The Jelling stones date from between 960 and 985. The older, smaller stone was raised by King Gorm the Old , thelastpagankingofDenmark, asamemorialhonouring "
//+ "		  Harald Bluetooth , to celebrate the conquest of Denmark and Norway and the conversion of the Danes to Chris- tianity. It has three sides: one with an animal image, one with an image of the crucified Jesus Christ, and a third bearing the following inscription: "
//+ "		  KingHaraldrorderedthismonumentmade in memory of Gormr, his father, and in mem- ory of Thyrvé, his mother; that Haraldr who won for himself all of Denmark and Norway "
//+ "		  (as Langobardland), [78] Serkland (i.e. the Muslim "
//+ "		  ous places in Eastern Europe. Viking Age inscriptions have also been discovered on the Manx runestones on the Isle of Man."
//+ "		  The Lingsberg Runestone , Sweden "
//+ "		  standardizedalphabet, called runor , builtuponsoundval- ues. While there are few remains of runic writing on pa- per from the Viking era, thousands of stones with runic inscriptions have been found where Vikings lived. They areusuallyinmemoryofthedead, thoughnotnecessarily placed at graves. The use of runor survived into the 15th"
//+ "		  3.2 Burial sites "
//+ "		  See also: Norse funeral and Ship burial "
//+ "		  There are numerous burial sites associated with Vikings throughout Europe and their sphere of influence – in"
//+ "		  the modern names. [69] [70]"
//+ "		population. [71]"
//+ "		Queen Thyre . [72] The larger stone was raised by his son,"
//+ "		and made the Danes Christian. [73]"
//+ "		Runestones attest to voyages to locations such as"
//+ "		Bath , [74] Greece, [75] Khwaresm , [76] Jerusalem , [77] Italy"
//+ "		world), [79] England [80] (including London [81] ), and vari-"
//+ ""
//+ ""
//+ "	**************************** Page 7****************************"
//+ "		  3.2 Burial sites 7 "
//+ "		  Scandinavia, the British Isles, Ireland, Greenland, Ice- land, Faeroe Islands, Germany, The Baltic, Russia, etc.. The burial practices of the Vikings were quite varied, from dug graves in the ground, to tumuli , sometimes in- cluding so-called ship burials. "
//+ "		  The ship prow"
//+ "		  Burial mounds at Gamla Uppsala"
//+ "		  Details of the burial ship from Oseberg . On exhibit at Viking Ship Museum , Oslo. "
//+ "		  According to written sources, most of the funerals took place at sea. The funerals involved either burial or cremation , depending on local customs. In the area that is now Sweden, cremations were predominant, in Den- mark burial was more common, and in Norway both"
//+ "		  Some of the funerary stone settings at Lindholm Høje Examples of Viking burial mounds and stone set graves, collectively known as tumuli."
//+ "		  mary source of evidence for circumstances in the Viking "
//+ "		  cation as to what was considered important to possess in "
//+ "		  services were given to dead children by the Vikings. [85] Some of the burial sites that are most important to our understanding of the Vikings include: • Norway: Oseberg ; Gokstad ; Borrehaugene . • Sweden: Gettlinge gravfält; the cemeteries of Birka ,"
//+ "		  Head post from the ship"
//+ "		  psala ; Hulterstad gravfält, near Alby ; Hulterstad, "
//+ "		  Öland . • Denmark: Jelling , a World Heritage Site ; Lindholm "
//+ "		  hoard. • Scotland: Port an Eilean Mhòir ship burial ; Scar "
//+ "		  • Faroe Islands: Hov . • Iceland: Mosfellsbær in Capital Region ; [87] [88] "
//+ "		  Húnavatnssýsla . [82] [89] [90] • Greenland: Brattahlíð . [91] • Germany: Hedeby . • Latvia: Grobiņa . • Ukraine: the Black Grave . • Russia: Gnezdovo ."
//+ "		  were common. [82] Viking barrows are one of the pri-"
//+ "		Age. [83] The items buried with the dead give some indi-"
//+ "		the afterlife. [84] We do not have any idea what mortuary"
//+ "		a World Heritage Site; [86] Valsgärde ; Gamla Up-"
//+ "		Høje ; Ladby ship ; Mammen chamber tomb and"
//+ "		boat burial , Orkney."
//+ "		the boat burial in Vatnsdalur, Austur-"
//+ ""
//+ ""
//+ "	**************************** Page 8****************************"
//+ "		  8 3 CULTURE"
//+ "		  3.3 Ships "
//+ "		  Main article: Viking ships "
//+ "		  There have been several archaeological finds of Viking ships of all sizes, providing knowledge of the craftsman- ship that went into building them. There were many types of Viking ships, built for various uses; the best- "
//+ "		  intended for warfare and exploration, designed for speed and agility, and were equipped with oars to complement the sail, making navigation possible independently of the wind. The longship had a long, narrow hull and shallow draught to facilitate landings and troop deployments in shallow water. Longships were used extensively by the Leidang , the Scandinavian defence fleets. The longship allowed the Norse to go Viking , which might explain why this type of ship has become almost synonymous with the "
//+ "		  The reconstructed longship Sea Stallion. "
//+ "		  A model of the knarr ship type. The longship facilitated far-reaching expeditions, but the Vikings also constructed several other types of ships. "
//+ "		  The Vikings built many unique types of watercraft, often used for more peaceful tasks. The knarr was a dedicated merchant vessel designed to carry cargo in bulk. It had a broader hull, deeper draught, and a small number of oars (used primarily to manoeuvre in harbours and similar sit- uations). One Viking innovation was the ' beitass ', a spar mounted to the sail that allowed their ships to sail effec- "
//+ "		  Viking ships to tow or carry a smaller boat to transfer crews and cargo from the ship to shore. Ships were an integral part of the Viking culture. They"
//+ "		  facilitated everyday transportation across seas and wa- terways, exploration of new lands, raids, conquests, and trade with neighbouring cultures. They also held a ma- jor religious importance. People with high status were sometimes buried in a ship along with animal sacrifices, weapons, provisions and other items, as evidenced by the "
//+ "		  the excavated ship burial at Ladby in Denmark. Ship burials were also practised by Vikings abroad, as evi- denced by the excavations of the Salme ships on the Es- "
//+ "		  Well-preserved remains of five Viking ships were exca- vated from Roskilde Fjord in the late 1960s, representing both the longship and the knarr. The ships were scuttled there in the 11th century, to block a navigation channel and thus protect Roskilde , then the Danish capital, from seaborne assault. The remains of these ships are on dis- play at the Viking Ship Museum in Roskilde . "
//+ "		  3.4 Everyday life "
//+ "		  A reconstructed Viking house in Iceland. "
//+ "		  A reconstructed Viking Age long house , at Fyrkat , Denmark. "
//+ "		  3.4.1 Social structure "
//+ "		  The Viking society was divided into the three socio- economic classes of Thralls, Karls and Jarls. This is de- scribed vividly in the Eddic poem of Rigsthula , which also explains that it was the God Ríg - father of mankind"
//+ "		  known type is probably the longship . [92] Longships were"
//+ "		concept of Vikings. [93] [94]"
//+ "		buried vessels at Gokstad and Oseberg in Norway [96] and"
//+ "		tonian island of Saaremaa . [97]"
//+ "		tively against the wind. [95] It was common for seafaring"
//+ ""
//+ ""
//+ "	**************************** Page 9****************************"
//+ "		  3.4 Everyday life 9 "
//+ "		  were wealthy and owned large estates with huge long- houses, horses and many thralls. The thralls or servants took care of most of the daily chores, while the Jarls engaged in administration, politics, hunting, sports, paid visits to other Jarls or were abroad on expeditions. When a Jarl died and was buried, his household thralls were sometimes sacrificially killed and buried next to him, as "
//+ "		  In daily life, there were many intermediate positions in the overall social structure and it is believed that there must have been some social mobility. These details are unclear, but titles and positions like hauldr , thegn , land- mand , show mobility between the Karls and the Jarls."
//+ "		  A large reconstructed chieftains longhouse at Lofotr Viking Mu- seum , Norway."
//+ "		  Other social structures included the communities of félag in both the civil and the military spheres, to which its members (called félagi ) were obliged. A félag could be centred around certain trades, a common ownership of a sea vessel or a military obligation under a specific leader. Members of the latter were referred to as drenge ; one of the words for warrior. There were also official commu- nities within towns and villages, the overall defence, reli- gion, the legal system and the Things . Women had a relatively free status in the Nordic coun- tries of Sweden, Denmark and Norway, illustrated in the"
//+ "		  Reconstructed town houses from Haithabu (now in Germany). "
//+ "		  also known as Heimdallr - who created the three classes. "
//+ "		  Thralls were the lowest ranking class and were slaves. Slavery was of vital importance to Viking society, for everyday chores and large scale construction and also to trade and economy. Thralls were used as servants and workers in the farms and larger households of the Karls and Jarls and they were used for constructing fortresses, fortifications, ramps, canals, mounds, roads and similar hard work projects. According to the Rigsthula, Thralls were despised and looked down upon. New thralls were suppliedbyeitherthesonsanddaughtersofthrallsorthey were captured abroad. The Vikings often deliberately captured many people on their raids in Europe, enslaved and made them into thralls. The new thralls were then brought back home to Scandinavia by boat, used on lo- cation or in newer settlements to build needed structures or sold, often to the Arabs in exchange for silver. Other names for thrall were 'træl' and 'ty'. Karls were free peasants. They owned farms, land and cattle and engaged in daily chores like ploughing the fields, milkingthecattle, buildinghousesandwagons, but employed thralls to make ends meet. Other names for Karls were 'bonde' or simply free men. TheJarlswerethe aristocracy oftheVikingsociety. They"
//+ "		  Gulating laws. [100] The paternal aunt, paternal niece and "
//+ "		  the right to inherit property from a deceased man. [100] In the absence of male relatives, an unmarried woman with no son could, inherit not only property but also the po- sition as head of the family from a deceased father or brother. Such a woman was referred to as Baugrygr , and she exercised all the rights afforded to the head of a fam- ily clan, such as the right to demand and receive fines for the slaughter of a family member, until she married, by "
//+ "		  ter the age of 20, an unmarried woman, referred to as maer and mey , reached legal majority and had the right "
//+ "		  her own person before the law. [100] An exception to her independence was the right to choose a marriage part- "
//+ "		  Widows enjoyed the same independent status as unmar- ried women. A married woman could divorce her hus- "
//+ "		  free woman to cohabit with a man and have children with him without marrying him, even if that man was mar- "
//+ "		  There was no distinction made between children born inside or outside of marriage: both had the right to in- herit property after their parents, and there was no “le- "
//+ "		  gious authority and were active as priestesses ( gydja ) and "
//+ "		  and medicine women. [103] These liberties gradually dis- appeared after the introduction of Christianity, and from"
//+ "		  many excavations have revealed. [99]"
//+ "		Icelandic Grágás and the Norwegian Frostating laws and"
//+ "		paternal granddaughter, referred to as odalkvinna , all had"
//+ "		Archaeology has confirmed this social structure. [98]"
//+ "		which her rights were transferred to her husband. [100] Af-"
//+ "		to decide of her place of residence and was regarded as"
//+ "		ner, as marriages were normally arranged by the clan. [101]"
//+ "		bandandremarry. [102] Itwasalsosociallyacceptablefora"
//+ "		ried: a woman in such a position was called frilla . [102]"
//+ "		gitimate” or “illegitimate” children. [102] Women had reli-"
//+ "		oracles ( sejdkvinna ); [103] they were active within art as"
//+ "		poets ( skalder ) [103] and rune masters , and as merchants"
//+ ""
//+ ""
//+ "	**************************** Page 10****************************"
//+ "		  10 3 CULTURE"
//+ "		  the late 13th-century, they are no longer mentioned. [100] "
//+ "		  3.4.2 Appearances"
//+ "		  Certain livestock were typical and unique to the Vikings, including the Icelandic horse , Icelandic cattle , a plethora "
//+ "		  goose . [112] [113] The Vikings in York mostly ate beef, mutton , and pork with small amounts of horse meat. Most of the beef and horse leg bones were found split lengthways, to get out the marrow. The mutton and swine were cut into leg and shoulder joints and chops. The fre- quent remains of pig skull and foot bones found on house floors indicate that brawn and trotters were also popu- lar. Hens were kept for both their meat and eggs, and the bones of game birds such as the black grouse , golden"
//+ "		  Typical jewellery worn by women of the Karls and Jarls. Orna- mented silver brooches, coloured glass-beads and amulets. "
//+ "		  The three classes were easily recognisable by their ap- pearances. Men and women of the Jarls were well groomed with neat hairstyles and expressed their wealth and status by wearing expensive clothes (often silk) and well crafted jewellery like brooches , belt buckles, neck- laces and arm rings. Almost all of the jewellery was craftedinspecificdesignsuniquetotheNorse(see Viking art ). Fingerringswereseldomusedandearringswerenot used at all, as they were seen as a Slavic phenomenon. Most Karls expressed similar tastes and hygiene, but in a "
//+ "		  3.4.3 Farming and cuisine "
//+ "		  The Sagas tell us about the diet and cuisine of the "
//+ "		  middens and garbage dumps have proved to be of great valueandimportance. Undigestedremainsofplantsfrom cesspits at Coppergate in York have provided a lot of information in this respect. Overall, archaeo-botanical investigations have been undertaken increasingly in re- cent decades, as a collaboration between archaeologists and palaeoethno-botanists. This new approach sheds new light on the agricultural and horticultural practices of the "
//+ "		  When the information from various sources are put to- gether, a picture of a diverse cuisine, with lots of differ- ent ingredients emerges. Meat products of all kinds, such "
//+ "		  consumed. [108] There were plenty of seafood, bread, por- ridges, dairy products, vegetables, fruits, berries and nuts. Alcoholic drinks like beer , mead , bjórr (a strong fruit wine) and, for the rich, imported wine , were"
//+ "		  Seafood was an important part of the diet, in some places even more so than meat. Whales and walrus were hunted for food in Norway and the north-western parts of the North Atlantic region, and seals were hunted nearly everywhere. Oysters , mussels and shrimps were eaten in large quantities and cod and salmon were pop- ular fish. In the southern regions, herring was also "
//+ "		  Milk and buttermilk were popular, both as cooking in- gredients and drinks, but were not always available, even "
//+ "		  with priorities varying from location to location, [119] and fermented milk products like skyr or surmjölk were pro- "
//+ "		  Food was often salted and enhanced with spices, some of which were imported like black pepper , while others were cultivated in herb gardens or harvested in the wild. Home grown spices that were used included caraway , mustard and horseradish as evidenced from the Oseberg "
//+ "		  found during the archaeological examinations of cesspits atCoppergateinYork. Thyme , juniperberry , sweetgale , yarrow , rue and peppercress werealsousedandcultivated "
//+ "		  Vikings collected and ate fruits, berries and nuts. Apple (wild crab apples ), plums and cherries were part of the "
//+ "		  blackberry , elderberry , rowan , hawthorn andvariouswild "
//+ "		  important part of the diet in general and large amounts of walnut shells have been found in cities like Hedeby. The shells were used for dyeing and it is assumed the nuts "
//+ "		  Theinventionandintroductionofthe mouldboardplough revolutionized agriculture in Scandinavia in the early Viking Age and made it possible to farm even the poor soils. In Ribe ,grainsof rye , barley , oat and wheat datedto the 8th century have been found and examined, and these "
//+ "		  and flour were used for making porridges, some cooked with milk, some cooked with fruit and sweetened with honey,andalsovariousformsofbread. Remainsofbread from primarily Birka in Sweden were made of barley and"
//+ "		  of sheep breeds, [111] the Danish hen and the Danish"
//+ "		plover , wild ducks, and geese have also been found. [114]"
//+ "		important. [115] [116] [117]"
//+ "		more relaxed and inexpensive way. [98] [104]"
//+ "		at farms. [118] The milk came from cows, goats and sheep,"
//+ "		duced as well as butter and cheese. [120]"
//+ "		ship burial [109] or dill , coriander , and wild celery , as"
//+ "		Vikings, [105] butfirsthandevidence, like cesspits , kitchen"
//+ "		Vikings and therefore also on their cuisine. [106]"
//+ "		as cured , smoked and whey -preserved meat, [107] sausages"
//+ "		and boiled or fried fresh meat cuts, were prepared and"
//+ "		served. [109] [110]"
//+ "		in herb gardens. [106] [121]"
//+ "		diet, [122] aswere rosehips and raspberry , wildstrawberry ,"
//+ "		berries, specific to the locations. [121] Hazelnuts were an"
//+ "		were enjoyed as well. [106] [118]"
//+ "		are believed to have been cultivated locally. [123] Grains"
//+ ""
//+ ""
//+ "	**************************** Page 11****************************"
//+ "		  3.4 Everyday life 11"
//+ "		  wheat. ItisuncleariftheNorseleavenedtheirbreads,but "
//+ "		  Flax was a very important crop for the Vikings. For oil extraction, food consumption and most importantly the production of linen . More than 40% of all known textile recoveries from the Viking Age can be traced as linen. This suggests a much higher actual percentage, as linen is "
//+ "		  The quality of food for common people was not always particularly high. The research at Coppergate shows that theVikingsinYorkmadebreadfromwholemealflour— probably both wheat and rye - but with the seeds of corn- field weeds included. Corncockle ( Agrostemma ), would havemadethebreaddark-coloured,buttheseedsarepoi- sonous, and people who ate the bread might have become ill. Seeds of carrots, parsnip , and brassicas were also dis- covered, but they were poor specimens and tend to come "
//+ "		  rotary querns often used in the Viking Age inevitably left tiny stone fragments (often from basalt rock) in the flour andwheneatenlateron,thesesmallstonesworedownthe teeth. The effects of this can be seen on skeletal remains "
//+ "		  3.4.4 Sports "
//+ "		  Sports were widely practised and encouraged by the "
//+ "		  anddevelopingcombatskillswerepopular. Thisincluded spear and stone throwing, building and testing physical strength through wrestling , fist fighting , and stone lifting. In areas with mountains, mountain climbing was prac- tised as a sport. Agility and balance were built and tested byrunning andjumping forsport, andthere ismention of a sport that involved jumping from oar to oar on the out- side of a ship’s railing as it was being rowed. Swimming was a popular sport and Snorri Sturluson describes three types: diving, long-distance swimming and a contest in which two swimmers try to duck one another. Chil- dren often participated in some of the sport disciplines and women have also been mentioned as swimmers, al- though it is unclear if they took part in competition. King OlafTryggvason washailedasamasterofbothmountain climbing and oar-jumping, and was said to have excelled in the art of knife juggling as well. Skiing and ice skating were the primary winter sports of the Vikings, although skiing was also used as everyday means of transport in winter time and in the colder re- gions of the north. Horse fighting was practised for sport, although the rules are unclear. It appears to have involved two stallions pit- ted against each other, within smell and sight of fenced- off mares. Whatever the rules were, the fights often re- sulted in the death of one of the stallions. Icelandic sources refer to the sport of knattleik . A ball game akin to hockey , knattleik involved a bat and a small"
//+ "		  hard ball and was usually played on a smooth field of ice. The rules are unclear, but it was popular with both adults and children, even though it often led to injuries. Knat- tleik appears to have been played only in Iceland, where it attracted many spectators, as did horse fighting. Hunting, as a sport, was limited to Denmark, where it wasnotregardedasanimportantoccupation. Birds, deer , hares and foxes werehuntedwithbowandspear,andlater with crossbows. The techniques were stalking, snare and traps and par force hunting with dog packs. "
//+ "		  3.4.5 Games and entertainment "
//+ "		  Rook, Lewis chessmen , at the British Museum in London "
//+ "		  Both archaeological finds and written sources testify to the fact that the Vikings set aside time for social and fes- "
//+ "		  Board games and dice games were played as a popu- lar pastime, at all levels of society. Preserved gaming piecesandboardsshowgameboardsmadeofeasilyavail- able materials like wood, with game pieces manufactured"
//+ "		  their ovens and baking utensils suggest that they did. [124]"
//+ "		poorly preserved compared to wool for example. [125]"
//+ "		from white carrots and bitter tasting cabbages. [122] The"
//+ "		of that period. [124]"
//+ "		Vikings. [126] [127] Sports that involved weapons training"
//+ "		tive gatherings. [126] [127] [128]"
//+ ""
//+ ""
//+ "	**************************** Page 12****************************"
//+ "		  12 4 WEAPONS AND WARFARE"
//+ "		  from stone, wood or bone, while other finds include elab- orately carved boards and game pieces of glass, amber , antler or walrus tusk, together with materials of foreign origin, such as ivory . The Vikings played several types of tafl games; hnefatafl , nitavl ( Nine Men’s Morris ) and the less common kvatrutafl . Chess also appeared at the end of the Viking Age. Hnefatafl is a war game, in which the object is to capture the king piece – a large hostile army threatens and the king’s men have to protect the king. It was played on a board with squares using black and white pieces, with moves made according to dice rolls. The Ockelbo Runestone shows two men engaged in Hnefatafl, and the sagas suggest that money or valuables could have "
//+ "		  On festive occasions storytelling , skaldic poetry , music "
//+ "		  the atmosphere. [128] Music was considered an art form and music proficiency as fitting for a cultivated man. The Vikings are known to have played instruments including "
//+ "		  3.4.6 Experimental archaeology "
//+ "		  Experimental archaeology of the Viking Age is a flour- ishing branch and several places have been dedicated to this technique, such as Jorvik Viking Centre in United "
//+ "		  Denmark, Foteviken Museum [129] in Sweden or Lofotr Viking Museum in Norway. Viking-age reenactors have undertaken experimental activities such as iron smelting and forging using Norse techniques at Norstead in New- "
//+ "		  On 1 July 2007, the reconstructed Viking ship Skuldelev "
//+ "		  Roskilde to Dublin. The remains of that ship and four others were discovered during a 1962 excavation in the Roskilde Fjord. Tree-ring analysis has shown the ship was built of oak in the vicinity of Dublin in about 1042. Seventy multi-national crew members sailed the ship backtoitshome,and SeaStallion arrivedoutsideDublin’s Custom House on 14 August 2007. The purpose of the voyage was to test and document the seaworthiness, speed, and manoeuvrability of the ship on the rough open sea and in coastal waters with treacherous currents. The crew tested how the long, narrow, flexible hull withstood the tough ocean waves. The expedition also provided valuable new information on Viking longships and soci- ety. The ship was built using Viking tools, materials, and much the same methods as the original ship. Other vessels, often replicas of the Gokstad ship (full- or half-scale) or Skuldelev I have been built and tested as well. The Snorri (a Skuldelev I Knarr ), was sailed from"
//+ "		  4 Weapons and warfare "
//+ "		  Main article: Viking Age arms and armour Our knowledge about the arms and armour of the Viking "
//+ "		  Viking swords "
//+ "		  age is based on archaeological finds, pictorial represen- tation, and to some extent on the accounts in the Norse sagas and Norse laws recorded in the 13th century. Ac- cording to custom, all free Norse men were required to own weapons and were permitted to carry them all the time. These arms were indicative of a Viking’s social status: a wealthy Viking had a complete ensemble of a helmet , shield , mail shirt, and sword. A typical bóndi (freeman)wasmorelikelytofightwitha spear andshield, and most also carried a seax as a utility knife and side- arm. Bowswereusedintheopeningstagesoflandbattles and at sea, but they tended to be considered less “hon- ourable” than a melee weapon. Vikings were relatively unusual for the time in their use of axes as a main battle weapon. The Húscarls , the elite guard of King Cnut (and laterof KingHaroldII )werearmedwithtwo-handedaxes that could split shields or metal helmets with ease. The warfare and violence of the Vikings were often motivated and fuelled by their beliefs in Norse reli- "
//+ "		  death. [133] [134] In combat it is believed, that the Vikings sometimes engaged in a disordered style of frenetic, fu- rious fighting known as berserkergang , leading them to be termed berserkers . Such tactics may have been de- ployedintentionallyby shocktroops andtheberserk-state may have been induced through ingestion of materials "
//+ "		  mushrooms, Amanita muscaria , [135] or large amounts of"
//+ "		  been involved in some dice games. [126] [128]"
//+ "		and alcoholic drinks, like beer and mead , contributed to"
//+ "		harps , fiddles , lyres and lutes . [126]"
//+ "		Kingdom, Sagnlandet Lejre and Ribe Viking Center in"
//+ "		foundland for example. [130]"
//+ "		2 , renamed Sea Stallion , [131] began a journey from"
//+ "		gion , focusing on Thor and Odin , the gods of war and"
//+ "		Greenland to Newfoundland in 1998. [132]"
//+ "		with psychoactive properties, such as the hallucinogenic"
//+ "		alcohol. [136]"
//+ ""
//+ ""
//+ "	**************************** Page 13****************************"
//+ "		  5.1 Goods 13"
//+ "		  5 Trade "
//+ "		  See also: Trade route from the Varangians to the Greeks and Volga trade route TheVikingsestablishedandengagedinextensivetrading"
//+ "		  • Glass was much prized by the Norse. The imported these have been found in their thousands. Åhus in Scania and the old market town of Ribe had major"
//+ "		  The scales and weights of a Viking trader. Used for measuring silver and sometimes gold. From the Sigtuna box ."
//+ "		  • Silk wasaveryimportantcommodityobtainedfrom valued by many European cultures of the time, and the Vikings used it to illustrate status such as wealth and nobility. Many of the archaeological finds in "
//+ "		  • Wine was imported from France and Germany as a beer . "
//+ "		  To counter these valuable imports, the Vikings exported "
//+ "		  • Amber - the fossilized resin of the pine tree - was line. It was worked into beads and ornamental ob- jects, before being traded. (See also the Amber "
//+ "		  Road ). • Fur was also exported as it provided warmth. This"
//+ "		  networksthroughouttheknownworldandhadaprofound influence on the economic development of Europe and "
//+ "		  Except for the major trading centres of Ribe , Hedeby and the like, the Viking world was unfamiliar with the use of coinage and was based on so called bullion economy. Silver was the most common metal in the economy by large, although gold was also used to some extent. Silver circulated in the form of bars, or ingots , as well as in the form of jewellery and ornaments. Traders carried small scales, enabling them to measure weight very accurately, so it was possible to have a very precise system of trade "
//+ "		  5.1 Goods "
//+ "		  The organized trade covered everything from ordinary items in bulk to exotic luxury products. The Viking ship designs, like that of the knarr, were an important factor "
//+ "		  other cultures included: [140] "
//+ "		  • Spices were obtained from Chinese and Persian sia. Vikings used homegrown spices and herbs like "
//+ "		  imported cinnamon ."
//+ "		  "
//+ "		  otters and beavers . • Cloth and wool . The Vikings were skilled spinners "
//+ "		  quality. • Down was collected and exported. The Norwe- feathers were bought from the Samis . Down was used for bedding and quilted clothing. Fowling on the steep slopes and cliffs was dangerous work and "
//+ "		  • Slaves , known as thralls in Old Norse. On their them monks and clergymen. They were sometimes sold as slaves to Arab merchants in exchange for sil- ver. "
//+ "		  Other exports included weapons, walrus ivory , wax , salt and cod . Asoneofthemoreexoticexports, huntingbirds were sometimes provided from Norway to the European "
//+ "		  Many of these goods were also traded within the Viking world itself, as well as goods such as soapstone and whetstone . Soapstone was traded with the Norse on Iceland and in Jutland , who used it for pottery. Whet- stones were traded and used for sharpening weapons, "
//+ "		  surrounding areas, that the extensive medieval trade with"
//+ "		  glass was often made into beads for decoration and"
//+ "		production of glass beads. [142] [143] [144]"
//+ "		Byzantium (modern day Istanbul ) and China. It was"
//+ "		Scandinavia include silk. [145] [146] [147]"
//+ "		drink of the wealthy, to vary the regular mead and"
//+ "		a large variety of goods. These goods included: [140]"
//+ "		frequently found on the North Sea and Baltic coast-"
//+ "		included the furs of pine martens , foxes , bears ,"
//+ "		Scandinavia not the least. [137] [138]"
//+ "		and weavers and exported woollen cloth of a high"
//+ "		gian west coast supplied eiderdowns and sometimes"
//+ "		was often lethal. [148]"
//+ "		and exchange, even without a regular coinage. [137]"
//+ "		raids, the Vikings captured many people, among"
//+ "		in their success as merchants. [139] Imported goods from"
//+ "		aristocracy, from the 10th century. [148]"
//+ "		traders, who met with the Viking traders in Rus-"
//+ "		caraway , thyme , horseradish and mustard , [141] but"
//+ "		tools andknives. [140] There areindicationsfrom Ribeand"
//+ ""
//+ ""
//+ "	**************************** Page 14****************************"
//+ "		  14 6 LEGACY"
//+ "		  oxen and cattle from Jutland (see Ox Road ), reach as far back as c. 720 AD. This trade satisfied the Vikings’ need forleatherandmeattosomeextent, andperhapshidesfor parchment production on the European mainland. Wool was also very important as a domestic product for the Vikings, to produce warm clothing for the cold Scandi- navian and Nordic climate, and for sails. Sails for Viking shipsrequiredlargeamountsofwool, asevidencedbyex- perimental archaeology. There are archaeological signs of organized textile productions in Scandinavia, reaching as far back as the early Iron Ages . Artisans and crafts- men in the larger towns were supplied with antlers from organized hunting with large-scale reindeer traps in the far north. They were used as raw material for making "
//+ "		  6 Legacy "
//+ "		  6.1 Medieval perceptions of the Vikings "
//+ "		  In England the Viking Age began dramatically on 8 June 793 when Norsemen destroyed the abbey on the island of Lindisfarne . The devastation of Northumbria 's Holy Island shocked and alerted the royal courts of Europe to the Viking presence. “Never before has such an atroc- "
//+ "		  of York . [149] Medieval Christians in Europe were totally unprepared for the Viking incursions and could find no explanation for their arrival and the accompanying suf- fering they experienced at their hands save the “Wrath of "
//+ "		  on Lindisfarne demonised perception of the Vikings for the next twelve centuries. Not until the 1890s did schol- ars outside Scandinavia begin to seriously reassess the achievements of the Vikings, recognizing their artistry, "
//+ "		  Norse Mythology , sagas, and literature tell of Scandi- navian culture and religion through tales of heroic and mythological heroes. Early transmission of this informa- tion was primarily oral, and later texts were reliant upon the writings and transcriptions of Christian scholars, in- cluding the Icelanders Snorri Sturluson and Sæmundur fróði . Many of these sagas were written in Iceland, and most of them, even if they had no Icelandic provenance, were preserved there after the Middle Ages due to the continued interest of Icelanders in Norse literature and law codes. The 200-year Viking influence on European history is filled with tales of plunder and colonization, and the ma- jority of these chronicles came from western witnesses and their descendants. Less common, though equally rel- evant,aretheVikingchroniclesthatoriginatedintheeast, including the Nestor chronicles, Novgorod chronicles, Ibn Fadlan chronicles, Ibn Rusta chronicles, and brief mentionsby Photius ,patriarchofConstantinople,regard- ing their first attack on the Byzantine Empire . Other"
//+ "		  chroniclers of Viking history include Adam of Bremen , who wrote, in thefourth volumeof his Gesta Hammabur- gensis Ecclesiae Pontificum , \"[t]here is much gold here (in Zealand ), accumulated by piracy. These pirates, which are called wichingi by their own people, and Ascomanni by our own people, pay tribute to the Danish king.” In 991, the Battle of Maldon between Viking raiders and the inhabitants of Maldon in Essex was commemorated with a poem of the same name. "
//+ "		  6.2 Post-medieval perceptions "
//+ "		  A modern reenactment of a Viking battle "
//+ "		  Earlymodernpublications, dealingwithwhatwenowcall Viking culture, appeared in the 16th century, e.g. Histo- ria de gentibus septentrionalibus (Olaus Magnus, 1555), and the first edition of the 13th-century Gesta Danorum of Saxo Grammaticus in 1514. The pace of publication increased during the 17th century with Latin translations of the Edda (notably Peder Resen’s Edda Islandorum of 1665). InScandinavia,the17th-centuryDanishscholars Thomas Bartholin and Ole Worm and the Swede Olaus Rudbeck used runic inscriptions and Icelandic sagas as historical sources. An important early British contributor to the study of the Vikings was George Hicke, who published his Linguarum vett. septentrionalium thesaurus in 1703– 05. During the 18th century, British interest and enthusi- asm for Iceland and early Scandinavian culture grew dra- matically, expressed in English translations of Old Norse texts and in original poems that extolled the supposed Viking virtues. The word “viking” was first popularised at the begin- ning of the 19th century by Erik Gustaf Geijer in his poem, The Viking . Geijer’s poem did much to prop- agate the new romanticised ideal of the Viking, which had little basis in historical fact. The renewed interest of Romanticism in the Old North had contemporary polit- ical implications. The Geatish Society , of which Geijer was a member, popularised this myth to a great extent. Another Swedish author who had great influence on the"
//+ "		  everyday utensils like combs. [148]"
//+ "		itybeenseen,”declaredtheNorthumbrianscholar Alcuin"
//+ "		God”. [150] More than any other single event, the attack"
//+ "		technological skills, and seamanship. [151]"
//+ ""
//+ ""
//+ "	**************************** Page 15****************************"
//+ "		  6.2 Post-medieval perceptions 15"
//+ "		  perception of the Vikings was Esaias Tegnér , member of the Geatish Society, who wrote a modern version of Friðþjófs saga hins frœkna , which became widely pop- ular in the Nordic countries, the United Kingdom, and Germany. "
//+ "		  Everyday life in the Viking Age "
//+ "		  Fascination with the Vikings reached a peak during the so-called Viking revival in the late 18th and 19th cen- turies. In Britain this was called Septentrionalism, in Germany\" Wagnerian \"pathosor EsotericisminGermany and Austria , and in the Scandinavian countries Romantic nationalism or Scandinavism . Pioneering 19th-century scholarly editions of the Viking Age began to reach a small readership in Britain, archaeologists began to dig up Britain’s Viking past, and linguistic enthusiasts started to identify the Viking-Age origins of rural idioms and proverbs. The new dictionaries of the Old Norse lan- guage enabled the Victorians to grapple with the primary "
//+ "		  Until recently, the history of the Viking Age was largely based on Icelandic sagas, the history of the Danes writ- ten by Saxo Grammaticus, the Russian Primary Chroni- cle , and Cogad Gáedel re Gallaib . Few scholars still ac- cept these texts as reliable sources, as historians now rely more on archaeology and numismatics , disciplines that have made valuable contributions toward understanding "
//+ "		  6.2.1 In 20th-century politics "
//+ "		  The romanticised idea of the Vikings constructed in scholarly and popular circles in northwestern Europe in the 19th and early 20th centuries was a potent one, and the figure of the Viking became a familiar and mal- leable symbol in different contexts in the politics and "
//+ "		  mandy, which had been settled by Vikings, the Viking ship became an uncontroversial regional symbol. In Ger- many, awareness of Viking history in the 19th century had been stimulated by the border dispute with Den- mark over Schleswig-Holstein and the use of Scandina- vian mythology by Richard Wagner . The idealised view of the Vikings appealed to Germanic supremacists who transformed the figure of the Viking in accordance with "
//+ "		  on the linguistic and cultural connections between Norse- speaking Scandinavians and other Germanic groups in"
//+ "		  the distant past, Scandinavian Vikings were portrayed in Nazi Germany as a pure Germanic type. The cultural phenomenon of Viking expansion was re-interpreted for use as propaganda to support the extreme militant nation- alism of the Third Reich, and ideologically informed in- terpretations of Viking paganism and the Scandinavian use of runes were employed in the construction of Nazi mysticism . Other political organizations of the same ilk, such as the former Norwegian fascist party Nasjonal Samling , similarly appropriated elements of the modern Viking cultural myth in their symbolism and propaganda. In communist Russia, the ideology of Slavic racial purity led to the complete denial that Scandinavians had played a part in the emergence of the principalities of the Rus’, which were supposed to have been founded by Slavs. Ev- idence to the contrary was suppressed until the 1990s. Novgorod now enthusiastically acknowledges its Viking "
//+ "		  6.2.2 In modern popular culture "
//+ "		  Viking reenactment training (Jomsvikings group) "
//+ "		  LedbytheoperasofGermancomposer RichardWagner , suchas DerRingdesNibelungen ,VikingsandtheRoman- ticist Viking Revival have inspired many creative works. These have included novels directly based on historical events, such as Frans Gunnar Bengtsson 's The Long Ships (which was also released as a 1963 film ), and historical fantasies such as the film The Vikings , Michael Crich- ton 's Eaters of the Dead (movie version called The 13th Warrior ), and the comedy film Erik the Viking . The vam- pire Eric Northman , in the HBO TV series True Blood , was a Viking prince before being turned into a vampire. Vikings appear in several books by the Danish American writer Poul Anderson , while British explorer, historian, and writer Tim Severin authored a trilogy of novels in 2005 about a young Viking adventurer Thorgils Leifsson, who travels around the world. In 1962, American comic book writer Stan Lee and his brother Larry Lieber , together with Jack Kirby , created the Marvel Comics superhero Thor , which they based on the Norse god of the same name. The character is fea- tured in the 2011 Marvel Studios film Thor and its sequel Thor: The Dark World and also appears in the 2012 film The Avengers and its associated animated series ."
//+ "		  history and has included a Viking ship in its logo. [156]"
//+ "		Icelandic sagas. [152]"
//+ "		the period. [153]"
//+ "		political ideologies of 20th-century Europe. [154] In Nor-"
//+ "		the ideology of the Germanic master race. [155] Building"
//+ ""
//+ ""
//+ "	**************************** Page 16****************************"
//+ "		  16 7 GENETIC LEGACY"
//+ "		  Since the 1960s, there has been rising enthusiasm for historical reenactment . While the earliest groups had little claim for historical accuracy, the seriousness and accuracy of reenactors has increased. The largest such groups include The Vikings and Regia Anglorum , though many smaller groups exist in Europe, North America, New Zealand, and Australia. Many reenactor groups par- ticipate in live-steel combat, and a few have Viking-style ships or boats. The Minnesota Vikings of the National Football League are so-named owing to the large Scandinavian population in the US state of Minnesota . Modernreconstructionsof Vikingmythology haveshown a persistent influence in late 20th- and early 21st-century popular culture in some countries, inspiring comics, role- playing games, computer games, and music, including Viking metal , a subgenre of heavy metal music ."
//+ "		  The Vikings were often depicted with winged helmets and in other clothing taken from Classical antiquity , es- pecially in depictions of Norse gods. This was done to legitimize the Vikings and their mythology by associating it with the Classical world, which had long been idealized in European culture. The latter-day mythos created by national romantic ideas blended the Viking Age with aspects of the Nordic Bronze Age some 2,000 years earlier. Horned hel- mets from the Bronze Age were shown in petroglyphs and appeared in archaeological finds (see Bohuslän and Vikso helmets). They were probably used for ceremonial "
//+ "		  Cartoons like Hägar the Horrible and Vicky the Viking , and sports kits such as those of the Minnesota Vikings and Canberra Raiders have perpetuated the myth of the"
//+ "		  6.3 Common misconceptions "
//+ "		  6.3.1 Horned helmets "
//+ "		  Main article: Horned helmet Apart from two or three representations of (ritual) hel-"
//+ "		  Viking helmets were conical, made from hard leather with wood and metallic reinforcement for regular troops. The iron helmet with mask and mail was for the chief- tains, based on the previous Vendel -age helmets from central Sweden. The only true Viking helmet found is from Gjermundbu in Norway. This helmet is made of"
//+ "		  Magnus Barelegs Viking Festival "
//+ "		  mets–withprotrusionsthatmaybeeitherstylisedravens, snakes, or horns – no depiction of the helmets of Viking warriors, and no preserved helmet, has horns. The for- mal, close-quarters style of Viking combat (either in shield walls or aboard “ship islands”) would have made horned helmets cumbersome and hazardous to the war- rior’s own side. Historians therefore believe that Viking warriors did not wear horned helmets; whether such helmets were used in Scandinavian culture for other, ritual purposes, remains unproven. The general misconception that Viking war- riors wore horned helmets was partly promulgated by the "
//+ "		  in 1811 in Stockholm. [157] They promoted the use of Norse mythology as the subject of high art and other eth- nological and moral aims."
//+ "		  6.3.2 Barbarity "
//+ "		  The image of wild-haired, dirty savages sometimes asso- ciated with the Vikings in popular culture is a distorted "
//+ "		  sponsible for most surviving accounts of the Vikings, and consequently, a strong possibility for bias exists. This at- titude is likely attributed to Christian misunderstandings regarding paganism. Viking tendencies were often mis- reported, and the work of Adam of Bremen, among oth- ers, told largely disputable tales of Viking savagery and "
//+ "		  Contrary to the wild, filthy image of Vikings, the early thirteenth-century chronicler John of Wallingford de- scribed a grave problem of Englishwomen seeking out Danish men as lovers because of their appealing “habit of combing their hair every day, of bathing every Satur- day, and regularly changing their clothes,” in contrast to "
//+ "		  7 Genetic legacy "
//+ "		  Studies of genetic diversity provide some indication of the origin and expansion of the Viking population. Haplogroup I-M253 (defined by specific genetic markers on the Y-chromosome) mutation occurs with the great- est frequency among Scandinavian males: 35 percent in Norway, Denmark, and Sweden, and peaking at 40 per-"
//+ "		  purposes. [158]"
//+ "		horned helmet. [159]"
//+ "		iron and has been dated to the 10th century. [160]"
//+ "		picture of reality. [2] Non-Scandinavian Christians are re-"
//+ "		uncleanliness. [161]"
//+ "		local men. [162]"
//+ "		19th-century enthusiasts of Götiska Förbundet , founded"
//+ "		cent within western Finland. [163] It is also common near"
//+ ""
//+ ""
//+ "	**************************** Page 17****************************"
//+ "		  17"
//+ "		  the southern Baltic and North Sea coasts, and then suc- cessively decreasing further to the south geographically. Genetic studies in the British Isles of the Y-DNA haplogroup R-M420 , seen also across Scandinavia, have demonstrated that the Vikings settled in Britain and Ire- land as well as raiding there. Both male and female de- scent studies show evidence of Norse descent in areas "
//+ "		  islands. [164] Inhabitants of lands farther away show most "
//+ "		  A specialised genetic and surname study in Liverpool demonstrated marked Norse heritage: up to 50 percent of males who belonged to original families, those who lived there before the years of industrialization and pop- "
//+ "		  itance – tracked through R-M420 haplotype signatures – "
//+ "		  cashire . [167] This was similar to the percentage of Norse "
//+ "		  Recent research suggests that the Scottish warrior Somerled ,whodrovetheVikingsoutofScotlandandwas the progenitor of Clan Donald , may have been of Viking "
//+ "		  8 See also "
//+ "		  • Faroese people • Geats • Gotlander • Gutasaga • Proto-Norse language • Scandinavian prehistory • Sea peoples • Swedes (Germanic tribe) • Viking raid warfare and tactics "
//+ "		  9 Notes "
//+ "		  [1] Viking (people) , Encyclopædia Britannica. "
//+ "		  [2] Roesdahl, pp. 9–22. "
//+ "		  [3] Brink 2008 "
//+ "		  [4] Wawn 2000 "
//+ "		  [5] Johnni Langer, “The origins of the imaginary viking”, Viking Heritage Magazine, Gotland University/Centre for Baltic Studies. Visby (Sweden), n. 4, 2002."
//+ "		  [6] The Syntax of Old Norse by Jan Terje Faarlund ; p 25 ISBN 0-19-927110-0 ; The Principles of English Etymol- ogy By Walter W. Skeat , published in 1892, defined Viking : better Wiking, Icel. Viking-r, O. Icel. *Viking- r, a creek-dweller; from Icel. vik, O. Icel. *wik, a creek, bay, with suffix -uig-r, belonging to Principles of English Etymology By Walter W. Skeat; Clarendon press; Page "
//+ "		  479 [7] Eldar Heide (2005). \" Víking - 'rower shifting'? An ety- mological contribution” (PDF). Arkiv för nordisk filologi "
//+ "		  120 : 41–54. Retrieved 20 April 2015. [8] Walter W. Skeat: Principles of English Etymology Claren- "
//+ "		  don press, p. 479 [9] Kvilhaug, Maria. “The Tribe that Gave Vikings Their Name?\" . http://freya.theladyofthelabyrinth.com/ . Freya. Retrieved 17 March 2015. External link in |website= "
//+ "		  ( help ) [10] AnatolyLiberman(2009). WhatDidTheVikingsDoBe- "
//+ "		  fore They Began to Play Football? [11] Hans C. Boas (13 May 2014). “Indo-European Lexicon - PIE Etymon and IE Reflexes” . Linguistics Research Cen- ter. TheUniversityofTexasatAustin. Retrieved20April "
//+ "		  2015. [12] Eldar Heide (2005). \" Víking - 'rower shifting'? An ety- mological contribution” (PDF). Arkiv för nordisk filologi "
//+ "		  120 : 41–54. Retrieved 20 April 2015. [13] Bernard Mees (2012). “Taking Turns: linguistic econ- omy and the name of the Vikings” . Royal Melbourne In- stitute of Technology (RMIT). Arkiv för nordisk filologi "
//+ "		  (academia.edu) 127 : 5–12. Retrieved 20 April 2015. [14] Eldar Heide (2008). “Viking, week, and Widsith. A reply toHaraldBjorvand” . CentreofMedievalStudies(Univer- sity of Bergen). Arkiv för nordisk filologi (academia.edu) "
//+ "		  123 : 23–28. Retrieved 20 April 2015. [15] Beard, David. “The Term “Viking\"\" . http://www. archeurope.com . Archaeology in Europe. Archived from the original on 7 April 2012. Retrieved 23 April 2014. "
//+ "		  External link in |work= ( help ) [16] Wolf 2004 , p. 2. [17] Educational Company of Ireland 2000 , p. 472. [18] Brookes 2004 , p. 297. [19] D'Amato 2010 , p. 3. [20] Douglas Harper: Russia Online Etymology Dictionary. A "
//+ "		  private homepage project. [21] “Land of the Rus - Viking explorations to the east” . Na- "
//+ "		  tional Museum of Denmark. Retrieved 20 April 2015. [22] “Dangerous journeys to Eastern Europe and Russia” . Na- "
//+ "		  tional Museum of Denmark. Retrieved 20 April 2015. [23] PeterSawyer, TheVikingExpansion , TheCambridgeHis- tory of Scandinavia, Issue 1 (Knut Helle, ed., 2003), p.105."
//+ "		  closest to Scandinavia, such as the Shetland and Orkney"
//+ "		Norse descent in the male Y-chromosome lines. [165]"
//+ "		ulation expansion. [166] High percentages of Norse inher-"
//+ "		werealsofoundamongmalesinthe Wirral and WestLan-"
//+ "		inheritancefoundamongmalesintheOrkneyIslands. [168]"
//+ "		descent , a member of haplogroup R-M420. [169]"
//+ ""
//+ ""
//+ "	**************************** Page 18****************************"
//+ "		  18 9 NOTES"
//+ "		  [24] Lund, Niels (2001). “The Danish Empire and the End of the Viking Age”, The Oxford Illustrated History of the Vikings . Ed. P. H. Sawyer. Oxford University Press, "
//+ "		  2001, p. 167–181. ISBN 0-19-285434-8 . [25] The Royal Household, “Sweyn” , The official Website of TheBritishMonarchy ,15March2015,accessed15March "
//+ "		  2015 [26] Lawson, M K (2004). “Cnut: England’s Viking King 1016-35”. The History Press Ltd, 2005, ISBN 978- "
//+ "		  0582059702 . [27] The Royal Household, “Canute The Great” , The official Website of The British Monarchy , 15 March 2015, ac- "
//+ "		  cessed 15 March 2015 [28] Badsey, S. Nicolle, D, Turnbull, S (1999). “The Timechart of Military History”. Worth Press Ltd, 2000, "
//+ "		  ISBN 1-903025-00-1 . [29] “History of Northumbria: Viking era 866 AD–1066 AD” "
//+ "		  www.englandnortheast.co.uk. [30] Toyne, Stanley Mease. The Scandinavians in history "
//+ "		  Pg.27. 1970. [31] The Fate of Greenland’s Vikings , by Dale Mackenzie Brown, Archaeological Institute of America , 28 February "
//+ "		  2000 [32] “The Norse discovery of America” . Ncbi.nlm.nih.gov. 4 "
//+ "		  April 2012. Retrieved 21 May 2012. [33] Ross, Valerie (31 May 2011). “Climate change froze Vikings out of Greenland” . Discover (Kalmback Publish- "
//+ "		  ing). Retrieved 6 April 2013. [34] Rurik Dynasty (medieval Russian rulers) Britannica On- "
//+ "		  line Encyclopedia [35] Hall, p. 98 [36] “Vikings’ Barbaric Bad Rap Beginning to Fade” . News.nationalgeographic.com. 28 October 2010. "
//+ "		  Retrieved 21 May 2012. [37] “Los vikingos en Al-Andalus (abstract available in En- glish)\" (PDF). Jesús Riosalido. 1997. Retrieved 11 May "
//+ "		  2010. [38] John Haywood: Penguin Historical Atlas of the Vikings , Penguin (1996). Detailed maps of Viking settlements in "
//+ "		  Scotland, Ireland, England, Iceland and Normandy. [39] Matthias Schulz (27 August 2010). \"'Sensational' Dis- covery: Archeologists Find Gateway to the Viking Em- pire” . Spiegel Online International. Retrieved 27 Febru- "
//+ "		  ary 2014. [40] Lotte Flugt Kold (3 November 2014). “Dannevirke” . danmarkshistorien.dk (in Danish). Aarhus University. "
//+ "		  Retrieved 20 December 2014. [41] Rudolf Simek, “the emergence of the viking age: cir- cumstances and conditions”, “The vikings first Europeans VIII–XI century—the new discoveries of archaeology”, other, 2005, pp. 24–25"
//+ "		  [42] Bruno Dumézil, master of Conference at Paris X- Nanterre, Normalien, aggregated history, author of con- version and freedom in the barbarian kingdoms. 5th–8th "
//+ "		  centuries (Fayard, 2005) [43] “Franques Royal Annals” cited in Peter Sawyer, “The Ox- "
//+ "		  ford Illustrated History of the Vikings”, 2001, p. 20 [44] Dictionnaire d'histoire de France, Perrin, Alain Decaux and André Castelot, 1981, pages 184/185. ISBN 2-7242- "
//+ "		  3080-9 . [45] “the Vikings” R.Boyer history, myths, dictionary, Robert "
//+ "		  Laffont several 2008, p96 ISBN 978-2-221-10631-0 [46] François-Xavier Dillmann , “Viking civilisation and cul- ture. A bibliography of French-language \", Caen, Centre for research on the countries of the North and Northwest, University of Caen, 1975, p.19, and “Les Vikings: the Scandinavian and European 800–1200”, 22nd exhibition "
//+ "		  of art from the Council of Europe, 1992, p. 26 [47] “History of the Kings of Norway” by Snorri Sturlusson translated by Professor of History François-Xavier Dill- mann, Gallimard ISBN 2-07-073211-8 pp. 15–16, 18, "
//+ "		  24, 33–34, 38 [48] Macauley Richardson, Lloyd. “Books: Eurasian Explo- ration” . Policy Review . Hoover Institution. Archived "
//+ "		  from the original on 2009-12-16. [49] Crone, Patricia. Meccan trade and the rise of Islam First "
//+ "		  Georgias Press. 2004. [50] “Viking expeditions and raids” . National Museum of "
//+ "		  Denmark. Retrieved 20 April 2015. [51] Roesdahl, pp. 295–7 [52] Gareth Williams, 'Kingship, Christianity and coinage: monetary and political perspectives on silver economy in the Viking Age', in Silver Economy in the Viking Age , ed. James Graham-Campbell and Gareth Williams, pp. 177– "
//+ "		  214; ISBN 978-1-59874-222-0 [53] Roesdahl, pp. 296 [54] The Northern Crusades: Second Edition by Eric Chris- "
//+ "		  tiansen; ISBN 0-14-026653-4 [55] “Written sources shed light on Viking travels” . National "
//+ "		  Museum of Denmark. Retrieved 20 April 2015. [56] Hall, 2010, p. 8 and passim . [57] Roesdahl, pp. 16–22. [58] Hall, pp. 8–11 [59] Lindqvist, pp. 160–61 [60] See List of English words of Old Norse origin for further "
//+ "		  explanations on specific words. [61] See Norman toponymy . [62] Henriksen, Louise Kæmpe: Nordic place names in Eu- "
//+ "		  rope Viking Ship Museum Roskilde [63] Viking Words The British Library"
//+ "		  "
//+ ""
//+ "	**************************** Page 19****************************"
//+ "		  19"
//+ "		  [64] Joel Supéry. “Germanic Toponomy” . Vikings in "
//+ "		  Aquitaine . Retrieved 1 March 2014. [65] Joel Supéry. “A colony in Gascony?\" . Vikings in "
//+ "		  Aquitaine . Retrieved 1 March 2014. [66] TheFrenchRegionsof Poitou-Charentes and Aquitaine to "
//+ "		  be precise. [67] Annie Dumont (2007). “Méthodes d'étude d'un site flu- vial du haut Moyen Age: Taillebourg – Port d'Envaux, (Charente-Maritime)\" (PDF). Proceedings of the 4th In- ternational Congress of Medieval and Modern Archaeol- ogy (inFrench)(MedievalEurope,Paris2007). Retrieved "
//+ "		  1 March 2014. [68] Department of Scandinavian Research University of "
//+ "		  Copenhagen [69] See information on the “Slavonic and Norse names of the Dnieperrapids”on TraderoutefromtheVarangianstothe "
//+ "		  Greeks . [70] Else Roesdahl (prof. in Arch. & Hist.): The Vikings , Pen- "
//+ "		  guin Books (1999), ISBN 0-14-025282-7 [71] Sawyer, P H: 1997 [72] Jelling stones . Encyclopædia Britannica . 2008. [73] Rundata , DR 42 [74] baþum (Sm101), see Nordiskt runnamnslexikon PDF [75] In the nominative: krikiaR (G216). In the genitive: girkha (U922$), k—ika (U104). In the dative: girk- ium (U1087†), kirikium (SöFv1954;20, U73, U140), ki(r)k(i)(u)(m) (Ög94$), kirkum (U136), krikium (Sö163, U431), krikum (Ög81A, Ög81B, Sö85, Sö165, Vg178, U201, U518), kri(k)um (U792), krikum (Sm46†, U446†), krkum (U358), kr ... (Sö345$A), kRkum (Sö82). Intheac- cusative: kriki (Sö170). Uncertain case krik (U1016$Q). Greecealsoappearsas griklanti (U112B), kriklati (U540), "
//+ "		  kriklontr (U374$), see Nordiskt runnamnslexikon PDF [76] Karusm (Vs1), see Nordiskt runnamnslexikon PDF [77] iaursaliR (G216), iursala (U605†), iursalir (U136G216, "
//+ "		  U605, U136), see Nordiskt runnamnslexikon PDF [78] lakbarþilanti (SöFv1954;22), see Nordiskt run- "
//+ "		  namnslexikon PDF [79] serklat (G216), se(r)kl ... (Sö279), sirklanti (Sö131), sirk: lan:ti (Sö179), sirk*la(t)... (Sö281), srklant - (U785), "
//+ "		  skalat- (U439), see Nordiskt runnamnslexikon PDF [80] eklans (Vs18$), eklans (Sö83†), ekla-s (Vs5), enklans (Sö55), iklans (Sö207), iklanþs (U539C), ailati (Ög104), aklati (Sö166), akla -- (U616$), anklanti (U194), eg×loti (U812), eklanti (Sö46, Sm27), eklati (ÖgFv1950;341, Sm5C, Vs9), enklanti (DR6C), haklati (Sm101), ik- lanti (Vg20), iklati (Sm77), ikla-ti (Gs8), i...-ti (Sm104), ok*lanti (Vg187), oklati (Sö160), onklanti (U241), onklati (U344), - klanti (Sm29$), iklot (N184), see Nordiskt run- "
//+ "		  namnslexikon PDF [81] luntunum (DR337$B), see Nordiskt runnamnslexikon PDF"
//+ "		  [82] Jasmine Idun Tova Lyman (2007), Viking Age graves in "
//+ "		  Iceland (PDF), University of Iceland, p. 4 [83] Medieval Archaeology: An Encyclopaedia (Pamela Crab- "
//+ "		  tree, ed., 2001), “Vikings,” p. 510. [84] Roesdahl, p. 20. [85] Roesdahl p. 70 (in Women, gender roles and children) [86] The Hemlanden cemetery located here is the largest Viking Period cemetery in Scandinavia Phillip Pulsiano, Kirsten Wolf, ed. (1993). Medieval Scandinavia: An En- cyclopedia (Illustrated ed.). United Kingdom: Taylor & "
//+ "		  Francis. pp. 238–239. ISBN 978-0-8240-4787-0 . [87] Erlandson (2005). “A Viking-Age Valley in Iceland: TheMosfellArchaeologicalProject” (PDF). MedievalAr- chaeology Journal of the Society For Medieval Archaeol- "
//+ "		  ogy . Archived from the original (PDF) on 19 April 2011. [88] See also Jon M. Erlandson . [89] [I)ór Magnússon: Bátkumlió í Vatnsdal , Arbók hies íslen- "
//+ "		  zka fornleifafélags (1966), 1-32 [90] A comprehensive list of registered pagan graves in Ice- land, can be found in Eldjárn & Fridriksson (2000): Kuml "
//+ "		  og haugfé . [91] Dale Mackenzie Brown (28 February 2000). “The Fate of Greenland’s Vikings” . Archaeology . the Archaeological "
//+ "		  Institute of America. Retrieved 22 February 2014. [92] Longships are sometimes erroneously called drakkar , a "
//+ "		  corruption of “dragon” in Norse. [93] Hadingham, Evan: Secrets of Viking Ships (05.09.00) "
//+ "		  NOVA science media. [94] Durham, Keith: Viking Longship Osprey Publishing, Ox- "
//+ "		  ford, 2002. [95] Block, Leo, To Harness the Wind: A Short History of the Development of Sails , Naval Institute Press, 2002, ISBN "
//+ "		  1-55750-209-9 [96] Ian Heath, The Vikings, p. 4, Osprey Publishing, 1985. [97] Curry, Andrew (10 June 2013). “The First Vikings” . Ar- chaeology . the Archaeological Institute of America. Re- "
//+ "		  trieved 22 February 2014. [98] Roesdahl, pp. 38–48, pp.61-71. [99] Mari Kildah (5 December 2013). “Double graves with headless slaves” . University of Oslo. Retrieved 23 June "
//+ "		  2014. [100] Borgström Eva (Swedish): Makalösa kvinnor: könsöver- skridare i myt och verklighet (Marvelous women : gender benders in myth and reality) Alfabeta/Anamma, Stock- "
//+ "		  holm2002. ISBN91-501-0191-9 (inb.). Libris8707902. [101] Borgström Eva(Swedish): Makalösa kvinnor: könsöver- skridare i myt och verklighet (Marvelous women : gender benders in myth and reality) Alfabeta/Anamma, Stock- holm2002. ISBN91-501-0191-9 (inb.). Libris8707902."
//+ "		  "
//+ ""
//+ "	**************************** Page 20****************************"
//+ "		  20 9 NOTES"
//+ "		  [102] Ohlander, Ann-Sofie & Strömberg, Ulla-Britt, Tusen svenska kvinnoår: svensk kvinnohistoria från vikingatid till nutid, 3. (A Thousand Swedish Women’s Years: Swedish Women’s History from the Viking Age until now), [omarb. och utök.] uppl., Norstedts akademiska "
//+ "		  förlag, Stockholm, 2008 [103] Ingelman-Sundberg, Catharina, Forntida kvinnor: jägare, vikingahustru, prästinna [Ancient women: hunters, viking "
//+ "		  wife, priestess], Prisma, Stockholm, 2004 [104] “Appearance-WhatdidtheVikingslooklike?\" . National "
//+ "		  Museum of Denmark. Retrieved 20 April 2015. [105] Sk. V. Gudjonsson (1941): Folkekost og sundhedsforhold i gamle dage. Belyst igennem den oldnordiske Litteratur. (Dvs. først og fremmest de islandske sagaer). København. (Danish) Short description in English: Diet and health in previous times, as revealed in the Old Norse Literature, "
//+ "		  especially the Icelandic Sagas. [106] Pernille Rohde Sloth, Ulla Lund Hansen & Sabine Karg (2013). “Viking Age garden plants from southern Scandi- navia – diversity, taphonomy and cultural aspect” (PDF). "
//+ "		  Danish Journal of Archaeology . Retrieved 19 June 2014. [107] Thiswillcausea lacticacidfermentation processtooccur. [108] “Forråd til vinteren - Salte, syrne, røge og tørre [Supplies for the winter - curing, fermenting, smoking and drying]\" . "
//+ "		  Ribe Vikingecenter (in Danish). Retrieved 20 April 2015. [109] Roesdahl, p. 54 [110] “Viking Food” . National Museum of Denmark. Re- "
//+ "		  trieved 20 April 2015. [111] See the article on the Northern European short-tailed sheep for specific information. In southern Scandinavia (ie. Denmark), the heath sheeps of Lüneburger Heid- "
//+ "		  schnucke was raised and kept. [112] “The animals on the farm - Genetic connection” . Ribe "
//+ "		  Vikingecenter . Retrieved 19 April 2015. [113] “Poultry” . Danish Agricultural Museum . Retrieved 19 "
//+ "		  April 2015. [114] O'Conner, Terry. 1999? “The Home- Food and Meat.” "
//+ "		  Viking Age York. Jorvik Viking Centre. [115] Roesdahl p. 102-117 [116] Nedkvitne, Arnved. “Fishing, Whaling and Seal Hunt- ing.” in Pulsiano, Phillip (1993). Medieval Scandinavia: An Encyclopedia . Garland Reference Library of the Hu- "
//+ "		  manities. [117] Inge Bødker Enghoff (2013). Huntng, fishing and ani- mal husbandry at The Farm Beneath The Sand, Western Greenland . Man & Society 28 (the Greenland National "
//+ "		  Museum, Dansk Polar Center). Retrieved 23 June 2014. [118] “A Viking Feast - an abundance of foods” . Ribe Vikinge- center . Archived from the original on 14 July 2014. Re- "
//+ "		  trieved 19 June 2014. [119] Roesdahl, p. 110-111"
//+ "		  [120] Fondén, R; Leporanta, K; Svensson, U (2007). “Chapter 7. Nordic/Scandinavian Fermented Milk Products”. In Tamime, Adnan. Fermented Milks . Blackwell. doi : 10.1002/9780470995501.ch7 . ISBN "
//+ "		  9780632064588 . [121] “The Seastallion from Glendalough” (PDF) (in Danish). "
//+ "		  Vikingeskibsmuseet. Retrieved 19 June 2014. [122] Hall, A. R. 1999? “The Home: Food- Fruit, Grain and "
//+ "		  Vegetable.” Viking Age York. The Jorvik Viking Centre . [123] “The farm crops” . Ribe Vikingecenter . Retrieved 19 April "
//+ "		  2015. [124] “From grains to bread - coarse, heavy and filling” . Ribe Vikingecenter (in Danish). Archived from the original on "
//+ "		  14 July 2014. Retrieved 19 June 2014. [125] Bo Ejstrud et.al. (2011). “From Flax To Linen - experi- ments with flax at Ribe Viking Centre” (PDF). University of Southern Denmark . ISBN 978-87-992214-6-2 . Re- "
//+ "		  trieved 19 April 2015. [126] Kirsten Wolf: Daily Life of the Vikings Greenwood Press “Daily life through history” series, 2004, ISBN 0-313- "
//+ "		  32269-4 , Ch. 7 [127] Isak Ladegaard (19 November 2012). “How Vikings "
//+ "		  killed time” . ScienceNordic . Retrieved 1 March 2014. [128] “Games and entertainment in the Viking period” . Na- "
//+ "		  tional Museum of Denmark. Retrieved 20 April 2015. [129] Fotevikens Museum Official homepage [130] Darrell Markewitz 1998–2010. “IRON SMELTING at the Norse Encampment -Daily Life in the Viking Age circa 1000 AD at Vinland. The Viking Encampment liv- ing history program at Parks Canada L'Anse aux Mead- ows NHSC in Newfoundland” . Warehamforge.ca. Re- "
//+ "		  trieved 21 May 2012. [131] ReturnofDublin’sVikingWarship . Retrieved14Novem- "
//+ "		  ber 2007. [132] “Beyond Lands’ End: Viking Voyage 1000” . Doug- "
//+ "		  cabot.com. Retrieved 21 May 2012. [133] Shona Grimbly (16 August 2013). Encyclopedia of the Ancient World . Routledge. pp. 121–. ISBN 978-1-136- "
//+ "		  78688-4 . [134] Dennis Howard Green; Frank Siegmund (2003). The Continental Saxons from the Migration Period to the Tenth Century: An Ethnographic Perspective . Boydell Press. pp. "
//+ "		  306–. ISBN 978-1-84383-026-9 . [135] Howard D. Fabing. “On Going Berserk: A Neurochemi- "
//+ "		  cal Inquiry.” Scientific Monthly. 83 [Nov. 1956] p. 232 [136] Robert Wernick. The Vikings. Alexandria VA: Time- "
//+ "		  Life Books . 1979. p. 285 [137] Gareth Williams: Viking Money BBC History [138] Graham-Campbell, James: The Viking World , Frances Lincoln Ltd, London (2013). Maps of trade routes."
//+ "		  "
//+ ""
//+ "	**************************** Page 21****************************"
//+ "		  21"
//+ "		  [139] Andrew Curry (July 2008). “Raidersor Traders?\" . Smith- sonian Magazine . Smithsonian Institution. Retrieved 24 "
//+ "		  February 2014. [140] Vikings as traders , Teachers’ notes 5. Royal Museums "
//+ "		  Greenwich [141] “Herbs, spices and vegetables in the Viking period” . Na- "
//+ "		  tional Museum of Denmark. Retrieved 20 April 2015. [142] HeidiMichelleSherman(2008). BarbarianscometoMar- ket: The Emporia of Western Eurasia from 500 BC to AD 1000. ProQuest LLC. pp. 250–5. Retrieved 24 February "
//+ "		  2014. [143] HL Renart of Berwick: Glass Beads of the Viking Age . An inquiry into the glass beads of the Vikings. Sourced "
//+ "		  information and pictures. [144] Glass and Amber Regia Anglorum. Sourced information "
//+ "		  and pictures. [145] Yngve Vogt (1 November 2013). “Norwegian Vikings purchased silk from Persia” . Apollon - research magazine . "
//+ "		  University of Oslo. Retrieved 24 February 2014. [146] Marianne Vedeler: Silk for The Vikings , Oxbow 2014. [147] Elizabeth Wincott Heckett (2002). “Irish Viking Age silks and their place in Hiberno-Norse society” . De- partment of Archaeology, University College Cork, NUI Cork, Ireland. Textile Society of America Symposium Pro- ceedings (University of Nebraska - Lincoln ( Digital Com- "
//+ "		  mons )). Retrieved 28 February 2014. [148] Jørgensen, Lise Bender; Jesch, Judith (2002). “Rural Economy: Ecology, Hunting, Pastoralism, Agricultural and Nutritional Aspects”. The Scandinavians - from the Vendel Period to the Tenth Century . Center for Interdisci- "
//+ "		  plinary Research on Social Stress. pp. 131–7. [149] English Historical Documents, c. 500–1042 by Dorothy "
//+ "		  Whitelock; p.776 [150] Derry (2012). A History of Scandinavia: Norway, Swe- "
//+ "		  den, Denmark, Finland, Iceland , p. 16. [151] Northern Shores by Alan Palmer; p.21; ISBN 0-7195- "
//+ "		  6299-6 [152] The Viking Revival By Professor Andrew Wawn at bbc [153] The Oxford Illustrated History of the Vikings by Peter "
//+ "		  Hayes Sawyer ISBN 0-19-820526-0 [154] Hall, pp. 220–1; Fitzhugh and Ward, pp. 362–64 [155] Fitzhugh and Ward, p. 363 [156] Hall, p. 221 [157] Frank, Roberta (2000). International Scandinavian and MedievalStudiesinMemoryofGerdWolfgangWeber . Ed. "
//+ "		  Parnaso. p. 487. ISBN 978-88-86474-28-3 . [158] Did Vikings really wear horns on their helmets? , The Straight Dope, 7 December 2004. Retrieved 14 Novem- ber 2007."
//+ "		  [159] “Did Vikings wear horned helmets?\" . The Economist "
//+ "		  (www.economist.com). Retrieved 10 April 2014. [160] “The Gjermundbu Find - The Chieftain Warrior” . Re- "
//+ "		  trieved 10 April 2014. [161] Williams, G. (2001) How do we know about the Vikings? "
//+ "		  BBC .co.uk. Retrieved 14 November 2007. [162] Ullidtz, Per (2014). 1016: The Danish Conquest of Eng- "
//+ "		  land . Copenhagen: Books on Demand. p. 300. [163] “Annals of Human Genetics. Volume 72 Issue 3 Pages 337–348, May 2008” . Blackwell-synergy.com. Retrieved "
//+ "		  21 May 2012. [164] Helgason, A.; Hickey, E.; Goodacre, S.; Bosnes, V.; Ste- fánsson, K. R.; Ward, R.; Sykes, B. (2001). “MtDNA and the Islands of the North Atlantic: Estimating the Proportions of Norse and Gaelic Ancestry” . The American Journal of Human Genetics 68 (3): 723–37. "
//+ "		  doi : 10.1086/318785 . PMC 1274484 . PMID 11179019 . [165] Roger Highfield, “Vikings who chose a home in Shetland beforealifeofpillage” , Telegraph , 7April2005, accessed "
//+ "		  16 November 2008 [166] “Excavating Past Population Structures by Surname- Based Sampling; The Genetic Legacy of the Vikings in Northwest England, Georgina R. Bowden, Molec- ular Biology and Evolution, 20 November 2007” . Mbe.oxfordjournals.org. 20 November 2007. Retrieved "
//+ "		  21 May 2012. [167] “A Y Chromosome Census of the British Isles, Capelli, Current Biology, Vol. 13, May 27, 2003” (PDF). Re- "
//+ "		  trieved 21 May 2012. [168] JamesRanderson,“ProofofLiverpool’sVikingpast” , The "
//+ "		  Guardian , 3 Dec 2007, accessed 16 November 2008 [169] “DNA shows Celtic hero Somerled’s Viking roots” . Scots- man . 26 April 2005. "
//+ "		  10 References "
//+ "		  • Brink, Stefan (2008). “Who were the Vikings?\". In "
//+ "		  ledge. pp. 4–10. ISBN 9780415692625 . • Brookes, Ian (2004). Chambers concise dictionary . "
//+ "		  • D'Amato, Raffaele (2010). The Varangian Guard "
//+ "		  179-5 . • Derry, T.K. (2012). A History of Scandinavia: Nor- and Minneapolis: University of Minnesota Press. "
//+ "		  ISBN 978-0-81663-799-7 . • Educational Company of Ireland (10 October Dictionary . Roberts Rinehart. ISBN 978-1-4616- 6031-6 ."
//+ "		  Brink, Stefan; Price, Neil. The Viking World . Rout-"
//+ "		Allied Publishers. ISBN 9798186062363 ."
//+ "		988-453 . Osprey Publishing. ISBN 978-1-84908-"
//+ "		way, Sweden, Denmark, Finland, Iceland . London"
//+ "		2000). Irish-English/English-Irish Easy Reference"
//+ ""
//+ ""
//+ "	**************************** Page 22****************************"
//+ "		  22 12 EXTERNAL LINKS"
//+ "		  • Fitzhugh, William W.; Ward, Elisabeth I. (2000). the National Museum of Natural History, Smithso- nian Institution, Washington D.C., April 29, 2000 - September 5, 2000) . Washington: Smithsonian In- "
//+ "		  stitution Press. ISBN 9781560989707 . • Hall, Richard Andrew (2007). The World "
//+ "		  9780500051443 . • Hall, Richard (January 1990). Viking Age Ar- "
//+ "		  9780747800637 . • Lindqvist, Thomas (4 September 2003). “Early Po- In Helle, Knut. The Cambridge History of Scandi- navia: Prehistory to 1520 . Cambridge University "
//+ "		  Press. pp. 160–67. ISBN 9780521472999 . • Roesdahl, Else (1998). The Vikings . Penguin "
//+ "		  • Sawyer, Peter Hayes (1 February 1972). Age "
//+ "		  9780312013653 . • Williams, Gareth (2007). “Kingship, Christianity silver economy in the Viking Age”. In Graham- Campbell, James; Williams, Gareth. Silver Econ- omy in the Viking Age . Left Coast Press. pp. 177– "
//+ "		  214. ISBN 9781598742220 . • Wolf, Kirsten (1 January 2004). Daily Life of the 0-313-32269-3 . "
//+ "		  11 Further reading "
//+ "		  • Askeberg, Fritz (1944). Norden och kontinenten i "
//+ "		  Almqvist & Wiksells boktr. • Downham, Clare (2007). Viking kings of Britain "
//+ "		  Dunedin Academic Press. ISBN 9781903765890 . • Downham, Clare (2011). “Viking Ethnicities. A (2012), pp. 1–12. PDF Academic.edu - registra- "
//+ "		  tion required • Hadley, Dawn (2006). The Vikings in England: Set- "
//+ "		  Press. ISBN 9780719059827 . • Heide, Eldar (2005). “Víking – 'rower shifting'? filologi (PDF) 120 . C.W.K. Gleerup. pp. 41–54."
//+ "		  • Heide, Eldar (2008). “Viking, week, and Widsith. filologi (PDF) 123 . C.W.K. Gleerup. pp. 23–28. "
//+ "		  Archived from the original (PDF) on 2008. • Hodges, Richard (2006). Goodbye to the Vikings: ald Duckworth & Company Limited. ISBN "
//+ "		  9780715634295 . • Svanberg, Fredrik (2003). Decolonizing the Viking "
//+ "		  9789122020066 . • Wamers, Egon (1985). Insularer Metallschmuck Untersuchungen zur skandinavischen Westex- pansion . Neumünster: Karl Wachholtz. ISBN "
//+ "		  9783529011566 . • Wamers, Egon (1998). “Insular Finds in Viking way”. In Clarke, H.B.; Mhaonaigh, M. Ní; Floinn, R. Ó. Ireland and Scandinavia in the Early Viking Age . Dublin: Four Courts Press. pp. 37–72. ISBN "
//+ "		  9781851822355 . • Wawn, M.A. (2000). The Vikings and the Victori- Britain . Woodbridge: Boydell and Brewer. ISBN 9780859916448 . "
//+ "		  12 External links "
//+ "		  • Vikings —View videos at The History Channel • Copenhagen-Portal – The Danish Vikings • BBC: History of Vikings • Encyclopædia Britannica: Viking, or Norseman, or "
//+ "		  • Borg Viking museum, Norway • Ibn Fadlan and the Rusiyyah, by James E. Mont- "
//+ "		  • Reassessing what we collect website – Viking and "
//+ "		  don with objects and images • Wawm, Andrew, The Viking Revival - BBC On- 2011)"
//+ "		  Vikings: The North Atlantic Saga ; (an Exhibition at"
//+ "		A reply to Harald Bjorvand”. Arkiv för nordisk"
//+ "		Re-Reading Early Medieval Archaeology . Ger-"
//+ "		of the Vikings . Thames & Hudson. ISBN"
//+ "		chaeology in Britain and Ireland . Shire. ISBN"
//+ "		litical Organisation: (a) An Introductory Survey”."
//+ "		Age . Almqvist & Wiksell International. ISBN"
//+ "		wikingerzeitlichen Gräbern Nordeuropas."
//+ "		Books. ISBN 9780140252828 ."
//+ "		Age Scandinavia and the State Formation of Nor-"
//+ "		of the Vikings . Palgrave Macmillan. ISBN"
//+ "		and coinage: monetary and political perspectives on"
//+ "		ans: Inventing the Old North in Nineteenth Century"
//+ "		Vikings . Greenwood Publishing Group. ISBN 978-"
//+ "		gammal tid: studier i forngermansk kulturhistoria ."
//+ "		Northman, or Varangian (people)"
//+ "		and Ireland: the dynasty of ́Ívarr to A.D. 1014 ."
//+ "		gomery, with full translation of Ibn Fadlan"
//+ "		Historiographic Overview”, History Compass 10.1"
//+ "		Danish London History of Viking and Danish Lon-"
//+ "		line,AncientHistoryinDepth(updated17February"
//+ "		tlement, Society and Culture . Manchester University"
//+ "		An etymological contribution”. Arkiv för nordisk"
//+ ""
//+ ""
//+ "	**************************** Page 23****************************"
//+ "		  23"
//+ "		  13 Text and image sources, contributors, and licenses "
//+ "		  13.1 Text • Vikings Source: https://en.wikipedia.org/wiki/Vikings?oldid=708009778 Contributors: AxelBoldt, Joao, Brion VIBBER, Mav, Bryan Arj, Montrealais, Tzartzam, Rickyrab, Edward, Patrick, D, Michael Hardy, Tim Starling, Dan Koehl, Nixdorf, Liftarn, Stephen C. Carlson, Mic, Ixfd64, Sannse, GTBacchus, Gustavf~enwiki, Egil, Ahoerstemeier, Pjamescowie, Angela, Alvaro, Julesd, Ugen64, Glenn, Marteau, Haabet, Kils, Evercat, Ruhrjung, Harvester, Ghewgill, Johan Magnus, Raven in Orbit, MasterDirk, Mulad, Hemmer, Smith03, Vikings, Adam Bishop, A1r, Nohat, Dino, Dysprosia, Polaris999, Timc, DJ Clayworth, Haukurth, Tpbradbury, Itai, EikwaR, Topbanana, Fvw, Warofdreams, Wetman, Pstudier, Secretlondon, Finlay McWalter, Owen, Dimadick, Rogper~enwiki, Amphioxys, Robbot, Fredrik, Kizor, Chris 73, RedWolf, Goethean, Sam Spade, AllanE, Mayooranathan, Sverdrup, PedroPVZ, Bkell, Hadal, JesseW, Saforrest, Wereon, Barce, Vikingstad, Lupo, Cutler, SpellBott, Dina, Carnildo, DigitalMedievalist, Apol0gies, Centrx, DocWatson42, ChristopherParham, Djinn112, Cfp, Jao, Inter, Yonderway, Wiglaf, Merlante, Tom harrison, Lupin, Ferkelparade, Zigger, Bradeos Graphon, Everyking, No Guru, Ken- neth Alan, Niteowlneils, BjornSandberg, FrYGuY, Guusbosman, Luigi30, Solipsist, Brockert, Apv, Adam McMaster, Bobblewik, Alan Au, Telsa, Quackor, Gazibara, SarekOfVulcan, Knutux, Zeimusu, Quadell, Antandrus, Beland, Madmagic, Evertype, Jossi, 1297, Rd- smith4, Gene s, PSzalapski, Kazuki, Sam Hocevar, Kahkonen, Brisky, Mrrhum, Neutrality, Okapi~enwiki, Joyous!, Fanghong~enwiki, Lacrimosus, SYSS Mouse, Freakofnurture, Duja, Stepp-Wulf, Econrad, Pyrop, A-giau, Chris j wood, Discospinster, Rich Farmbrough, Vague Rant, AxSkov, Vsmith, Florian Blaschke, Dreiss2, YUL89YYZ, Bishonen, Xezbeth, Ponder, Roodog2k, Zazou, Arthur Hol- land, Dbachmann, Captain Q, Mani1, Paul August, Bo Lindbergh, SpookyMulder, Bender235, Rubicon, ESkog, Kbh3rd, Swid, JoeS- mack, Aecis, Livajo, Sfahey, Mulder1982, Kwamikagami, QuartierLatin1968, Kross, -jkb-, Shanes, Art LaPella, RoyBoy, Bookofjude, Screensaver, Adambro, Jonathan Drain, Bobo192, Infocidal, Moliate, Sabretooth, Greenleaf~enwiki, טראהנייר ירעל, Zetawoof, Vanished user 19794758563875, Sam Korn, Polylerus, Pharos, Pearle, Jonathunder, Irishpunktom, Supersexyspacemonkey, Eddideigel, Mareino, A2Kafir, Ogress, Jumbuck, Red Winged Duck, Denhulde, Grutness, PMLF, Abedegno, ThePedanticPrick, Kessler, Rd232, Keenan Pep- per, Jeltz, Plumbago, Fornadan, Lord Pistachio, Nwinther, Ninio, MarkGallagher, Lightdarkness, Sugaar, Pippu d'Angelo, Katefan0, Jjhake, Svartalf, Snowolf, Gzur, Dkikizas, Binabik80, SidP, Almafeta, Evil Monkey, Max Naylor, RainbowOfLight, Grenavitar, Randy Johnston, Sumergocognito, LordAmeth, Ghirlandajo, Inge, Brycew, RyanGerbil10, Hijiri88, Crosbiesmith, Feezo, Woohookitty, Jacen Aratan, Henrik, TigerShark, ScottDavis, LOL, PaulHammond, Uncle G, Briangotts, Dodiad, JeremyA, Jeff3000, MONGO, Twthmoses, Thebogusman, GregorB, SDC, Banpei~enwiki, Heritagelost, Eirikr, A3r0, Dysepsion, GSlicer, Ashmoo, Graham87, Nickaubert, Magis- ter Mathematicae, Rlf~enwiki, BD2412, Qwertyus, Dwarf Kirlston, Erraunt, Amaxson, Rjwilmsi, Angusmclellan, Panoptical, Hiberni- antears, Quiddity, BlueMoonlet, JHMM13, SMC, Bhadani, Williamborg, Matt Deres, Yamamoto Ichiro, Scorpionman, Falphin, FlaBot, Gmanacsa, Latka, Loggie, Nivix, CarolGray, Asgath, RexNL, Gurch, MartinHvidberg, OrbitOne, Darlene4, Intgr, D.brodale, Alphachimp, Rell Canis, Windharp, Osli73, Scimitar, Chobot, Parallel or Together?, Garas, Aethralis, Gdrbot, Bgwhite, Chwyatt, Gwernol, Tone, The Rambling Man, YurikBot, Wavelength, RobotE, Jimp, Dannycas, Gregalodon, Daverocks, RussBot, Manicsleeper, Muchness, The Storm Surfer, Pigman, Gardar Rurak, DanMS, Stephenb, Gaius Cornelius, CambridgeBayWeather, Wimt, Manxruler, JustinLong, NawlinWiki, Wiki alf, Bachrach44, Magicmonster, Astral, The Ogre, Lowe4091, Bloodofox, Murphys Law, Tetsuo, ExRat, Merman, Maverick Leon- hart, Oliver Gehrke, Chooserr, Irishguy, Rbarreira, Anetode, Dfgarcia, Mlouns, Nanten, R.D.H. (Ghost In The Machine), Yano, PM Poon, Nut-meg, Xompanthy, Sandman1142, Morgan Leigh, DeadEyeArrow, Bota47, TigerPaw2154, Kewp, Sabrenaut, Slicing, Johnsem- lak, HopeSeekr of xMule, Igiffin, Novasource, FF2010, Sadistik, Whitejay251, Laszlo Panaflex, Zzuuzz, Linuxx, Theda, Closedmouth, Denisutku, Orland, Elim Garak, Pietdesomere, Sarpedon Achilles, H@r@ld, Tsunaminoai, Xareu bs, De Administrando Imperio, Leonar- doRob0t, Peter, Hayden120, ArielGold, Curpsbot-unicodify, Rredwell, Eaefremov, Katieh5584, Kungfuadam, RG2, GrinBot~enwiki, Airconswitch, Sam Weber, DVD R W, CIreland, Victor falk, That Guy, From That Show!, Luk, Iwasabducted, DocendoDiscimus, Mlibby, Yvwv, SmackBot, Looper5920, Iamhove, Bozebus17, Ulterior19802005, Moeron, Smitz, ElectricRay, Hanchi, KnowledgeOfSelf, Roy- alguard11, Arkaru, AndyZ, KocjoBot~enwiki, Jagged 85, Big Adamsky, Chairman S., Alepik, Alksub, EncycloPetey, Delldot, Hardy- plants, LuciferMorgan, Dpwkbw, Srnec, Master Deusoma, Septegram, Commander Keane bot, Nathan.tang, Cool3, Marktreut, Peter Isotalo, Gilliam, Ohnoitsjamie, Hmains, Betacommand, Skizzik, Angelbo, Durova, Poulsen, Master Jay, Ciacchi, Keegan, TimBentley, Timbouctou, Roede, Pieter Kuiper, Grimhelm, Moogle001, Fluri, Apeloverage, Papa November, Balin42632003, Barend, WeniWidi- Wiki, Viewfinder, Ravens326, Sparsefarce, DHN-bot~enwiki, Colonies Chris, AKMask, Scwlong, J00tel, Tsca.bot, Can't sleep, clown will eat me, Geoffrey Matthews, Shalom Yechiel, Alphathon, Fotoguzzi, OrphanBot, Onorem, Rrburke, Pnkrockr, Mindstar, Aces lead, Edivorce, Puck34, Treygdor, Stevenmitchell, BIL, Krich, PiMaster3, Emre D., Hakozen, NoIdeaNick, Ghiraddje, Nibuod, Sirgregmac, Bowlhover, Nakon, Magore, Tomasooie, RaCha'ar, Drogo, Bardytown, RandomP, Tanber, Elston Gunn, Only, Iridescence, Sethwood- worth, Aji23, Bigsteeve, Hammer1980, The PIPE, DMacks, Wizardman, Maelnuneb, Fosco~enwiki, Metamagician3000, Aaker, Jfin- gers88, DDima, Epf, Risker, Tfl, Kukini, Njård, BlackTerror, Nasz, Ohconfucius, Mrego, SashatoBot, Rockvee, Yakbasser, Anlace, Mouse Nightshirt, Attys, Haakon Thue Lie, Methylparabex, Kuru, John, Livingintellect, Euchiasmus, Grumpy444grumpy~enwiki, Scien- tizzle, Treyt021, FirstDark, Kipala, Vanished188, ML5, Pomakis, NewTestLeper79, Ishmaelblues, JorisvS, Hemmingsen, Ospinad, Mon Vier, Thijs en, Jazriel, Polyhymnia, Mr. Lefty, HADRIANVS, Gs371, Stoa, PParkerT, The Man in Question, Bless sins, Pointman323, Yvesnimmo, Mr Stephen, Papabrow, Godfrey Daniel, Vendetta411, MrArt, Waggers, Mets501, Drieakko, Neddyseagoon, Marhawkman, Ryulong, Jstreutker, MTSbot~enwiki, Skinsmoke, Tbonequeen79, JPatten, Scotty mckilton, Nicolharper, Squirepants101, Sifaka, Keycard, Thefuguestate, Swishtheshot, Obtsu, Emx~enwiki, DouglasCalvert, Seqsea, Fan-1967, Iridescent, Pipedreambomb, Benchik, Joseph Solis in Australia, Ojan, Kernow, Shoeofdeath, Manickennel, Sander Säde, Amakuru, SweetNeo85, ChadyWady, Sparviere, Scarlet Lioness, Tawkerbot2, Nerfer, Fdssdf, CalebNoble, The Haunted Angel, Akseli, JForget, CmdrObot, Evilasiangenius, Eiorgiomugini, Augustulus, RedRollerskate, Jzefran, Dgw, Londoncalling7, AshLin, Ferdiaob, Lazulilasher, Musashi1600, Shizane, Moreschi, Joshuawright11, Ken Gallager, Probugcatcher, Tim1988, RedSirus, Xilog, Rudjek, LucReid, Viking97, Ntsimp, Mixolydian, Treybien, MikeChristie, Skiley22, Steel, Vanished user vjhsduheuiui4t5hjri, JonnyLightning, Travelbird, Denghu, Otto4711, Faismoerko, Lomesir22, Tkynerd, Tawkerbot4, Doug Weller, DumbBOT, Oyadama, Eirik Lie, Chrislk02, Phydend, DBaba, Walgamanus, Toreroraas, Kozuch, Wagapm, After Midnight, Omicronpersei8, Oudeis23, Dan102001, Satori Son, Kennet.mattfolk, FrancoGG, Jon C., JAF1970, Barticus88, Wikid77, D4g0thur, Ishdarian, Ep9206, Kahriman~enwiki, 24fan24, Mojo Hand, Sopranosmob781, Headbomb, John254, SGGH, Folantin, Doyley, Karamell, Esbullin, DotDarkCloud, CharlotteWebb, WhaleyTim, Comanchecph, Qp10qp, NatalieErin, Vikingman318, Escarbot, Oreo Priest, ILHI, Hmrox, Hires an editor, Gossamers, AntiVandalBot, Luna Santin, Cmagnan~enwiki, Quintote, Brendandh, Tmopkisn, Mack2, D. Webb, Wiki user06, Desalvionjr, Pconlon, Janepuke, Gökhan, Canadian-Bacon, Kariteh, Ioeth, Woodstein52, Arsenikk, Mr buick, JAnDbot, Xhi- enne, Narssarssuaq, Dogru144, Husond, LEVScrew, MER-C, Dsp13, Instinct, WikipedianProlific, Trellia, Hello32020, Michig, LmoneyK, OhanaUnited, Andonic, Bearpatch, Hut 8.5, J-stan, Leolaursen, IcyClaws, PhilKnight, GoodDamon, Extreme barry, Annasweden, Phar- illon, Bongwarrior, VoABot II, Mrund, JNW, Turkishbob, JamesBWatson, SHCarter, Trhickman, Think outside the box, Fragasm, Burn-"
//+ "		  Derksen, The Anome, Berek, Sjc, Amillar, Eob, Danny, Rmhermen, Christian List, Ben-Zin~enwiki, Ant, Perique des Palottes, Heron,"
//+ ""
//+ ""
//+ "	**************************** Page 24****************************"
//+ "		  24 13 TEXT AND IMAGE SOURCES, CONTRIBUTORS, AND LICENSES "
//+ "		  inate 58, Thorht, Jeffery21, Singularity, Cartoon Boy, EliteOneOneSeven, SparrowsWing, Jvhertum, RebekahThorn, WikieWikieWikie, Indon, Jaiotu, Crococolana, Curtisdozier, Berig, Pearking, Allstarecho, Anrie, Spellmaster, Jacobko, Glen, DerHexer, Jimipage26, Cs007, Brumblebug, Lecomte99, Amitchell125, NatureA16, Ksero, Hdt83, MartinBot, Schmloof, Darkbreed, Gronkmeister, Autosol, KiwiCurt, Arjun01, NAHID, Rettetast, Roboticus, Keith D, Zouavman Le Zouave, R'n'B, LinguisticDemographer, CommonsDelinker, Rockgod214, AlexiusHoratius, Vkire, PapalAuthoritah, Bentaguayre, Mbhiii, Francis Tyers, Dudley Miles, Wiki Raja, MopherRabbit, Mausy5043, Dinkytown, AlphaEta, Sasajid, PharaohoftheWizards, Slogsweep, Anrkist, LordSamael, EscapingLife, Everlong16, Numbo3, Richiekim, Wuffanie, Lovesmehon, Frankydum, HelgeRieder, Ginsengbomb, Thatoneguy7878, Eliz81, Dirkmav41, Thaurisil, Cop 663, Dispenser, Bot-Schafter, Codohu, Johnbod, Joman726, Thorgis, Jeepday, Memory palace, Garret Beaumain, Chicken815, TwentyFours, Järnvarg, AntiSpamBot, Junafani, Scapegoat4242, Knight of BAAWA, Andejons, Bushcarrot, NewEnglandYankee, Matthardingu, SJP, Bobianite, Gregfitzy,Squidfryerchef,Aatomic1,Mufka,Nwmorris,Concoll,Shoessss,Cmichael,BrettAllen,Atheuz,Alynn2,Warlordwolf,Afv2006, Rolfenstein, Morganthompson, VikingAD, Jonah573, Mathesoneon, Bonadea, DrNegative, Rebellion101, Beezhive, Martial75, Piepoop, 66dracula66, Idioma-bot, Spellcast, ACSE, TeamZissou, Lights, Shadowblade12088, UnicornTapestry, Deor, King Lopez, VolkovBot, Thedjatclubrock, Thomas.W, Aciram, Horatio325, Carter, LarsThomsen, Holme053, Seattle Skier, Ph8l, BismarckTheIronChancellor, Ryan032, WOSlinker, Kaiwynn, Philip Trueman, Spurius Furius Fusus, TXiKiBoT, Celtus, Knowledgebycoop, Dawidbernard, Gombo, Saleems, UlrikOldenburg, Jem4water, Ann Stouter, JamesMcCloud129, Heri Joensen, Kayli Mullins, AlysTarr, Gabhala, Qxz, Triple- jumper, Klamber, Lradrama, Beyond silence, Gekritzl, Cahlersmeyer, Corvus cornix, Digby Tantrum, Shadowviking, Garygame, Arigato1, BotKung, Spazy gim, Shouriki, InfinityAndBeyond, Jmq324, CO, Eldredo, Wenli, Ashnard, Enigmaman, Viktor-viking, Spinningspark, Princess Moanna, Top Sock Puppet, Nibios, Twooars, AlleborgoBot, Georgethedecider, Ojevindlang, Celticpete, Austriacus, S.Örvarr.S, Sausneb, Safouane, Keagzta, Boydenmb, Arabicmusic, Langskip, SieBot, Coffee, Oxxo, Frans Fowler, Spartan, Restre419, Andrarias, Sophos II, BotMultichill, Jauerback, Sakkura, Stanning, Oydman, Sonyack, Gerakibot, Caltas, Triwbe, Sat84, Duplicity, Keilana, Sar0006, Tiptoety, Radon210, Graytrees~enwiki, Suwatest, Walpole, Nua eire, HerveGourmelon, Cheeseloop, Hiddenfromview, Xhammer3, Fausts, Oxymoron83, AngelOfSadness, KPH2293, Deathklok, Gbbinning, Lightmouse, Slowdeath2007, Avtomat, Taggard, Danelo, Benoni- Bot~enwiki, Kingryanv, OKBot, Nancy, Wj7008, Callopeaatsaaps, Asia345, Calatayudboy, Paiev, Rkarlsba, C'est moi, Segregold, Termer, Anchor Link Bot, Runesubaru, Witchkraut, Hamiltondaniel, Takeshi357, Wiki-BT, Dust Filter, Ken123BOT, Dabomb87, Flavigny20, Verdadero, Escape Orbit, G6tbj, EmanWilm, Roxorius, Celticsco, Mr. Granger, Gonzalo.jimenez, Truebluedub, ClueBot, Dave T Hobbit, Lazserus, Avenged Eightfold, TheUnfan, Lagwagon1991, The Thing That Should Not Be, Pro123, DLibrasnow, Molchar, Annabelle623, Plastikspork, Banasta, Iandiver, Saddhiyama, Popmonkey58, Wiccasha, Ashwin18, Viking berserker, Timberframe, Axeman999, Coun- terVandalismBot, CEWIKIUMRUM, Niceguyedc, Richerman, Parkwells, P. S. Burton, Admyq5, Marselan, Melsueherrmann, Dhacker29, Auntof6, Abcdrew, LoneKnightProductions, Hhallgri, Asomeowen, Excirial, Alexbot, Robbie098, Kurtcobain911, Wikiscribe, Mwintin, Maxwashere, 360drummer, W3lucario, Daikorethvai, Catletd, NuclearWarfare, Utopial, HOBOslayer31093, Arjayay, Luje94, Terra Xin, Leatharnak, Superham07, Dekisugi, Chrispy4, SchreiberBike, Audaciter, Carriearchdale, Langrenn, Killvikingkill, Tubten, Mor- gan quieres huevos, Aitias, Kryon562, Kurtcobain321, Firefinger3295, Eggc1, Hereitisthen, Party, Botang, Advanceandawesome, God- ofthecoldwhitesilence, Vitross, Profplum999, Abospo, XLinkBot, Spitfire, Pichpich, My name is elmo, Snikburz, Zerf~enwiki, Repeet, CrazyTOMM, Nickthe003, Doodo~enwiki, Lered, Nepenthes, Sonty567, Avoided, Catznoble, Rajansubedi, Kkv123, D0nth4t3m3, Sa- lokineragn, Foundinkualalumpur, Mtdewmaniac21, MarmadukePercy, Sleptrip, Casewicz, Atoric, ChessA4, Schildewaert, Booldey wolf, Addbot, Yousou, Nortonius, DougsTech, Holt, Eskil S, CactusWriter, RJW37, BabelStone, Daicaregos, Fernlea, Mecjmr, LinkFA-Bot, Robtj966, Numbo3-bot, Tide rolls, Blood93, SasiSasi, Jarble, Greyhood, GloriousLeader, Legobot, PlankBot, Luckas-bot, Yobot, Kar- tano, Yngvadottir, LemonMonday, AnomieBOT, Anne McDermott, Nortmannus, Hadden, Rubinbot, 1exec1, AngusCA, Bluerasberry, Danno uk, Stuka115, 78.109.35W, Flahblahblah, MidnightBlueMan, LilHelpa, TheRealNightRider, MauritsBot, Xqbot, Addihockey10, JimVC3, Gilo1969, Tad Lincoln, Gjoh, BritishWatcher, J04n, Brandon5485, Amaury, Philip72, Sabrebd, Glic16, Le Deluge, Ellenois, Thehelpfulbot, Ddd0dd, Hyperboreer, FrescoBot, Jancyclops, Tobby72, Misiekuk, Urgos, HJ Mitchell, Doremo, Endofskull, Cannolis, SL93, Hchc2009, Pshent, Toolboks, Pinethicket, Elockid, Abductive, Michellecornelison, Jonesey95, FILWISE, Jamesinderbyshire, Jan- dalhandler, Kgrad, TobeBot, Dinamik-bot, Vrenator, Begoon, Anglo-Saxon1, Aoidh, Brianann MacAmhlaidh, Os150296, StephenPater- noster, ExtraOrdinary Apprentice, Reach Out to the Truth, DARTH SIDIOUS 2, OnWikiNo, Org.aidepikiw, Eldar Heide, Mandolinface, DASHBot,TGCP,EmausBot,JohnofReading,Shamalamastreetman,Calanon,Optiguy54,Dewritech,GoingBatty,Fakirbakir,Rarevogel, Gwillhickers, Somebody500, Wikipelli, JDDJS, Bongoramsey, Iñaki Salazar, Faye Bloom, Everard Proudfoot, Wayne Slam, Dala-Freyr, CubeDigit, Erianna, Snaevar, Jeroen1961, Arnegeo, Brandmeister, Lisztrachmaninovfan, L Kensington, Fhonan, Philafrenzy, Stkncheese, ChuispastonBot, Ole a33, TariSaint, Peter Karlsen, Hi csports, Palaeozoic99, Revenance, Sven Manguard, DASHBotAV, Gmoney345, Shamory1, Stekse, E. Fokker, Helpsome, Facebook owner, ClueBot NG, ThanatosXRS, Catlemur, Joefromrandb, ZarlanTheGreen, Snot- bot, Frietjes, Alphasinus, Mesoderm, Newyorkadam, Tjodrik, Ceaseless, Zwesomehandz, Helpful Pixie Bot, Gitangurang, Excerpted31, Stas-Adolf, BG19bot, JeBonSer, Vagobot, Hashem sfarim, Wzrd1, Stallican, Kanjawe, Mysterytrey, Marcocapelle, Merchant banker, Carlstak, Dainomite, Trevayne08, Writ Keeper, Nameyxe, Newsleep, BattyBot, Boeing720, Th4n3r, Cyberbot II, GoShow, Louey37, Mmwikiest, Khazar2, JYBot, Dexbot, Caroline1981, Hmainsbot1, Mogism, Kbog, Ipad9, Tuco Il Brutto, Barayev, Krakkos, JustAMuggle, JaviP96, Leprof 7272, Faizan, Noelmantra, SaturatedFats, Obenritter, Jodosma, Law8f, Svedjebruk, Askold, Lewisbookreviews, Avi8tor, Eric Corbett, Sam Sailor, RhinoMind, Rfischer8655, Editguy111, Sthelen.aqua, MjolnirPants, Library Guy, Мехтех, Filedelinkerbot, Green daemon, William Harris, Paleolithic Man, Izotz Aro, Galaad1k, Harunabdr, Wikifiveoh, HafizHanif, Troy Lasley, Anjalikiggal, Grisepik, KasparBot, Sweepy, Connor Machiavelli and Anonymous: 1456 "
//+ "		  13.2 Images • File:Basse-Normandie_flag.svg Source: https://upload.wikimedia.org/wikipedia/commons/1/10/Flag_of_Basse-Normandie.svg alt='Haute-Normandie flag.svg' src='https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/Haute-Normandie_flag.svg/ 30px-Haute-Normandie_flag.svg.png' width='30' height='18' srcset='https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/ Haute-Normandie_flag.svg/45px-Haute-Normandie_flag.svg.png 1.5x, https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/ Haute-Normandie_flag.svg/60px-Haute-Normandie_flag.svg.png 2x' data-file-width='500' data-file-height='300' /></a> Original artist: "
//+ "		  Zorlot • File:Beserker,_Lewis_Chessmen,_British_Museum.jpg Source: https://upload.wikimedia.org/wikipedia/commons/0/04/Beserker% "
//+ "		  Original artist: Rob Roy • File:Blair_A'Bhuailte_and_Loch_Leum_na_Luirginn.jpg Source: https://upload.wikimedia.org/wikipedia/commons/4/4d/Blair_"
//+ "		  License: GFDL Contributors: own work + <a href='//commons.wikimedia.org/wiki/File:Haute-Normandie_flag.svg' class='image'><img"
//+ "		2C_Lewis_Chessmen%2C_British_Museum.jpg License: CC BY 2.0 Contributors: Flickr : Beserker, Lewis Chessmen, British Museum"
//+ "		A%27Bhuailte_and_Loch_Leum_na_Luirginn.jpg License: CC BY-SA 2.0 Contributors: From geograph.org.uk Original artist: Sarah"
//+ ""
//+ ""
//+ "	**************************** Page 25****************************"
//+ "		  13.2 Images 25 "
//+ "		  Charlesworth • File:Box_and_scales.jpg Source: https://upload.wikimedia.org/wikipedia/commons/e/e3/Box_and_scales.jpg License: CC BY-SA 3.0 "
//+ "		  • File:Busdorf_-_Haithabu_-_Wikinger-Häuser_05_ies.jpg Source: https://upload.wikimedia.org/wikipedia/commons/2/2b/Busdorf_ "
//+ "		  • File:Chieftains_house_(reconstruction).jpg Source: https://upload.wikimedia.org/wikipedia/commons/5/53/Chieftains_house_ "
//+ "		  • File:Commons-logo.svg Source: https://upload.wikimedia.org/wikipedia/en/4/4a/Commons-logo.svg License: CC-BY-SA-3.0 Contribu- "
//+ "		  • File:Eiríksstaðir_-_Wikingerschmuck.jpg Source: https://upload.wikimedia.org/wikipedia/commons/c/c1/Eir%C3%ADkssta%C3% "
//+ "		  • File:Europe_814.svg Source: https://upload.wikimedia.org/wikipedia/commons/9/90/Europe_814.svg License: CC BY-SA 3.0 Contrib- "
//+ "		  Hel-hama • File:Exhibition_in_Viking_Ship_Museum,_Oslo_01.jpg Source: https://upload.wikimedia.org/wikipedia/commons/2/2b/ "
//+ "		  Wysocki • File:Faroe_stamps_515-517_everyday_life_in_the_viking_age.jpg Source: https://upload.wikimedia.org/wikipedia/commons/e/e0/ "
//+ "		  • File:Flag_of_Belarus.svg Source: https://upload.wikimedia.org/wikipedia/commons/8/85/Flag_of_Belarus.svg License: Public domain "
//+ "		  • File:Flag_of_Denmark.svg Source: https://upload.wikimedia.org/wikipedia/commons/9/9c/Flag_of_Denmark.svg License: Public do- "
//+ "		  • File:Flag_of_Estonia.svg Source: https://upload.wikimedia.org/wikipedia/commons/8/8f/Flag_of_Estonia.svg License: Public domain "
//+ "		  to match the image at [1 ]. • File:Flag_of_Finland.svg Source: https://upload.wikimedia.org/wikipedia/commons/b/bc/Flag_of_Finland.svg License: Public domain "
//+ "		  • File:Flag_of_Germany.svg Source: https://upload.wikimedia.org/wikipedia/en/b/ba/Flag_of_Germany.svg License: PD Contributors: ? "
//+ "		  • File:Flag_of_Iceland.svg Source: https://upload.wikimedia.org/wikipedia/commons/c/ce/Flag_of_Iceland.svg License: Public domain "
//+ "		  • File:Flag_of_Italy.svg Source: https://upload.wikimedia.org/wikipedia/en/0/03/Flag_of_Italy.svg License: PD Contributors: ? Original "
//+ "		  • File:Flag_of_Latvia.svg Source: https://upload.wikimedia.org/wikipedia/commons/8/84/Flag_of_Latvia.svg License: Public domain "
//+ "		  • File:Flag_of_Norway.svg Source: https://upload.wikimedia.org/wikipedia/commons/d/d9/Flag_of_Norway.svg License: Public domain "
//+ "		  • File:Flag_of_Russia.svg Source: https://upload.wikimedia.org/wikipedia/en/f/f3/Flag_of_Russia.svg License: PD Contributors: ? Origi- "
//+ "		  • File:Flag_of_Sweden.svg Source: https://upload.wikimedia.org/wikipedia/en/4/4c/Flag_of_Sweden.svg License: PD Contributors: ? "
//+ "		  • File:Flag_of_Ukraine.svg Source: https://upload.wikimedia.org/wikipedia/commons/4/49/Flag_of_Ukraine.svg License: Public domain "
//+ "		  • File:Flag_of_the_United_Kingdom.svg Source: https://upload.wikimedia.org/wikipedia/en/a/ae/Flag_of_the_United_Kingdom.svg Li- "
//+ "		  • File:Four_Provinces_Flag.svg Source: https://upload.wikimedia.org/wikipedia/commons/6/65/Four_Provinces_Flag.svg License: CC "
//+ "		  • File:Fyrkat_hus_stor.jpg Source: https://upload.wikimedia.org/wikipedia/commons/d/dd/Fyrkat_hus_stor.jpg License: CC-BY-SA-3.0 "
//+ "		  • File:Gamla_Uppsala_-_line_of_tumuli.jpg Source: https://upload.wikimedia.org/wikipedia/commons/f/f5/Gamla_Uppsala_-_line_ "
//+ "		  • File:JellingeStoneLargeInscription.JPG Source: https://upload.wikimedia.org/wikipedia/commons/4/4c/ "
//+ "		  • File:Jomvikings_Winkinger_Kampftraining.webm Source: https://upload.wikimedia.org/wikipedia/commons/b/b6/Jomvikings_ "
//+ "		  • File:Lbs_fragm_82,_0001v_-_1.jpg Source: https://upload.wikimedia.org/wikipedia/commons/7/73/Lbs_fragm_82%2C_0001v_-_1. "
//+ "		  Library of Iceland • File:Lindholm_hoeje(Dänemark).jpg Source: https://upload.wikimedia.org/wikipedia/commons/6/6a/Lindholm_hoeje%28D%C3%"
//+ "		  Contributors: Own work Original artist: Berig"
//+ "		-_Haithabu_-_Wikinger-H%C3%A4user_05_ies.jpg License: CC BY-SA 3.0 Contributors: Own work Original artist: Frank Vincentz"
//+ "		%28reconstruction%29.jpg License: CC BY-SA 2.0 Contributors: Chieftains house (reconstruction) Original artist: Juanjo Marin"
//+ "		tors: ? Original artist: ?"
//+ "		B0ir_-_Wikingerschmuck.jpg License: CC BY-SA 3.0 Contributors: Own work Original artist: Wolfgang Sauber"
//+ "		utors: “The Public Schools Historical Atlas” by Charles Colbeck. Longmans, Green; New York; London; Bombay. 1905. Original artist:"
//+ "		Exhibition_in_Viking_Ship_Museum%2C_Oslo_01.jpg License: CC BY 3.0 Contributors: Own work Original artist: Grzegorz"
//+ "		Faroe_stamps_515-517_everyday_life_in_the_viking_age.jpg License: Public domain Contributors: ? Original artist: ?"
//+ "		Contributors: http://www.tnpa.by/ViewFileText.php?UrlRid=52178&UrlOnd=%D1%D2%C1%20911-2008 Original artist: Zscout370"
//+ "		main Contributors: Own work Original artist: User:Madden"
//+ "		Contributors: http://www.riigikantselei.ee/?id=73847 Originalartist: Originallydrawnby User:SKopp . Bluecolourchangedby User:PeepP"
//+ "		Contributors: http://www.finlex.fi/fi/laki/ajantasa/1978/19780380 Original artist: Drawn by User:SKopp"
//+ "		Original artist: ?"
//+ "		Contributors: Islandic National Flag Original artist: Ævar Arnfjörð Bjarmason , Zscout370 and others"
//+ "		artist: ?"
//+ "		Contributors: Own work Original artist: SKopp"
//+ "		Contributors: Own work Original artist: Dbenbenn"
//+ "		nal artist: ?"
//+ "		Original artist: ?"
//+ "		Contributors: ДСТУ 4512:2006 — Державний прапор України. Загальні технічні умови Original artist: Government of Ukraine"
//+ "		cense: PD Contributors: ? Original artist: ?"
//+ "		BY-SA 3.0 Contributors: All flags are based on 1651 provincial arms Original artist: Caomhan27"
//+ "		Contributors: ? Original artist: ?"
//+ "		of_tumuli.jpg License: Public domain Contributors: Own work Original artist: Szilas"
//+ "		JellingeStoneLargeInscription.JPG License: CC BY-SA 3.0 Contributors: Own work Original artist: Boeing720"
//+ "		Winkinger_Kampftraining.webm License: CC BY-SA 3.0 Contributors: Own work Original artist: HelgeRieder"
//+ "		jpg License: Public domain Contributors: http://handrit.is/en/manuscript/view/is/LbsFragm-0082 Original artist: National and University"
//+ "		A4nemark%29.jpg License: Public domain Contributors: my cam Original artist: Bunnyfrosch"
//+ ""
//+ ""
//+ "	**************************** Page 26****************************"
//+ "		  26 13 TEXT AND IMAGE SOURCES, CONTRIBUTORS, AND LICENSES "
//+ "		  • File:Modell_Knorr.jpg Source: https://upload.wikimedia.org/wikipedia/commons/7/78/Modell_Knorr.jpg License: CC-BY-SA-3.0 "
//+ "		  • File:Oseberg_ship_head_post.jpg Source: https://upload.wikimedia.org/wikipedia/commons/6/6c/Oseberg_ship_head_post.jpg Li- "
//+ "		  History, University of Oslo, Norway • File:Reconstructed_Viking_House.jpg Source: https://upload.wikimedia.org/wikipedia/commons/0/03/Reconstructed_Viking_House. "
//+ "		  • File:Scandinavia_M2002074_lrg.jpg Source: https://upload.wikimedia.org/wikipedia/commons/f/f1/Scandinavia_M2002074_lrg.jpg http://earthobservatory.nasa.gov/NaturalHazards/natural_hazards_v2.php3?img_id=2611 Original artist: Image courtesy Jacques De- "
//+ "		  scloitres, MODIS Land Rapid Response Team at NASA GSFC • File:U_240,_Lingsberg.JPG Source: https://upload.wikimedia.org/wikipedia/commons/9/96/U_240%2C_Lingsberg.JPG License: CC "
//+ "		  • File:VIKING_LONGSHIP_\\char\"0022\\relax{}SEA_STALLION\\char\"0022\\relax{}_ARRIVES_IN_DUBLIN.jpg Source: https: cense: CC BY-SA 2.0 Contributors: originally posted to Flickr as VIKING LONGSHIP “SEA STALLION” ARRIVES IN DUBLIN "
//+ "		  Original artist: William Murphy • File:Viking_Festival,_Delamont_County_Park,_June_2012_(17).JPG Source: https://upload.wikimedia.org/wikipedia/commons/9/ "
//+ "		  Original artist: Ardfern • File:Viking_swords.jpg Source: https://upload.wikimedia.org/wikipedia/commons/d/de/Viking_swords.jpg License: CC BY 2.5 Con- "
//+ "		  • File:Vikings_fight.JPG Source: https://upload.wikimedia.org/wikipedia/commons/4/40/Vikings_fight.JPG License: CC-BY-SA-3.0 "
//+ "		  • File:Wikinger.jpg Source: https://upload.wikimedia.org/wikipedia/commons/c/cb/Wikinger.jpg License: Public domain Contributors: ? "
//+ "		  • File:WikingerKarte.jpg Source: https://upload.wikimedia.org/wikipedia/commons/3/3a/WikingerKarte.jpg License: CC-BY-SA-3.0 "
//+ "		  Wikipedia • File:Wikisource-logo.svg Source: https://upload.wikimedia.org/wikipedia/commons/4/4c/Wikisource-logo.svg License: CC BY-SA 3.0 "
//+ "		  • File:Wikivoyage-Logo-v3-icon.svg Source: https://upload.wikimedia.org/wikipedia/commons/d/dd/Wikivoyage-Logo-v3-icon.svg Li- "
//+ "		  • File:Wiktionary-logo-en.svg Source: https://upload.wikimedia.org/wikipedia/commons/f/f8/Wiktionary-logo-en.svg License: Public based on original logo tossed together by Brion Vibber "
//+ "		  13.3 Content license • Creative Commons Attribution-Share Alike 3.0"
//+ "		  Contributors: ? Original artist: ?"
//+ "		cense: CC BY-SA 3.0 nl Contributors: Museum of Cultural History, University of Oslo, Norway Original artist: Museum of Cultural"
//+ "		jpg License: CC BY-SA 4.0 Contributors: Own work Original artist: Anjali Kiggal"
//+ "		License: Public domain Contributors: Moderate-resolution Imaging Spectroradiometer (MODIS), flying aboard NASA’s Terra satellite."
//+ "		BY 2.5 Contributors: Own work Original artist: Berig"
//+ "		//upload.wikimedia.org/wikipedia/commons/6/68/VIKING_LONGSHIP_%22SEA_STALLION%22_ARRIVES_IN_DUBLIN.jpg Li-"
//+ "		95/Viking_Festival%2C_Delamont_County_Park%2C_June_2012_%2817%29.JPG License: CC BY-SA 3.0 Contributors: Own work"
//+ "		tributors: Picture taken by viciarg at the Vikingermuseum in Haithabu , Germany Original artist: Own work"
//+ "		Contributors: ? Original artist: ?"
//+ "		Original artist: ?"
//+ "		Contributors: Transferred from de.wikipedia to Commons by Gikü . Original artist: The original uploader was Captain Blood at German"
//+ "		Contributors: Rei-artur Original artist: Nicholas Moreau"
//+ "		cense: CC BY-SA 3.0 Contributors: Own work Original artist: AleXXw"
//+ "		domain Contributors: Vector version of Image:Wiktionary-logo-en.png . Original artist: Vectorized by Fvasconcellos ( talk · contribs ),"
//+ "";
//
//		Unirest.setTimeouts(10000, 10000000);
//		HttpResponse<String> response = request("collection1/addDocument")
//				.queryString("documentName", "artcom"+(new Date()).getTime())
//				.queryString("documentDescription", "")
//				.queryString("user", "jmschnei@gmail.com")
////				.queryString("format", "text")
//				.queryString("informat", "text")
////				.queryString("input", input)
////				.queryString("path", "")
//				.queryString("analysis", "ner_PER_ORG_LOC_en_all,temp_en")
//				.body(input2)
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		Assert.assertTrue(response.getBody().contains("successfully"));
//
//	}
	
	
	@Test
	public void testX_1_CreateDocument() throws UnirestException, IOException,Exception {
		String input = "Noch sind Busse mit Batterie ein seltener Anblick auf deutschen Straßen. Um ehrgeizige Umweltziele erreichen zu können, soll ihre Zahl steigen. Doch geht das ohne entsprechende Fördergelder? Stuttgart (dpa) - Der Motor springt an, aber das grimmige Knurren eines klassischen Dieselantriebs bleibt aus. Fast lautlos rollt der Bus los. Kein Aufheulen beim Beschleunigen, keine rüttelnden Sitze. Hybridbusse fahren mit der Unterstützung von Elektromotoren sanft, aber kraftvoll an - und ersparen nicht nur den Fahrgästen, sondern auch geplagten Anwohnern Lärm und Emissionen. Vor allem sollen alternative Antriebe im Busverkehr den Kraftstoffverbrauch und Schadstoffausstoß drücken. Der Umwelt zuliebe fördert sie auch die Politik. Wirtschaftlich lohnt sich das bisher allerdings kaum, sagen Unternehmer. Die Zahl ist entsprechend gering: Knapp 450 Busse mit Hybrid-, Elektro- oder Brennstoffzellen-Antrieb fuhren zum 1. Januar 2015 durch Deutschland - laut Kraftfahrtbundesamt unter mehr als 77 000 Bussen insgesamt. Doch der Trend geht zumindest nach oben. Dieselhybride machen den größten Anteil aus, er stieg im Vergleich zum Vorjahr um fast 20 Prozent auf rund 290. Größere Flotten gibt es etwa in Hamburg, Dresden, Hannover, dem Ruhrgebiet und der Region Stuttgart. Der Bund hat dafür Geld in Forschungsprojekte gesteckt, daneben bezuschusst er Dieselhybride. Teils kommt auch von den Ländern Unterstützung. Drei Millionen Euro haben Unternehmen zum Beispiel in Baden-Württemberg seit 2012 bekommen. Dem Verband Deutscher Verkehrsunternehmen zufolge fördern sieben Länder generell den Kauf solcher Busse. Aber die neuen Antriebe kosten gleichzeitig bis zu zwei- oder dreimal so viel wie Dieselmodelle, je nach Typ und Hersteller. Die im Südwesten geförderten Hybridbusse waren zwischen 79 000 und 235 000 Euro teurer als herkömmliche. Das private Unternehmen LVL in Ludwigsburg hat trotzdem gleich zehn Hybridbusse gekauft, die seit Anfang des Jahres unterwegs sind. «Wir wollen die Weiterentwicklung der Technik nicht verschlafen», sagt Betriebsleiter Frank Metzger. Um einzuschätzen, ob die sich rechnet, sei es aber noch zu früh. Das Land hat alle Busse bezuschusst. «Sonst wäre es natürlich wirtschaftlich nicht machbar gewesen.» Die Politik dringt auf saubere Luft. Ab 2020 sollen etwa in Hamburg nur noch emissionsfreie Busse angeschafft werden, wie ein Sprecher der Hamburger Hochbahn sagt. Rund 60 Busse mit alternativen Antrieben fahren dort bereits, die meisten davon Dieselhybride, aber auch solche mit Brennstoffzellen. Finanziell lohnt sich das nicht - trotzdem macht die Hochbahn mit: «Größere Unternehmen müssen da eine Schrittmacherfunktion einnehmen.» In Stuttgart sieht man das ähnlich. Dort sind derzeit 16 Brennstoffzellen- und Hybridbusse im Einsatz. Private Mittelständler, die vor allem die Buslinien auf dem Land bedienen, tun sich dagegen noch schwer. Zwar federten Subventionen die Aufschläge etwas ab, im Preiswettbewerb der Linienvergabe seien die Busse trotzdem viel zu teuer, heißt es vom Bundesverband Deutscher Omnibusunternehmer. Auch die Kommunen zeigten in ihren Ausschreibungen kaum Interesse - denn die Anschaffung alternativer Antriebe würde die Preise für die Fahrgäste in die Höhe treiben. Es gibt weitere Vorbehalte: Herstellern wie Daimler, MAN, Volvo oder Solaris aus Polen zufolge könnten einige Hybridmodelle zwar bis zu 30 Prozent Kraftstoff und noch mehr Emissionen sparen. Im Alltagsbetrieb kommen sie an so hohe Zahlen aber nicht heran, wie Unternehmer sagen. Die Busse lohnten sich außerdem nur in der Stadt, wo das ständige Bremsen die Batterie wieder auflädt. Auch Tankstellen für Elektro- und Brennstoffzellen-Busse seien bisher kaum vorhanden und teuer, berichten Unternehmer. So seien Hybridantriebe dank ihres Dieselmotors noch praktikabler - auch wenn Hersteller wie Daimler davon ausgehen, dass sie nur Zwischenlösungen sein werden. Deswegen wird an neuen Projekten geforscht. In Mannheim tanken zwei Induktions-Busse seit Ende Juni Strom aus Metallplatten unter sechs Haltestellen - eine ähnliche Technik nutzen elektrische Zahnbürsten. Aufgeladen kommen die Busse rund 20 Kilometer weit, wie René Weintz vom Verkehrsbetrieb RNV sagt. Ob sich dieses System durchsetzen kann, weiß er nicht. «Es ist aber eine vielversprechende Technik.» Ohne Geld vom Staat wäre es jedoch wieder nicht möglich gewesen: 3,3 Millionen Euro hat das Bundesverkehrsministerium hier investiert. # Notizblock ## Redaktionelle Hinweise - Hintergrund «Neuartige Antriebe bei Omnibussen» bis 0600 - ca. 20 Zl ## Internet - [RNV Primove](http://dpaq.de/t9u8Q) - [Förderbedingungen Bundesumweltministerium](http://dpaq.de/lErR3) - [Kraftfahrtbundesamt-Zahlen](http://dpaq.de/cV1ZF) ## Orte - [Bundesverband Deutscher Omnibusunternehmer](Reinhardtstraße 25, 10117 Berlin, Deutschland) * * * * Die folgenden Informationen sind nicht zur Veröffentlichung bestimmt ## Ansprechpartner - Lars Wagner, Verband Deutscher Verkehrsunternehmen, +49 30 39993214, - Matthias Schröter, Bundesverband Deutscher Omnibusunternehmer, +49 30 24089300 - Christoph Kreienbaum, Hamburger Hochbahn, +49 40 32882121, - Uta Leitner, Daimler, +";


		Unirest.setTimeouts(10000, 10000000);
		HttpResponse<String> response = request("CondatTest/addDocument")
				.queryString("documentName", "condat"+(new Date()).getTime())
				.queryString("documentDescription", "")
				.queryString("user", "jmschnei@gmail.com")
				//			.queryString("format", "text")
				.queryString("informat", "text")
				//			.queryString("input", input)
				//			.queryString("path", "")
				.queryString("analysis", "ner_PER_ORG_LOC_en_all,temp_en")
				.body(input)
				.asString();
		assertTrue(response.getStatus() == 200);
		Assert.assertTrue(response.getBody().contains("successfully"));
	}
}
