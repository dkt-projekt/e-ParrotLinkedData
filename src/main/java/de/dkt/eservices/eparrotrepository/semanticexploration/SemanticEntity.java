package de.dkt.eservices.eparrotrepository.semanticexploration;

public class SemanticEntity {

	public String text;
	public String uri;
	
	public SemanticEntity(String text, String uri) {
		super();
		this.text = text;
		this.uri = uri;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
