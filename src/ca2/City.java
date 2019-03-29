package ca2;

import java.util.ArrayList;
import java.util.List;

public class City {

	public List<City> connectedCities = new ArrayList<>();
	public List<Route> connectedRoutes = new ArrayList<>();
	
	public String name;
	public int x, y;
	
	public City(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	public void connectRoutes(List<Route> routes) {
		for(Route route : routes) {
			if(route.fromCity.equals(this)) {
				connectedRoutes.add(route);
			}else if(route.toCity.equals(this)) {
				connectedRoutes.add(route);
			}
		}
	}
	
	public void connectCity(City city) {
		connectedCities.add(city);
		city.connectedCities.add(this);
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
