package ca2.tests;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca2.City;
import ca2.MainModel;
import ca2.MainModel.CostedPath;
import ca2.Route;

class MainModelTests {

	private MainModel model;
	
	@BeforeEach
	void setUp() throws Exception {
		model = new MainModel(new File("Database.csv"));
	}
	
	public class CostedPath {
		public double pathCost = 0;
		public List<City> pathList = new ArrayList<>();
	}
	
	@Test
	void testCollecMin() {
		CostedPath p1 = new CostedPath();
		CostedPath p2 = new CostedPath();
		
		p1.pathCost = 10;
		p2.pathCost = 20;
		
		List<CostedPath> allPaths = new ArrayList<>();
		allPaths.add(p1);
		allPaths.add(p2);
		
		CostedPath out = Collections.max(allPaths, (path1, path2) -> (int) (path1.pathCost - path2.pathCost));
		assertEquals(p2, out);
	}

	@Test
	void testSearch() {
		City from = model.getCities().get(0);
		City to = model.getCities().get(7);
		model.searchGraphDepthFirst(from, null, to);
	}
	
	@Test
	void distances() {
		List<Route> routes = model.getRoutes();
		
		for(Route r : routes) {
			double x1 = r.fromCity.x;
			double x2 = r.toCity.x;
			double y1 = r.fromCity.y;
			double y2 = r.toCity.y;
			
			double distance = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
			System.out.println(r.fromCity.name + " to " + r.toCity.name + " is: " + distance);
		}
	}
	
	@Test
	void testSearching() {
		testSearch("Winterfell", "Braavos");
	}
	
	void testSearch(String from, String to) {
		City fromCity = null, toCity = null;
		List<City> cities = model.getCities();
		
		for (City city : cities) {
			String cityName = city.name;

			if (from.equals(cityName)) {
				fromCity = city;
			} else if (to.equals(cityName)) {
				toCity = city;
			}
		}
		
		model.searchGraphDepthFirst(fromCity, null, toCity);
	}

}
