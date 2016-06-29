package de.dkt.eservices.eparrotrepository.ddbb;

import org.json.JSONObject;

public class NLPModel {

	protected int modelId;
	protected String modelName;

	protected String type;
	
	protected String url;
	protected String analysis;
	protected String language;
	protected String models;
	protected String informat;
	protected String outformat;
	protected String mode;
	
	
	public NLPModel() {
		super();
	}

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}

	public String getInformat() {
		return informat;
	}

	public void setInformat(String informat) {
		this.informat = informat;
	}

	public String getOutformat() {
		return outformat;
	}

	public void setOutformat(String outformat) {
		this.outformat = outformat;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public JSONObject getJSONObject(){
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("modelId", modelId);
		resultJSON.put("modelName", modelName);
		resultJSON.put("modelType", type);
		resultJSON.put("url", url);
		resultJSON.put("analysis", analysis);
		resultJSON.put("language", language);
		resultJSON.put("models", models);
		resultJSON.put("informat", informat);
		resultJSON.put("outformat", outformat);
		resultJSON.put("mode", mode);

		return resultJSON;
	}

}
