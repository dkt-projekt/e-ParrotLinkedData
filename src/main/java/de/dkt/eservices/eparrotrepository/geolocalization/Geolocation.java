package de.dkt.eservices.eparrotrepository.geolocalization;

public class Geolocation implements Comparable{

	public Geopoint latitude;
	public Geopoint longitude;
	
	public Geolocation() {
	}
	
	public Geolocation(String s) {
		if(s.equalsIgnoreCase("MIN")){
			latitude = new Geopoint("-90");
			longitude = new Geopoint("0");
		}
		else if(s.equalsIgnoreCase("MAX")){
			latitude = new Geopoint("90");
			longitude = new Geopoint("360");
		}
		else{
			//System.out.println(s);
			String parts[] = s.split("_");
			latitude = new Geopoint(parts[0]);
			longitude = new Geopoint(parts[1]);
		}
	}
	
	@Override
	public int compareTo(Object o) {
		if(o instanceof Geolocation){
			Geolocation ter = (Geolocation) o;
			if(latitude.compareTo(ter.latitude)==0){
				return longitude.compareTo(ter.longitude);
			}
//			else if(initialTime.compareTo(ter.initialTime)<0){
//				return initialTime.compareTo(ter.initialTime);
//			}
			else{
				return latitude.compareTo(ter.latitude);
			}
		}
		return Integer.MIN_VALUE;
	}
	
	@Override
	public String toString() {
		return latitude.toString()+"_"+longitude.toString();
	}

}
