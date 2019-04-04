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
		for (Route r : routes) {
			if ((r.fromCity == from || r.fromCity == to) && (r.toCity == from || r.toCity == to)) {
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

		public String toString() {
			return "Path: " + pathList;
		}
	}

	public <T> CostedPath findShortestPath(City startNode, List<City> encountered, City lookingfor) {
		CostedPath cp = new CostedPath(); // Create result object for cheapest path

		if (encountered == null)
			encountered = new ArrayList<>();

		List<City> unencountered = new ArrayList<>();

		startNode.nodeValue = 0; // Set the starting node value to zero
		unencountered.add(startNode); // Add the start node as the only value in the unencountered list to start
		City currentNode;
		do { // Loop until unencountered list is empty
			currentNode = unencountered.remove(0); // Get the first unencountered node (sorted list, so will have lowest
													// value)

			encountered.add(currentNode); // Record current node in encountered list
			if (currentNode.equals(lookingfor)) { // Found goal - assemble path list back to start and return it
				cp.pathList.add(currentNode); // Add the current (goal) node to the result list (only element)
				cp.pathCost = currentNode.nodeValue; // The total cheapest path cost is the node value of the
														// current/goal node
				while (currentNode != startNode) { // While we're not back to the start node...
					boolean foundPrevPathNode = false; // Use a flag to identify when the previous path node is
														// identified
					for (City n : encountered) { // For each node in the encountered list...
						for (Route e : n.connectedRoutes) { // For each edge from that node...
							int routeCost = (int) e.distance;
							if ((e.toCity == currentNode || e.fromCity == currentNode)
									&& currentNode.nodeValue - routeCost == n.nodeValue) {
								// If that the
								// current node and the difference in node values is the cost of the edge ->
								// found path node!
								cp.pathList.add(0, n); // Add the identified path node to the front of the result list
								currentNode = n; // Move the currentNode reference back to the identified path node
								foundPrevPathNode = true; // Set the flag to break the outer loop
								break; // We've found the correct previous path node and moved the currentNode
										// reference
								// back to it so break the inner loop
							}
						}
						if (foundPrevPathNode)
							break; // We've identified the previous path node, so break the inner loop to continue
					}
				}
				// Reset the node values for all nodes to (effectively) infinity so we can
				// search again (leave no footprint!)
				for (City n : encountered)
					n.nodeValue = Integer.MAX_VALUE;
				for (City n : unencountered)
					n.nodeValue = Integer.MAX_VALUE;
				return cp; // The costed (cheapest) path has been assembled, so return it!
			}
			// We're not at the goal node yet, so...
			for (Route e : currentNode.connectedRoutes) { // For each edge/link from the current node...
				City dest = e.toCity == currentNode ? e.fromCity : e.toCity;
				if (!encountered.contains(dest)) { // If the node it leads to
													// has not yet been
													// encountered (i.e.
					// processed)
					int routeCost = (int) e.distance;
					dest.nodeValue = Integer.min(dest.nodeValue, currentNode.nodeValue + routeCost); // Update
					// end
					// of the edge to the minimum of its current value or the total of the current
					// node's value plus the cost of the edge
					unencountered.add(dest);
				}
			}
			Collections.sort(unencountered, (n1, n2) -> n1.nodeValue - n2.nodeValue); // Sort in ascending node value
																						// order
		} while (!unencountered.isEmpty());
		return null; // No path found, so return null
	}

	public <T> CostedPath findEasiestPath(City startNode, List<City> encountered, City lookingfor) {
		CostedPath cp = new CostedPath(); // Create result object for cheapest path

		if (encountered == null)
			encountered = new ArrayList<>();

		List<City> unencountered = new ArrayList<>();

		startNode.nodeValue = 0; // Set the starting node value to zero
		unencountered.add(startNode); // Add the start node as the only value in the unencountered list to start
		City currentNode;
		do { // Loop until unencountered list is empty
			currentNode = unencountered.remove(0); // Get the first unencountered node (sorted list, so will have lowest
													// value)

			encountered.add(currentNode); // Record current node in encountered list
			if (currentNode.equals(lookingfor)) { // Found goal - assemble path list back to start and return it
				cp.pathList.add(currentNode); // Add the current (goal) node to the result list (only element)
				cp.pathCost = currentNode.nodeValue; // The total cheapest path cost is the node value of the
														// current/goal node
				while (currentNode != startNode) { // While we're not back to the start node...
					boolean foundPrevPathNode = false; // Use a flag to identify when the previous path node is
														// identified
					for (City n : encountered) { // For each node in the encountered list...
						for (Route e : n.connectedRoutes) { // For each edge from that node...
							int routeCost = 10 - e.ease;
							if ((e.toCity == currentNode || e.fromCity == currentNode)
									&& currentNode.nodeValue - routeCost == n.nodeValue) {
								// If that the
								// current node and the difference in node values is the cost of the edge ->
								// found path node!
								cp.pathList.add(0, n); // Add the identified path node to the front of the result list
								currentNode = n; // Move the currentNode reference back to the identified path node
								foundPrevPathNode = true; // Set the flag to break the outer loop
								break; // We've found the correct previous path node and moved the currentNode
										// reference
								// back to it so break the inner loop
							}
						}
						if (foundPrevPathNode)
							break; // We've identified the previous path node, so break the inner loop to continue
					}
				}
				// Reset the node values for all nodes to (effectively) infinity so we can
				// search again (leave no footprint!)
				for (City n : encountered)
					n.nodeValue = Integer.MAX_VALUE;
				for (City n : unencountered)
					n.nodeValue = Integer.MAX_VALUE;
				return cp; // The costed (cheapest) path has been assembled, so return it!
			}
			// We're not at the goal node yet, so...
			for (Route e : currentNode.connectedRoutes) { // For each edge/link from the current node...
				City dest = e.toCity == currentNode ? e.fromCity : e.toCity;
				if (!encountered.contains(dest)) { // If the node it leads to
													// has not yet been
													// encountered (i.e.
					// processed)
					int routeCost = 10 - e.ease;
					dest.nodeValue = Integer.min(dest.nodeValue, currentNode.nodeValue + routeCost); // Update
					// end
					// of the edge to the minimum of its current value or the total of the current
					// node's value plus the cost of the edge
					unencountered.add(dest);
				}
			}
			Collections.sort(unencountered, (n1, n2) -> n1.nodeValue - n2.nodeValue); // Sort in ascending node value
																						// order
		} while (!unencountered.isEmpty());
		return null; // No path found, so return null
	}

	public <T> CostedPath findSafestPath(City startNode, List<City> encountered, City lookingfor) {
		CostedPath cp = new CostedPath(); // Create result object for cheapest path

		if (encountered == null)
			encountered = new ArrayList<>();

		List<City> unencountered = new ArrayList<>();

		startNode.nodeValue = 0; // Set the starting node value to zero
		unencountered.add(startNode); // Add the start node as the only value in the unencountered list to start
		City currentNode;
		do { // Loop until unencountered list is empty
			currentNode = unencountered.remove(0); // Get the first unencountered node (sorted list, so will have lowest
													// value)

			encountered.add(currentNode); // Record current node in encountered list
			if (currentNode.equals(lookingfor)) { // Found goal - assemble path list back to start and return it
				cp.pathList.add(currentNode); // Add the current (goal) node to the result list (only element)
				cp.pathCost = currentNode.nodeValue; // The total cheapest path cost is the node value of the
														// current/goal node
				while (currentNode != startNode) { // While we're not back to the start node...
					boolean foundPrevPathNode = false; // Use a flag to identify when the previous path node is
														// identified
					for (City n : encountered) { // For each node in the encountered list...
						for (Route e : n.connectedRoutes) { // For each edge from that node...
							int routeCost = 10 - e.safety;
							if ((e.toCity == currentNode || e.fromCity == currentNode)
									&& currentNode.nodeValue - routeCost == n.nodeValue) {
								// If that the
								// current node and the difference in node values is the cost of the edge ->
								// found path node!
								cp.pathList.add(0, n); // Add the identified path node to the front of the result list
								currentNode = n; // Move the currentNode reference back to the identified path node
								foundPrevPathNode = true; // Set the flag to break the outer loop
								break; // We've found the correct previous path node and moved the currentNode
										// reference
								// back to it so break the inner loop
							}
						}
						if (foundPrevPathNode)
							break; // We've identified the previous path node, so break the inner loop to continue
					}
				}
				// Reset the node values for all nodes to (effectively) infinity so we can
				// search again (leave no footprint!)
				for (City n : encountered)
					n.nodeValue = Integer.MAX_VALUE;
				for (City n : unencountered)
					n.nodeValue = Integer.MAX_VALUE;
				return cp; // The costed (cheapest) path has been assembled, so return it!
			}
			// We're not at the goal node yet, so...
			for (Route e : currentNode.connectedRoutes) { // For each edge/link from the current node...
				City dest = e.toCity == currentNode ? e.fromCity : e.toCity;
				if (!encountered.contains(dest)) { // If the node it leads to
													// has not yet been
													// encountered (i.e.
					// processed)
					int routeCost = 10 - e.safety;
					dest.nodeValue = Integer.min(dest.nodeValue, currentNode.nodeValue + routeCost); // Update
					// end
					// of the edge to the minimum of its current value or the total of the current
					// node's value plus the cost of the edge
					unencountered.add(dest);
				}
			}
			Collections.sort(unencountered, (n1, n2) -> n1.nodeValue - n2.nodeValue); // Sort in ascending node value
																						// order
		} while (!unencountered.isEmpty());
		return null; // No path found, so return null
	}

}
