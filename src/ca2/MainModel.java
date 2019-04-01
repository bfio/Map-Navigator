package ca2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
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
		connectRoutes();
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
			r.fromCity.connectCity(r.toCity);
		}
	}

	private void connectRoutes() {
		for (City c : cities) {
			c.connectRoutes(routes);
		}
	}

	public List<City> getCities() {
		return cities;
	}

	public List<Route> getRoutes() {
		return routes;
	}
	
	public Route getRoute(City from, City to) {
		Route route = null;
		for(Route r : routes) {
			if((r.fromCity == from || r.fromCity == to) && (r.toCity == from || r.toCity == to)) {
				route = r;
				break;
			}
		}
		return route;
	}

	// Recursive depth-first search of graph (node returned if found)
	public City searchGraphDepthFirst(City from, List<City> encountered, City lookingfor) {
		System.out.println(from);
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

	// New class to hold a CostedPath object i.e. a list of GraphNodeAL2 objects and
	// a total cost attribute
	public class CostedPath {
		public double pathCost = 0;
		public List<City> pathList = new ArrayList<>();
	}

	// Retrieve cheapest path by expanding all paths recursively depth-first
	public <T> CostedPath searchGraphDepthFirstShortestPath(City from, List<City> encountered, double totalCost,
			City lookingfor) {
		if (from.equals(lookingfor)) { // Found it - end of path
			CostedPath cp = new CostedPath(); // Create a new CostedPath object
			cp.pathList.add(from); // Add the current node to it - only (end of path) element
			cp.pathCost = totalCost; // Use the current total cost
			return cp; // Return the CostedPath object
		}
		if (encountered == null)
			encountered = new ArrayList<>(); // First node so create new (empty) encountered list
		encountered.add(from);
		List<CostedPath> allPaths = new ArrayList<>(); // Collection for all candidate costed paths from this node
		for (Route adjLink : from.connectedRoutes) { // For every adjacent node

			if (adjLink.fromCity.equals(from)) {
				if (!encountered.contains(adjLink.toCity)) // That has not yet been encountered
				{
					// Create a new CostedPath from this node to the searched for item (if a valid
					// path exists)
					CostedPath temp = searchGraphDepthFirstShortestPath(adjLink.toCity, encountered,
							totalCost + adjLink.distance, lookingfor);
					if (temp == null)
						continue; // No path was found, so continue to the next iteration
					temp.pathList.add(0, from); // Add the current node to the front of the path list
					allPaths.add(temp); // Add the new candidate path to the list of all costed paths
				}
			} else {
				if (!encountered.contains(adjLink.fromCity)) // That has not yet been encountered
				{
					// Create a new CostedPath from this node to the searched for item (if a valid
					// path exists)
					CostedPath temp = searchGraphDepthFirstShortestPath(adjLink.fromCity, encountered,
							totalCost + adjLink.distance, lookingfor);
					if (temp == null)
						continue; // No path was found, so continue to the next iteration
					temp.pathList.add(0, from); // Add the current node to the front of the path list
					allPaths.add(temp); // Add the new candidate path to the list of all costed paths
				}
			}

		}
		// If no paths were found then return null. Otherwise, return the cheapest path
		// (i.e. the one with min pathCost)
		return allPaths.isEmpty() ? null : Collections.min(allPaths, (p1, p2) -> (int) (p1.pathCost - p2.pathCost));
	}

	// Retrieve cheapest path by expanding all paths recursively depth-first
	public <T> CostedPath searchGraphDepthFirstSafestPath(City from, List<City> encountered, double totalCost,
			City lookingfor) {
		if (from.equals(lookingfor)) { // Found it - end of path
			CostedPath cp = new CostedPath(); // Create a new CostedPath object
			cp.pathList.add(from); // Add the current node to it - only (end of path) element
			cp.pathCost = totalCost; // Use the current total cost
			return cp; // Return the CostedPath object
		}
		if (encountered == null)
			encountered = new ArrayList<>(); // First node so create new (empty) encountered list
		encountered.add(from);
		List<CostedPath> allPaths = new ArrayList<>(); // Collection for all candidate costed paths from this node
		for (Route adjLink : from.connectedRoutes) { // For every adjacent node

			if (adjLink.fromCity.equals(from)) {
				if (!encountered.contains(adjLink.toCity)) // That has not yet been encountered
				{
					// Create a new CostedPath from this node to the searched for item (if a valid
					// path exists)
					CostedPath temp = searchGraphDepthFirstShortestPath(adjLink.toCity, encountered,
							totalCost + (adjLink.safety), lookingfor);
					if (temp == null)
						continue; // No path was found, so continue to the next iteration
					temp.pathList.add(0, from); // Add the current node to the front of the path list
					allPaths.add(temp); // Add the new candidate path to the list of all costed paths
				}
			} else {
				if (!encountered.contains(adjLink.fromCity)) // That has not yet been encountered
				{
					// Create a new CostedPath from this node to the searched for item (if a valid
					// path exists)
					CostedPath temp = searchGraphDepthFirstShortestPath(adjLink.fromCity, encountered,
							totalCost + (adjLink.safety), lookingfor);
					if (temp == null)
						continue; // No path was found, so continue to the next iteration
					temp.pathList.add(0, from); // Add the current node to the front of the path list
					allPaths.add(temp); // Add the new candidate path to the list of all costed paths
				}
			}

		}
		// If no paths were found then return null. Otherwise, return the cheapest path
		// (i.e. the one with max safety)
		return allPaths.isEmpty() ? null : Collections.max(allPaths, (p1, p2) -> (int) (p1.pathCost - p2.pathCost));
	}

}
