package ca2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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

	public Image drawCity(Image orgImage, City city) {
		int orgImgWidth = (int) orgImage.getWidth();
		int orgImgHeight = (int) orgImage.getHeight();

		PixelReader reader = orgImage.getPixelReader();

		WritableImage img = new WritableImage(orgImgWidth, orgImgHeight);
		PixelWriter writer = img.getPixelWriter();

		for (int row = 0; row < orgImgHeight; row++) {
			for (int col = 0; col < orgImgWidth; col++) {
				Color color = reader.getColor(col, row);
				
				boolean inRangeOfCity = (Math.abs(col - city.x) < 10 && Math.abs(row - city.y) < 10);
				
				if(inRangeOfCity)
					color = Color.BLACK;
				
				writer.setColor(col, row, color);
			}
		}

		return img;
	}
	
	public Image drawRoute(Image orgImage, Route route) {
		int orgImgWidth = (int) orgImage.getWidth();
		int orgImgHeight = (int) orgImage.getHeight();

		PixelReader reader = orgImage.getPixelReader();

		WritableImage img = new WritableImage(orgImgWidth, orgImgHeight);
		PixelWriter writer = img.getPixelWriter();

		int x1 = route.getFromCity().x;
		int x2 = route.getToCity().x;
		int y1 = route.getFromCity().y;
		int y2 = route.getToCity().y;
		
		int botRow = (y2 >= y1) ? y2 : y1;
		int topRow = (y2 >= y1) ? y1 : y2;
		int leftCol = (x2 >= x1) ? x1 : x2;
		int rightCol = (x2 >= x1) ? x2 : x1;
		
		double m = (double)(y2 - y1)/(x2-x1);
		
		for (int row = 0; row < orgImgHeight; row++) {
			for (int col = 0; col < orgImgWidth; col++) {
				Color color = reader.getColor(col, row);
				
				
				if((row < botRow && row > topRow) && (col > leftCol && col < rightCol)){
					double close = m * (col - x1) + y1 - row;
					boolean inRangeOfRoute = (Math.abs(close) < 5);
					if(inRangeOfRoute)
						color = Color.PURPLE;
				}
				
				writer.setColor(col, row, color);
			}
		}

		return img;
	}
	
}
