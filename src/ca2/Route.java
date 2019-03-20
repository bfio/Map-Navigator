package ca2;

public class Route {

	private City fromCity, toCity;
	
	private String fromCityName, toCityName;
	private int ease, safety;
	private double distance;
	
	public Route(String fCity, String tCity, int ease, double distance, int safety){
		this.fromCityName = fCity;
		this.toCityName = tCity;
		this.ease = ease;
		this.distance = distance;
		this.safety = safety;
	}
	
	public String getFromCityName() {
		return fromCityName;
	}
	
	public String getToCityName() {
		return toCityName;
	}
	
	public void setFromCity(City c) {
		this.fromCity = c;
	}
	
	public void setToCity(City c) {
		this.toCity = c;
	}
	
}
