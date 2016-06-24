package de.dkt.eservices.eparrotrepository.ddbb;

import org.json.JSONObject;

public class Collection {

//	protected Integer id;
	protected int collectionId;
	protected int userId;
	protected String collectionName;
	protected String description;

	protected boolean priv;
	protected String analysis;
	protected String users;

	protected String timelining;
	protected String geolocalization;
	protected String entitylinking;
	protected String clustering;
	protected String documents;
	
	public Collection() {
		super();
	}

	public Collection(int collectionId, int userId, String collectionName, String description, boolean priv,
			String analysis, String users, String timelining, String geolocalization, String entitylinking,
			String clustering, String documents) {
		super();
		this.collectionId = collectionId;
		this.userId = userId;
		this.collectionName = collectionName;
		this.description = description;
		this.priv = priv;
		this.analysis = analysis;
		this.users = users;
		this.timelining = timelining;
		this.geolocalization = geolocalization;
		this.entitylinking = entitylinking;
		this.clustering = clustering;
		this.documents = documents;
	}


	public int getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getGeolocalization() {
		return geolocalization;
	}

	public void setGeolocalization(String geolocalization) {
		this.geolocalization = geolocalization;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimelining() {
		return timelining;
	}

	public void setTimelining(String timelining) {
		this.timelining = timelining;
	}

	public String getGeolocation() {
		return geolocalization;
	}

	public void setGeolocation(String geolocation) {
		this.geolocalization = geolocation;
	}

	public String getEntitylinking() {
		return entitylinking;
	}

	public void setEntitylinking(String entitylinking) {
		this.entitylinking = entitylinking;
	}

	public String getClustering() {
		return clustering;
	}

	public void setClustering(String clustering) {
		this.clustering = clustering;
	}

	public boolean isPriv() {
		return priv;
	}

	public void setPriv(boolean priv) {
		this.priv = priv;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getDocuments() {
		return documents;
	}

	public void setDocuments(String documents) {
		this.documents = documents;
	}

	public JSONObject getJSONObject(){
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("collectionId", collectionId);
		resultJSON.put("userId", userId);
		resultJSON.put("collectionName", collectionName);
		resultJSON.put("collectionDescription", description);
		resultJSON.put("private", priv);
		resultJSON.put("analysis", analysis);
		resultJSON.put("users", users);

		resultJSON.put("timelining", timelining);
		resultJSON.put("geolocalization", geolocalization);
		resultJSON.put("entitylinking", entitylinking);
		resultJSON.put("clustering", clustering);
		resultJSON.put("documents", documents);

		return resultJSON;
	}

}
