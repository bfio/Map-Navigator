package ca2;

public class Route {

	private City fromCity, toCity;
	
	private int ease, safety;
	private double distance;
	
	public Route(City from, City to, int ease, double distance, int safety){
		this.fromCity = from;
		this.toCity = to;
		this.ease = ease;
		this.distance = distance;
		this.safety = safety;
	}

	public City getFromCity() {
		return fromCity;
	}

	public City getToCity() {
		return toCity;
	}
	
}
