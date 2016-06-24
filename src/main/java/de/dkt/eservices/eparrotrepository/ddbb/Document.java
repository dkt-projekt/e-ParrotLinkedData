package de.dkt.eservices.eparrotrepository.ddbb;

import org.json.JSONObject;

public class Document {

	protected int documentId;
	protected int collectionId;
	protected String documentName;
	protected String description;
	
	protected String content;
	protected String annotatedContent;
	protected String highlightedContent;

	public Document() {
		super();
	}

	public Document(int documentId, int collectionId, String documentName, String description, String content,
			String annotatedContent, String highlightedContent) {
		super();
		this.documentId = documentId;
		this.collectionId = collectionId;
		this.documentName = documentName;
		this.description = description;
		this.content = content;
		this.annotatedContent = annotatedContent;
		this.highlightedContent = highlightedContent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAnnotatedContent() {
		return annotatedContent;
	}

	public void setAnnotatedContent(String annotatedContent) {
		this.annotatedContent = annotatedContent;
	}

	public String getHighlightedContent() {
		return highlightedContent;
	}

	public void setHighlightedContent(String highlightedContent) {
		this.highlightedContent = highlightedContent;
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public int getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public JSONObject getJSONObject(){
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("documentId", documentId);
		resultJSON.put("collectionId", collectionId);
		resultJSON.put("documentName", documentName);
		resultJSON.put("documentId", documentId);
		resultJSON.put("description", description);
		resultJSON.put("content", content);
		resultJSON.put("annotatedcontent", annotatedContent);
		resultJSON.put("highlightedcontent", highlightedContent);
		return resultJSON;
	}
}
