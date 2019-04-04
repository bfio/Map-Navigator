package ca2;

public class Route {

	public City fromCity, toCity;

	public int ease, safety;
	public double distance;

	public Route(City from, City to, int ease, double distance, int safety) {
		this.fromCity = from;
		this.toCity = to;
		this.ease = ease;
		this.distance = distance;
		this.safety = safety;
	}

}
