package ca2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainModel {

	private List<City> cities = new ArrayList<>();
	private List<Route> routes = new ArrayList<>();

	public MainModel(File dbFile) {
		try {
			loadCities(dbFile);
			loadRoutes(dbFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		connectCities();
		System.out.println("Done loading Database");
	}

	private void loadCities(File dbFile) throws FileNotFoundException {
		Scanner mScanner = new Scanner(dbFile);
		mScanner.useDelimiter("[,\r\n]");

		mScanner.nextLine(); // Skips the labels on line 1
		while (mScanner.hasNextInt()) {
			int type = mScanner.nextInt();

			if (type == 0) {
				String name = mScanner.next();
				int x = mScanner.nextInt();
				int y = mScanner.nextInt();
				
				cities.add(new City(name, x, y));
			}

			if (mScanner.hasNextLine()) {
				mScanner.nextLine();
			}
		}

		mScanner.close();

	}

	private void loadRoutes(File dbFile) throws FileNotFoundException {
		Scanner mScanner = new Scanner(dbFile);
		mScanner.useDelimiter("[,\r\n]");

		mScanner.nextLine(); // Skips the labels on line 1
		while (mScanner.hasNextInt()) {
			int type = mScanner.nextInt();

			if (type == 1) {
				mScanner.next();
				mScanner.next();
				mScanner.next();

				String fromCityName = mScanner.next();
				String toCityName = mScanner.next();
				int ease = mScanner.nextInt();
				double distance = mScanner.nextDouble();
				int safety = mScanner.nextInt();

				City fromCity = null, toCity = null;

				for (City city : cities) {
					String cityName = city.name;

					if (fromCityName.equals(cityName)) {
						fromCity = city;
					} else if (toCityName.equals(cityName)) {
						toCity = city;
					}
				}

				routes.add(new Route(fromCity, toCity, ease, distance, safety));
			}

			if (mScanner.hasNextLine()) {
				mScanner.nextLine();
			}

		}

		mScanner.close();
	}

	private void connectCities() {
		for (Route r : routes) {
			r.getFromCity().connectCity(r.getToCity());
		}
	}

	public List<City> getCities() {
		return cities;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	// Recursive depth-first search of graph (node returned if found)
	public City searchGraphDepthFirst(City from, List<City> encountered, City lookingfor) {
		System.out.println(from.name);
		if (from.equals(lookingfor))
			return from;
		if (encountered == null)
			encountered = new ArrayList<>(); // First node so create new (empty) encountered list
		encountered.add(from);
		for (City adjNode : from.connectedCities)
			if (!encountered.contains(adjNode)) {
				City result = searchGraphDepthFirst(adjNode, encountered, lookingfor);
				if (result != null)
					return result;
			}
		return null;
	}
}
