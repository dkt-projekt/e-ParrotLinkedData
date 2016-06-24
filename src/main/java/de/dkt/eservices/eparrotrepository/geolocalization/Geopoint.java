package de.dkt.eservices.eparrotrepository.geolocalization;

public class Geopoint {

	String text;
	
	public Geopoint() {
		super();
	}

	public Geopoint(String text) {
		super();
		if(text.equalsIgnoreCase("MIN")){
			text = "-90";
		}
		else if(text.equalsIgnoreCase("MAX")){
			text = "90";
		}
		else{
			this.text = text;
		}
	}

	public int compareTo(Object o) {
		if(o instanceof Geopoint){
			Geopoint te = (Geopoint) o;
//			if(text.compareTo(te.text)==0){
//				return 0;
//			}
//			else if(text.compareTo(te.text)>0){
//				return text.compareTo(te.text);
//			}
//			else{
//				return text.compareTo(te.text);
//			}
			float f1 = Float.parseFloat(te.text);
			float f2 = Float.parseFloat(te.text);
			if(f1==f2){
				return 0;
			}
			else if(f1>f2){
				return -1;
			}
			else{
				return 1;
			}
		}
		return Integer.MIN_VALUE;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}
