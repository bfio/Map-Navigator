package ca2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainModel {

	private List<City> cities = new ArrayList<>();
	private List<Route> routes = new ArrayList<>();
	
	public void parseDatabase(File dbFile) {
		Scanner mScanner;
		try {
			mScanner = new Scanner(dbFile);

			mScanner.useDelimiter("[,\r\n]");

			while (mScanner.hasNextLine()) {
				mScanner.nextLine();

				int type = mScanner.nextInt();

				if (type == 0) {
					String name = mScanner.next();
					int x = mScanner.nextInt();
					int y = mScanner.nextInt();
					System.out.println(name + " " + x + " " + y);

					cities.add(new City(name, x, y));
				} else if (type == 1) {
					mScanner.next();
					mScanner.next();
					mScanner.next();

					String fromCity = mScanner.next();
					String toCity = mScanner.next();
					int ease = mScanner.nextInt();
					String distance = mScanner.next();
					int safety = mScanner.nextInt();

					System.out.println("Route " + fromCity + " to " + toCity + '\t' + "Ease: " + ease + " Distance: "
							+ distance + " Saftey: " + safety);
					
					routes.add(new Route(fromCity, toCity, ease, 0.0, safety));
				}

			}

			mScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		mapRoutesToCities();
		
		System.out.println("Done loading Database");
	}

	private void mapRoutesToCities() {
		for (Route route : routes) {
			for(City city : cities) {
				String cityName = city.getName();
				
				if(route.getFromCityName().equals(cityName)) {
					route.setFromCity(city);
				}
				
				if(route.getToCityName().equals(cityName)) {
					route.setToCity(city);
				}
			}
		}
	}

}
