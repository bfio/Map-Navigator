package ca2;

import java.util.ArrayList;
import java.util.List;

public class City {

	public List<City> connectedCities = new ArrayList<>();
	
	public String name;
	public int x, y;
	
	public City(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	public void connectCity(City city) {
		connectedCities.add(city);
		city.connectedCities.add(this);
	}
	
}
