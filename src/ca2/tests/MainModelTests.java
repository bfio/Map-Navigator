package ca2.tests;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca2.City;
import ca2.MainModel;

class MainModelTests {

	private MainModel model;
	
	@BeforeEach
	void setUp() throws Exception {
		model = new MainModel(new File("Database.csv"));
	}

	@Test
	void testSearch() {
		City from = model.getCities().get(0);
		City to = model.getCities().get(7);
		model.searchGraphDepthFirst(from, null, to);
	}
	
	@Test
	void testSearching() {
		testSearch("Winterfell", "Braavos");
	}
	
	void testSearch(String from, String to) {
		City fromCity = null, toCity = null;
		List<City> cities = model.getCities();
		
		for (City city : cities) {
			String cityName = city.getName();

			if (from.equals(cityName)) {
				fromCity = city;
			} else if (to.equals(cityName)) {
				toCity = city;
			}
		}
		
		model.searchGraphDepthFirst(fromCity, null, toCity);
	}

}
