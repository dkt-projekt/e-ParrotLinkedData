package de.dkt.eservices.eparrotrepository.geolocalization;

import java.io.Serializable;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.niftools.NIFReader;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;

public class GeolocalizedElement implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public enum GeoElementType {DOCUMENT,CONCEPT,GEOPOSITION};
	
	public GeoElementType type;
	public String uri;
	public Geolocation geolocationExpression;
	public Model model;
	
	public GeolocalizedElement(GeoElementType type, String uri, Model model) {
		super();
		this.type = type;
		this.uri = uri;
		this.model = model;
	}
	
	public GeolocalizedElement(GeoElementType type, String uri, Geolocation geolocationExpression, Model model) {
		super();
		this.type = type;
		this.uri = uri;
		this.geolocationExpression = geolocationExpression;
		this.model = model;
	}

	public GeolocalizedElement(String s) throws Exception {
		super();
		String parts[] = s.split("<-->");
		this.type = GeoElementType.valueOf(parts[0]);
		this.uri = parts[1];
		this.geolocationExpression = new Geolocation(parts[2]);
		this.model = NIFReader.extractModelFromFormatString(parts[3], RDFSerialization.TURTLE);
	}

	@Override
	public String toString() {
		return type.name()+"<-->"+uri+"<-->"+geolocationExpression.toString()+"<-->"+NIFReader.model2String(model, "TTL");
	}
}
