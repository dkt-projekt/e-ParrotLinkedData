package de.dkt.eservices.eparrotrepository.ddbb;

import org.json.JSONObject;

public class User {

	protected Integer userId;
	protected String email;
	protected String password;
	protected String name;

	public User() {
		super();
	}

	public User(Integer userId, String email, String password, String name) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public JSONObject getJSONObject(){
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("id", userId);
		resultJSON.put("email", email);
		resultJSON.put("password", password);
		resultJSON.put("name", name);
		return resultJSON;
	}
	
}
