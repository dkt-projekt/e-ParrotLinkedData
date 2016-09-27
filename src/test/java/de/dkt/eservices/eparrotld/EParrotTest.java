package de.dkt.eservices.eparrotld;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONObject;
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
////	
////	@Test
////	public void test2_1_listUsers() throws UnirestException, IOException,
////			Exception {
////				HttpResponse<String> response = request("listUsers/")
////						.asString();
////		assertTrue(response.getStatus() == 200);
////		assertTrue(response.getBody().length() > 0);
////		String expectedOutput = "{\"users\":{\"user1\":{\"password\":\"*3A9588B01510694D53D532A8B2885A00D89F0F5D\",\"name\":\"julian\",\"id\":1,\"email\":\"jmschnei@gmail.com\"},\"user2\":{\"password\":\"*C87302C99F650EFB1654068B217EFE5E69DA6271\",\"name\":\"julian 3\",\"id\":2,\"email\":\"jmschnei3\"}}}";
////		Assert.assertEquals(expectedOutput, response.getBody());
////	}
//
//	@Test
//	public void test3_1_CreateUser_1() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = request("createUser/")
//				.queryString("newUser", "jmschnei2")
//				.queryString("newPassword", "jmschnei2")
//				.queryString("newUserName", "julian 2")
//				.queryString("newUserRole", "normal")
//				.queryString("user", "jmschnei")
//				.queryString("password", "password")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		assertTrue(response.getBody().length() > 0);
//		Assert.assertEquals("The user jmschnei2 has NOT been created. The process has failed!!!!", response.getBody());
//	}
//	
////	@Test
////	public void test3_2_CreateUser_2() throws UnirestException, IOException,Exception {
////		HttpResponse<String> response2 = request("createUser/")
////				.queryString("newUser", "jmschnei3")
////				.queryString("newPassword", "jmschnei3")
////				.queryString("newUserName", "julian 3")
////				.queryString("newUserRole", "normal")
////				.queryString("user", "jmschnei@gmail.com")
////				.queryString("password", "passwordJuli")
////				.asString();
////		assertTrue(response2.getStatus() == 200);
////		assertTrue(response2.getBody().length() > 0);
////		Assert.assertTrue(response2.getBody().contains("successfully"));
////	}
////
//	
//	@Test
//	public void test3_3_CheckUser() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response2 = request("checkUser/")
//				.queryString("user", "jmschnei@gmail.com")
//				.queryString("password", "passwordJuli")
//				.asString();
//		assertTrue(response2.getStatus() == 200);
//		assertTrue(response2.getBody().length() > 0);
//		System.out.println("DEBUG: "+response2.getBody());
//		Assert.assertTrue(response2.getBody().contains("true"));
//
//		response2 = request("checkUser/")
//				.queryString("user", "jmschnei2@gmail.com")
//				.queryString("password", "passwordJuli")
//				.asString();
//		assertTrue(response2.getStatus() == 200);
//		assertTrue(response2.getBody().length() > 0);
//		Assert.assertTrue(response2.getBody().contains("false"));
//	}
//	
//	@Test
//	public void test3_4_ExistsUser() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response2 = request("existsUser/")
//				.queryString("user", "jmschnei@gmail.com")
//				.asString();
//		assertTrue(response2.getStatus() == 200);
//		Assert.assertTrue(response2.getBody().contains("true"));
//
//		response2 = request("existsUser/")
//				.queryString("user", "jmschnei3")
//				.asString();
//		assertTrue(response2.getStatus() == 200);
//		Assert.assertTrue(response2.getBody().contains("true"));
//
//		response2 = request("existsUser/")
//				.queryString("user", "jmschnei2@gmail.com")
//				.asString();
//		assertTrue(response2.getStatus() == 200);
//		Assert.assertTrue(response2.getBody().contains("false"));
//	}
//	
////	@Test
////	public void test4_1_CreateCollection() throws UnirestException, IOException,Exception {
////		HttpResponse<String> response = request("createCollection/")
////				.queryString("collectionName", "collection1")
////				.queryString("description", "This is the description of the test collection 1")
////				.queryString("user", "jmschnei@gmail.com")
////				.queryString("private", false)
////				.queryString("analysis", "ner")
////				.queryString("users", "")
////				.asString();
////		assertTrue(response.getStatus() == 200);
////		assertTrue(response.getBody().length() > 0);
////		Assert.assertTrue(response.getBody().contains("successfully"));
////
////	}
//
	@Test
	public void test4_2_listCollections() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = request("listCollections/")
				.queryString("informat", "text")
				.asString();
		assertTrue(response.getStatus() == 200);
		
		JSONObject j = new JSONObject(response.getBody());
		JSONObject cols = j.getJSONObject("collections");
		Iterator it = cols.keys();
		int cnt = 0;
		while(it.hasNext()){
			Object o = it.next();
			cnt++;
		}
		System.out.println(cnt);
//		System.exit(0);
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
//	
//	@Test
//	public void test4_3_collectionOverview() throws UnirestException, IOException,Exception {
//				HttpResponse<String> response = request("collection1/overview").asString();
//		assertTrue(response.getStatus() == 200);
//		
//		Assert.assertEquals("{\"entitylinking\":\"\",\"private\":false,\"documents\":\"<div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Plaintext<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div><div class=\\\"row\\\"><div class=\\\"col-lg-12\\\"><div class=\\\"panel panel-default\\\"><div class=\\\"panel-heading\\\"><i class=\\\"fa fa-bar-chart-o fa-fw\\\"><\\/i> document1Turtle<\\/div><div class=\\\"panel-body\\\"><div class=\\\"col-lg-12 col-md-6\\\"><div class=\\\"row\\\"><blockquote><p>1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n<\\/p><\\/blockquote><\\/div><div class=\\\"row\\\"><span class=\\\"label label-default\\\">Other<\\/span><span class=\\\"label label-primary\\\">Temporal Expressions<\\/span><span class=\\\"label label-success\\\">Person<\\/span><span class=\\\"label label-info\\\">Location<\\/span><span class=\\\"label label-warning\\\">Organization<\\/span><\\/div><\\/div> <\\/div> <\\/div> <\\/div><\\/div>\",\"collectionDescription\":\"This is the description of the test collection 1\",\"timelining\":\"\",\"clustering\":\"\",\"geolocalization\":\"\",\"analysis\":\"ner\",\"collectionId\":1,\"userId\":1,\"users\":\"\",\"collectionName\":\"collection1\"}", response.getBody());
//		
//	}
//	
//	@Test
//	public void test5_1_DeleteDocument() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = request("collection1/deleteDocument")
//				.queryString("documentName", "document1Plaintext")
//				.queryString("user", "jmschnei@gmail.com")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		System.out.println("BODY: "+ response.getBody());
////		Assert.assertTrue(response.getBody().contains("successfully"));
//
//		response = request("collection1/deleteDocument")
//				.queryString("documentName", "document1Turtle")
//				.queryString("user", "jmschnei@gmail.com")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		System.out.println("BODY: "+ response.getBody());
////		Assert.assertTrue(response.getBody().contains("successfully"));
//
//	}
//	
//	@Test
//	public void test5_2_CreateDocument() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = request("collection1/addDocument")
//				.queryString("documentName", "document1Plaintext")
//				.queryString("documentDescription", "This is the description of the document 1 (Plaintext) of collection 1")
//				.queryString("user", "jmschnei@gmail.com")
//				.queryString("format", "")
//				.queryString("informat", "text")
//				.queryString("input", TestConstants.doc1PlaintextContent)
////				.queryString("path", "")
//				.queryString("analysis", "ner_LOC_en")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		Assert.assertTrue(response.getBody().contains("successfully"));
//
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
//
//		response = request("collection1/addDocument")
//				.queryString("documentName", "document1Turtle")
//				.queryString("documentDescription", "This is the description of the document 1 (Turtle) of collection 1")
//				.queryString("user", "jmschnei")
//				.queryString("format", "")
//				.queryString("informat", "turtle")
//				.queryString("input", TestConstants.doc1TurtleContent)
////				.queryString("path", "")
//				.queryString("analysis", "ner_LOC_en")
//				.asString();
//		assertTrue(response.getStatus() == 502);
//		System.out.println("BODY: "+response.getBody());
//		Assert.assertTrue(response.getBody().contains("has not rights for accessing the collection"));
//	}
//	
//	@Test
//	public void test5_2_2_CreateDocument() throws UnirestException, IOException,Exception {
//		String input = "" + 
//				"1937\n" + 
//				" With his ranks swelled by Italian troops and Spanish colonial soldiers from Morocco, Franco made another attempt to capture Madrid in January and February 1937, but again failed." + 
//				" On 21 February the League of Nations Non-Intervention Committee ban on foreign national volunteers went into effect. The large city of Málaga was taken on 8 February. On 7 March, the German Condor Legion" +  
//				" equipped with Heinkel He 51 biplanes arrived in Spain; on 26 April the Legion Bombed the town of Guernica, killing hundreds.";
//		String input2 = 
//"	@prefix dktnif: <http://dkt.dfki.de/ontologies/nif#> ."
//+ "		@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ."
//+ "		@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> ."
//+ "		@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> ."
//+ "		@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> ."
//+ "		@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> ."
//+ ""
//+ "		<http://dkt.dfki.de/documents/#char=0,2507>"
//+ "		        a               nif:RFC5147String , nif:String , nif:Context ;"
//+ "		        nif:beginIndex  \"0\"^^xsd:nonNegativeInteger ;"
//+ "		        nif:endIndex    \"2507\"^^xsd:nonNegativeInteger ;"
//+ "		        nif:isString    \"\\n[Datumsstempel: MAR 16 1942]\\n \\nMrs. Luise Mendelsohn\\nThe Alden\\n225 Central Park West\\nNew York City\\n[Absender]\\nThe Drake\\nLake Shore Drive\\nChicago\\n[Anmerkung: 1b, 21/III 42.]\\n\\nThe Drake\\nChicago\\nIII.21.42.\\n\\nThe flowers came and your\\nletter, and I feel not totally\\ncut off from where I came and\\nshall go back to. They deserve a\\nspecial reply tomorrow. -\\nAfter a very lively luncheon\\nwith Prof. Lorch and family at\\nAnn Arbor I streamlined to\\nChicago into the Drake fossil. The\\ntechnical details I admired 18\\nyears ago have become either\\nloose or dusty and the seclusive\\nrefinement has made place a\\n\\nhumdrum of incentives for\\nlady-battleships or pre-war jeunesse-\\nchromée. \\nI had oyster dinner with Mies\\nand later at his flat much serious\\ntalk and laughter. Hilbersheimer\\ncame joining us.\\nMy lecture at Mies&apos;s department,\\nfor students only, was tremendously\\ncheered and Mies regretted not having\\ninvited the architects and the public.\\nDinner at a Greek Restaurant, invited\\nby Hilbersh. \\nToday, I went to the Arts Club, where\\nin the \\\"Wrigley\\\" Bldg. my work refuses\\nto be chewed, a posthumous sight for\\nme and an unborn for Chicago.\\nMet a French (Baronesse of course)\\ninfected by Jenny de Margerie with\\nMendelsohnitis,\\n\\nand the Lady President of the Club\\nlooking like a cluster of overpainted\\ntoo late autumn apples.\\nBadly hung and no critics. Sealed\\nlips because of unopened brass-heads. \\nThen, the Sulzbergers came. Too rich as\\nnot to be conceated. They don&apos;t think\\nthat modern Architecture and World Wars\\nhave any connection or George III any\\nwith war reverses!\\nI think to cancel their invitation for\\nsupper tomorrow night - 7 miles bus-\\nride for, certainly, cold meat.\\nIn an hour&apos;s time, Cocktail Party\\nat Mies - apparently for Bach, no\\nMass or remembrance visible.\\nAfter that my counter invitation for\\ndinner - Mies and friend. Did&apos;nt see\\nher yet:\\n\\nMonday night - Marc gentlemen\\nDinner.\\nTuesday night - Dinner &amp; Lecture\\nof the Institute within my exhibition.\\nAn embar[r]assing background.\\nWednesday to Ann Arbor - 2nd lecture\\non Thursday.\\nThursday night, may be Dinner with\\nAlbert Kahn&apos;s in Detroit and Friday\\nmorning to New York. There is, of course,\\na day train and Plant&apos;s office has\\njust wasted money. Nevertheless,\\nyour spring costume is certain.\\nWeather in Chicago changes from\\nlate spring to deep winter. I am\\ntranspiring and caughing.\\nAn impossible climate, a depressing\\nvista to my sensitive eyes - a world\\nuntouched by a 30 years fight! Mr. benedicted\\nby\\nBenedictus\"^^xsd:string ;"
//+ "		        nif:language    \"en\"^^xsd:string .";
//
////		HttpResponse<String> response = request("collection20160707171608/addDocument")
//				HttpResponse<String> response = Unirest.post("http://dev.digitale-kuratierung.de/api/e-parrot/collection20160707171608/addDocument")
//				.queryString("documentName", "8"+(new Date()).getTime())
//				.queryString("documentDescription", "")
//				.queryString("user", "hh@hh.hh")
//				.queryString("format", "")
//				.queryString("informat", "turtle")
//				.queryString("input", input2)
////				.queryString("path", "")
//				.queryString("analysis", "ner_PER_ORG_LOC_en_all,temp_en")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		Assert.assertTrue(response.getBody().contains("successfully"));
//	}
//	
//	
//	 
//	@Test
//	public void test5_3_ListDocuments() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = request("collection1/listDocuments")
//				.queryString("user", "jmschnei@gmail.com")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		
//		String expectedDocuments = "{\"documents\":{\"document2\":{\"documentDescription\":\"This is the description of the document 1 (Turtle) of collection 1\",\"highlightedContent\":\"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\",\"documentId\":17,\"documentName\":\"document1Turtle\",\"documentContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:String , nif:RFC5147String , nif:Context ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=636,649>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"636\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"649\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=146,151>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"146\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"151\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:Context , nif:RFC5147String , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\\n<http://dkt.dfki.de/examples/#char=711,717>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"711\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"717\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=277,282>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"277\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"282\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=254,260>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"254\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"260\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\"},\"document1\":{\"documentDescription\":\"This is the description of the document 1 (Plaintext) of collection 1\",\"highlightedContent\":\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\",\"documentId\":16,\"documentName\":\"document1Plaintext\",\"documentContent\":\"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://freme-project.eu/#char=0,816>\\n        a               nif:RFC5147String , nif:Context , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"816\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\\\\\n\\\\\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\\\\\n\\\" .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://freme-project.eu/#char=713,719>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"713\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"719\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=638,651>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"638\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"651\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=279,284>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"279\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"284\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=256,262>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"256\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"262\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=0,816>\\n        a               nif:String , nif:Context , nif:RFC5147String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"816\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\\\\\n\\\\\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\\\\\n\\\" .\\n\\n<http://freme-project.eu/#char=148,153>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"148\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"153\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\"}}}";
//		Assert.assertEquals(expectedDocuments,response.getBody());
//
////		response = request("collection1/addDocument")
////				.queryString("documentName", "document1Turtle")
////				.queryString("documentDescription", "This is the description of the document 1 (Turtle) of collection 1")
////				.queryString("user", "jmschnei@gmail.com")
////				.queryString("format", "")
////				.queryString("informat", "turtle")
////				.queryString("input", TestConstants.doc1TurtleContent)
//////				.queryString("path", "")
////				.queryString("analysis", "ner_LOC_en")
////				.asString();
////		assertTrue(response.getStatus() == 200);
////		Assert.assertTrue(response.getBody().contains("successfully"));
//
//	}
//
//	@Test
//	public void test5_9_ListDocuments() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = request("CondatTest/listDocuments")
//				.queryString("user", "jmschnei@gmail.com")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		
//		String expectedDocuments = "{\"documents\":{\"document2\":{\"documentDescription\":\"This is the description of the document 1 (Turtle) of collection 1\",\"highlightedContent\":\"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\",\"documentId\":17,\"documentName\":\"document1Turtle\",\"documentContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:String , nif:RFC5147String , nif:Context ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=636,649>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"636\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"649\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=146,151>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"146\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"151\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:Context , nif:RFC5147String , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\\n<http://dkt.dfki.de/examples/#char=711,717>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"711\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"717\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=277,282>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"277\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"282\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=254,260>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"254\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"260\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\"},\"document1\":{\"documentDescription\":\"This is the description of the document 1 (Plaintext) of collection 1\",\"highlightedContent\":\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\",\"documentId\":16,\"documentName\":\"document1Plaintext\",\"documentContent\":\"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://freme-project.eu/#char=0,816>\\n        a               nif:RFC5147String , nif:Context , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"816\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\\\\\n\\\\\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\\\\\n\\\" .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://freme-project.eu/#char=713,719>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"713\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"719\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=638,651>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"638\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"651\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=279,284>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"279\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"284\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=256,262>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"256\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"262\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\\n<http://freme-project.eu/#char=0,816>\\n        a               nif:String , nif:Context , nif:RFC5147String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"816\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\\\\\n\\\\\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\\\\\n\\\" .\\n\\n<http://freme-project.eu/#char=148,153>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"148\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"153\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://freme-project.eu/#char=0,816> ;\\n        <http://www.w3.org/2005/11/its/rdf#taClassRef>\\n                <http://dbpedia.org/ontology/Location> .\\n\"}}}";
//		Assert.assertEquals(expectedDocuments,response.getBody());
//	}
//
//	@Test
//	public void test6_1_documentOverview() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = request("collection1/document1Turtle/overview").asString();
//		assertTrue(response.getStatus() == 502);
//		Assert.assertTrue(response.getBody().contains("The user has not permission to read this document"));
//	}
//	
//	@Test
//	public void test6_2_documentOverview() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = request("collection1/document1Turtle/overview")
//				.queryString("user", "jmschnei@gmail.com")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		
//		String expectedDocuments = "{\"documentDescription\":\"This is the description of the document 1 (Turtle) of collection 1\",\"highlightedContent\":\"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the <span class=\\\"label label-warning\\\">South<\\/span>. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at <span class=\\\"label label-warning\\\">Ferrol<\\/span> in northwestern <span class=\\\"label label-warning\\\">Spain<\\/span>. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered <span class=\\\"label label-warning\\\">San Sebastián<\\/span> to the Nationalists, who then advanced toward their capital, <span class=\\\"label label-warning\\\">Bilbao<\\/span>. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\",\"documentId\":17,\"documentName\":\"document1Turtle\",\"documentContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:String , nif:RFC5147String , nif:Context ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\",\"collectionId\":1,\"annotatedContent\":\"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\\n@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\\n@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\\n@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\\n\\n<http://dkt.dfki.de/examples/#char=636,649>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"San Sebastián\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"636\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"649\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=146,151>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"South\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"146\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"151\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=0,813>\\n        a               nif:Context , nif:RFC5147String , nif:String ;\\n        nif:beginIndex  \\\"0\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex    \\\"813\\\"^^xsd:nonNegativeInteger ;\\n        nif:isString    \\\"1936\\\\n\\\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipúzcoa from July to September. The capture of Guipúzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irún, closing the French border to the Republicans. On 13 September, the Basques surrendered San Sebastián to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\\\n\\\"^^xsd:string .\\n\\n<http://dkt.dfki.de/examples/#char=711,717>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Bilbao\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"711\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"717\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=277,282>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Spain\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"277\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"282\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\\n<http://dkt.dfki.de/examples/#char=254,260>\\n        a                     nif:RFC5147String , nif:String ;\\n        nif:anchorOf          \\\"Ferrol\\\"^^xsd:string ;\\n        nif:beginIndex        \\\"254\\\"^^xsd:nonNegativeInteger ;\\n        nif:endIndex          \\\"260\\\"^^xsd:nonNegativeInteger ;\\n        nif:referenceContext  <http://dkt.dfki.de/examples/#char=0,813> ;\\n        itsrdf:taClassRef     <http://dbpedia.org/ontology/Location> .\\n\"}";
//		
//		Assert.assertEquals(expectedDocuments,response.getBody());
//	}
//	
//	
//
//	
////	@Test
////	public void testETimeliningProcessDocument() throws UnirestException, IOException,Exception {
////		String indexPath = "";
////		String sesamePath = "";
////		String OS = System.getProperty("os.name");
////		
////		if(OS.startsWith("Mac")){
////			indexPath = "/Users/jumo04/Documents/DFKI/DKT/dkt-test/tests/luceneindexes/";
////			sesamePath = "/Users/jumo04/Documents/DFKI/DKT/dkt-test/tests/sesamestorages/";
////		}
////		else if(OS.startsWith("Windows")){
////			indexPath = "C://tests/luceneindexes/";
////			sesamePath = "C://tests/sesamestorage/";
////		}
////		else if(OS.startsWith("Linux")){
////			indexPath = "/tmp/storage/documents/index1/";
////			sesamePath = "/tmp/storage/sesame/storage1/";
////		}
////		
////		HttpResponse<String> response = documentRequest()
////				.queryString("informat", "text/turtle")
////				.queryString("input", inputText)
////				.queryString("outformat", "turtle")
////				.queryString("language", "en")
////				.queryString("openNLPAnalysis", "ner")
////				.queryString("openNLPModels", "ner-wikinerEn_LOC;ner-wikinerEn_ORG;ner-wikinerEn_PER;englishDates")
////				.queryString("sesameStorageName", "test2")
////				.queryString("sesameStoragePath", sesamePath)
////				.queryString("sesameCreate", true)
////				.queryString("luceneIndexName", "test2")
////				.queryString("luceneIndexPath", indexPath)
////				.queryString("luceneFields", "content")
////				.queryString("luceneAnalyzers", "standard")
////				.queryString("luceneCreate", true)
////				.asString();
////		assertTrue(response.getStatus() == 200);
////		assertTrue(response.getBody().length() > 0);
////		Assert.assertEquals(expectedOutput, response.getBody());
////		
////
////		
////	}
//	
//	
//	@Test
//	public void test8_4_getCollectionClustering() throws UnirestException, IOException,Exception {
//		//HttpResponse<String> response = Unirest.post("http://dev.digitale-kuratierung.de:8092/e-parrot/CondatTest6/clustering")
//		HttpResponse<String> response = request("MiddleEastNewsCollection/clustering")
//				.queryString("user", "dkt-project@dfki.de")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		System.out.println("BBBBOOOODDDDDYYYY: "+response.getBody());
////		String expectedDocuments = "{\"models\":{\"model4\":{\"modelName\":\"temp_en\",\"models\":\"englishDates\",\"informat\":\"turtle\",\"modelId\":4,\"language\":\"en\",\"modelType\":\"timex\",\"analysis\":\"temp\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model3\":{\"mode\":\"all\",\"modelName\":\"ner_PER_ORG_LOC_en_all\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":3,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model2\":{\"mode\":\"link\",\"modelName\":\"ner_PER_ORG_LOC_en_link\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":2,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model1\":{\"mode\":\"spot\",\"modelName\":\"ner_PER_ORG_LOC_en_spot\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":1,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"}}}";
////		Assert.assertEquals(expectedDocuments,response.getBody());
//		String expectedDocuments = "";
//		Assert.assertEquals(expectedDocuments, response.getBody());
//		
//	}
//
	@Test
	public void test8_5_getCollectionTimelining() throws UnirestException, IOException,Exception {
		//HttpResponse<String> response = Unirest.post("http://dev.digitale-kuratierung.de:8092/e-parrot/CondatTest6/clustering")
		HttpResponse<String> response = request("MiddleEastNewsCollection/timelining")
				.queryString("user", "dkt-project@dfki.de")
				.queryString("limit", 3)
				.asString();
		assertTrue(response.getStatus() == 200);
		System.out.println("BBBBOOOODDDDDYYYY: "+response.getBody());
//		String expectedDocuments = "{\"models\":{\"model4\":{\"modelName\":\"temp_en\",\"models\":\"englishDates\",\"informat\":\"turtle\",\"modelId\":4,\"language\":\"en\",\"modelType\":\"timex\",\"analysis\":\"temp\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model3\":{\"mode\":\"all\",\"modelName\":\"ner_PER_ORG_LOC_en_all\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":3,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model2\":{\"mode\":\"link\",\"modelName\":\"ner_PER_ORG_LOC_en_link\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":2,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model1\":{\"mode\":\"spot\",\"modelName\":\"ner_PER_ORG_LOC_en_spot\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":1,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"}}}";
//		Assert.assertEquals(expectedDocuments,response.getBody());
		String expectedDocuments = "";
		Assert.assertEquals(expectedDocuments, response.getBody());
		
	}

//	
//	
//	
//	@Test
//	public void test9_1_listModels() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = request("listmodels")
//				.asString();
//		assertTrue(response.getStatus() == 200);
////		String expectedDocuments = "{\"models\":{\"model4\":{\"modelName\":\"temp_en\",\"models\":\"englishDates\",\"informat\":\"turtle\",\"modelId\":4,\"language\":\"en\",\"modelType\":\"timex\",\"analysis\":\"temp\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model3\":{\"mode\":\"all\",\"modelName\":\"ner_PER_ORG_LOC_en_all\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":3,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model2\":{\"mode\":\"link\",\"modelName\":\"ner_PER_ORG_LOC_en_link\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":2,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model1\":{\"mode\":\"spot\",\"modelName\":\"ner_PER_ORG_LOC_en_spot\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":1,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"}}}";
////		Assert.assertEquals(expectedDocuments,response.getBody());
//		String expectedDocuments = "{\"models\":{\"model4\":{\"modelName\":\"temp_en\",\"models\":\"englishDates\",\"informat\":\"turtle\",\"modelId\":4,\"language\":\"en\",\"modelType\":\"timex\",\"analysis\":\"temp\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model3\":{\"mode\":\"all\",\"modelName\":\"ner_PER_ORG_LOC_en_all\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":3,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model2\":{\"mode\":\"link\",\"modelName\":\"ner_PER_ORG_LOC_en_link\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":2,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"},\"model1\":{\"mode\":\"spot\",\"modelName\":\"ner_PER_ORG_LOC_en_spot\",\"models\":\"ner-wikinerEn_PER;ner-wikinerEn_ORG;ner-wikinerEn_LOC\",\"informat\":\"turtle\",\"modelId\":1,\"language\":\"en\",\"modelType\":\"ner\",\"analysis\":\"ner\",\"outformat\":\"turtle\",\"url\":\"/e-nlp/namedEntityRecognition\"";
//		Assert.assertTrue(response.getBody().startsWith(expectedDocuments));
//	}
//
//	@Test
//	public void test9_2_addModel() throws UnirestException, IOException,Exception {
//		
//		String dictionaryContent = "Armentières	http://www.geonames.org/3036903\n"
//				+"Hampstead Heath	http://www.geonames.org/6618525/hampstead-heath.html\n"
//				+"Haarlem 	http://www.geonames.org/2755003\n"
//				+"England	http://www.geonames.org/6269131\n"
//				+"Lourds	http://www.geonames.org/2997395/lourdes.html\n"
//				+"Graswang	http://www.geonames.org/2917958\n"
//				+"Straßburg	http://www.geonames.org/ 2764186\n"
//				+"La Spezia	http://www.geonames.org/3175081\n"
//				+"Aland Islands 	http://www.geonames.org/661883\n"
//				+"Sonnenspitze 	http://www.geonames.org/2764900\n"
//				+"Luxor 	http://www.geonames.org/360502\n"
//				+"San Sebastian	http://www.geonames.org/3110044\n"
//				+"Rudolstadt	http://www.geonames.org/2843355\n"
//				+"Mount Scopus	http://www.geonames.org/281956\n"
//				+"Molokai	http://www.geonames.org/5851218/moloka-i.html";
//
//		String dictUploadData = 
//				"Mahler	http://d-nb.info/gnd/11857633X\n" +
//					"Herbert Eulenberg	http://d-nb.info/gnd/118682636\n" +
//					"Leonardo da Vinci	http://d-nb.info/gnd/118640445\n" +
//					"Treitschke	http://d-nb.info/gnd/118623761\n" +
//					"Schmädel 	http://d-nb.info/gnd/117521221";
//		
//		Date d = new Date();
////		HttpResponse<String> response = request("addmodels")
//		HttpResponse<String> response = Unirest.post("http://dev.digitale-kuratierung.de/api/e-parrot/addmodels")
//				.queryString("modelName", "Dictionary1")
//				.queryString("modelType", "dict")
//				.queryString("modelInformat", "turtle")
//				.queryString("modelOutformat", "turtle")
//				.queryString("url", "/e-nlp/namedEntityRecognition")
//				.queryString("analysis", "dict")
//				.queryString("language", "en")
//				.queryString("models", "")
//				.queryString("mode", "")
//				.queryString("content", dictUploadData)
//				.body("")
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		Assert.assertTrue(response.getBody().contains("successfully"));
//	}
//	

//	
//	
//	@Test
//	public void testX_1_CreateDocument() throws UnirestException, IOException,Exception {
//		String input = "Noch sind Busse mit Batterie ein seltener Anblick auf deutschen Straßen. Um ehrgeizige Umweltziele erreichen zu können, soll ihre Zahl steigen. Doch geht das ohne entsprechende Fördergelder? Stuttgart (dpa) - Der Motor springt an, aber das grimmige Knurren eines klassischen Dieselantriebs bleibt aus. Fast lautlos rollt der Bus los. Kein Aufheulen beim Beschleunigen, keine rüttelnden Sitze. Hybridbusse fahren mit der Unterstützung von Elektromotoren sanft, aber kraftvoll an - und ersparen nicht nur den Fahrgästen, sondern auch geplagten Anwohnern Lärm und Emissionen. Vor allem sollen alternative Antriebe im Busverkehr den Kraftstoffverbrauch und Schadstoffausstoß drücken. Der Umwelt zuliebe fördert sie auch die Politik. Wirtschaftlich lohnt sich das bisher allerdings kaum, sagen Unternehmer. Die Zahl ist entsprechend gering: Knapp 450 Busse mit Hybrid-, Elektro- oder Brennstoffzellen-Antrieb fuhren zum 1. Januar 2015 durch Deutschland - laut Kraftfahrtbundesamt unter mehr als 77 000 Bussen insgesamt. Doch der Trend geht zumindest nach oben. Dieselhybride machen den größten Anteil aus, er stieg im Vergleich zum Vorjahr um fast 20 Prozent auf rund 290. Größere Flotten gibt es etwa in Hamburg, Dresden, Hannover, dem Ruhrgebiet und der Region Stuttgart. Der Bund hat dafür Geld in Forschungsprojekte gesteckt, daneben bezuschusst er Dieselhybride. Teils kommt auch von den Ländern Unterstützung. Drei Millionen Euro haben Unternehmen zum Beispiel in Baden-Württemberg seit 2012 bekommen. Dem Verband Deutscher Verkehrsunternehmen zufolge fördern sieben Länder generell den Kauf solcher Busse. Aber die neuen Antriebe kosten gleichzeitig bis zu zwei- oder dreimal so viel wie Dieselmodelle, je nach Typ und Hersteller. Die im Südwesten geförderten Hybridbusse waren zwischen 79 000 und 235 000 Euro teurer als herkömmliche. Das private Unternehmen LVL in Ludwigsburg hat trotzdem gleich zehn Hybridbusse gekauft, die seit Anfang des Jahres unterwegs sind. «Wir wollen die Weiterentwicklung der Technik nicht verschlafen», sagt Betriebsleiter Frank Metzger. Um einzuschätzen, ob die sich rechnet, sei es aber noch zu früh. Das Land hat alle Busse bezuschusst. «Sonst wäre es natürlich wirtschaftlich nicht machbar gewesen.» Die Politik dringt auf saubere Luft. Ab 2020 sollen etwa in Hamburg nur noch emissionsfreie Busse angeschafft werden, wie ein Sprecher der Hamburger Hochbahn sagt. Rund 60 Busse mit alternativen Antrieben fahren dort bereits, die meisten davon Dieselhybride, aber auch solche mit Brennstoffzellen. Finanziell lohnt sich das nicht - trotzdem macht die Hochbahn mit: «Größere Unternehmen müssen da eine Schrittmacherfunktion einnehmen.» In Stuttgart sieht man das ähnlich. Dort sind derzeit 16 Brennstoffzellen- und Hybridbusse im Einsatz. Private Mittelständler, die vor allem die Buslinien auf dem Land bedienen, tun sich dagegen noch schwer. Zwar federten Subventionen die Aufschläge etwas ab, im Preiswettbewerb der Linienvergabe seien die Busse trotzdem viel zu teuer, heißt es vom Bundesverband Deutscher Omnibusunternehmer. Auch die Kommunen zeigten in ihren Ausschreibungen kaum Interesse - denn die Anschaffung alternativer Antriebe würde die Preise für die Fahrgäste in die Höhe treiben. Es gibt weitere Vorbehalte: Herstellern wie Daimler, MAN, Volvo oder Solaris aus Polen zufolge könnten einige Hybridmodelle zwar bis zu 30 Prozent Kraftstoff und noch mehr Emissionen sparen. Im Alltagsbetrieb kommen sie an so hohe Zahlen aber nicht heran, wie Unternehmer sagen. Die Busse lohnten sich außerdem nur in der Stadt, wo das ständige Bremsen die Batterie wieder auflädt. Auch Tankstellen für Elektro- und Brennstoffzellen-Busse seien bisher kaum vorhanden und teuer, berichten Unternehmer. So seien Hybridantriebe dank ihres Dieselmotors noch praktikabler - auch wenn Hersteller wie Daimler davon ausgehen, dass sie nur Zwischenlösungen sein werden. Deswegen wird an neuen Projekten geforscht. In Mannheim tanken zwei Induktions-Busse seit Ende Juni Strom aus Metallplatten unter sechs Haltestellen - eine ähnliche Technik nutzen elektrische Zahnbürsten. Aufgeladen kommen die Busse rund 20 Kilometer weit, wie René Weintz vom Verkehrsbetrieb RNV sagt. Ob sich dieses System durchsetzen kann, weiß er nicht. «Es ist aber eine vielversprechende Technik.» Ohne Geld vom Staat wäre es jedoch wieder nicht möglich gewesen: 3,3 Millionen Euro hat das Bundesverkehrsministerium hier investiert. # Notizblock ## Redaktionelle Hinweise - Hintergrund «Neuartige Antriebe bei Omnibussen» bis 0600 - ca. 20 Zl ## Internet - [RNV Primove](http://dpaq.de/t9u8Q) - [Förderbedingungen Bundesumweltministerium](http://dpaq.de/lErR3) - [Kraftfahrtbundesamt-Zahlen](http://dpaq.de/cV1ZF) ## Orte - [Bundesverband Deutscher Omnibusunternehmer](Reinhardtstraße 25, 10117 Berlin, Deutschland) * * * * Die folgenden Informationen sind nicht zur Veröffentlichung bestimmt ## Ansprechpartner - Lars Wagner, Verband Deutscher Verkehrsunternehmen, +49 30 39993214, - Matthias Schröter, Bundesverband Deutscher Omnibusunternehmer, +49 30 24089300 - Christoph Kreienbaum, Hamburger Hochbahn, +49 40 32882121, - Uta Leitner, Daimler, +";
//
//
//		Unirest.setTimeouts(10000, 10000000);
//		HttpResponse<String> response = request("CondatTest/addDocument")
//				.queryString("documentName", "condat"+(new Date()).getTime())
//				.queryString("documentDescription", "")
//				.queryString("user", "jmschnei@gmail.com")
//				//			.queryString("format", "text")
//				.queryString("informat", "text")
//				//			.queryString("input", input)
//				//			.queryString("path", "")
//				.queryString("analysis", "ner_PER_ORG_LOC_en_all,temp_en")
//				.body(input)
//				.asString();
//		assertTrue(response.getStatus() == 200);
//		Assert.assertTrue(response.getBody().contains("successfully"));
//	}
}
