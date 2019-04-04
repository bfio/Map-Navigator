package ca2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca2.MainModel;
import ca2.MainModel.CostedPath;
import ca2.MainView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class MainController implements EventHandler<ActionEvent> {

	private static final String[] ROUTE_OPERATIONS = { "Multiple Routes", "Shortest Route", "Easiest Route",
			"Safest Route" };

	private MainView view;
	private MainModel model;

	private Image displayedImg;

	public MainController(MainView view, MainModel model) {
		this.view = view;
		this.model = model;

		view.getAllMenuItems().forEach(m -> m.setOnAction(this));
		view.getFromCityDropdown().setOnAction(this);
		view.getToCityDropdown().setOnAction(this);
		view.getFindRouteButton().setOnAction(this);
		view.getAddWaypointButton().setOnAction(this);
		view.getAddAvoidCityButton().setOnAction(this);

		setUpControllerPane();

		resetDisplayedImg();
	}

	private void resetDisplayedImg() {
		displayedImg = view.getMapImg();
	}

	private void setUpControllerPane() {
		List<City> cities = model.getCities();

		view.setFromCityDropdown(cities);
		view.setToCityDropdown(cities);
		view.setRouteOperationsDropdown(ROUTE_OPERATIONS);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource().equals(view.getMenuItemExit())) {
			Platform.exit();
			return;
		}

		if (event.getSource().equals(view.getFromCityDropdown())
				|| event.getSource().equals(view.getToCityDropdown())) {
			resetDisplayedImg();

			City fromCity = view.getFromCityDropdown().getValue();
			City toCity = view.getToCityDropdown().getValue();

			displayedImg = drawCity(displayedImg, fromCity);
			displayedImg = drawCity(displayedImg, toCity);
		} else if (event.getSource().equals(view.getAddWaypointButton())) {
			view.createWaypointView(model.getCities());
		} else if (event.getSource().equals(view.getAddAvoidCityButton())) {
			view.createAvoidCityView(model.getCities());
		} else if (event.getSource().equals(view.getFindRouteButton())) {
			resetDisplayedImg();
			findRoute();
		}

		view.setImageView(displayedImg);
	}

	private void findRoute() {
		City fromCity = view.getFromCityDropdown().getValue();
		City toCity = view.getToCityDropdown().getValue();

		if (fromCity != null && toCity != null) {

			List<City> stops = new ArrayList<>();
			stops.add(fromCity);
			view.getWaypointDropdown().forEach(cb -> stops.add(cb.getValue()));
			stops.add(toCity);
			stops.removeIf(Objects::isNull);

			String selectedOp = view.getRouteOperationsDropdown().getValue();

			List<City> avoidCity = null;
			if (!view.getAvoidCityDropdown().isEmpty()) {
				avoidCity = new ArrayList<>();
				for (ComboBox<City> cb : view.getAvoidCityDropdown()) {
					avoidCity.add(cb.getValue());
				}
				avoidCity.removeIf(Objects::isNull);
			}

			List<City> paths = new ArrayList<>();
			for (int i = 1; i < stops.size(); i++) {
				List<City> tempEncount = null;
				if (avoidCity != null) {
					tempEncount = new ArrayList<>();
					tempEncount.addAll(avoidCity);
				}
				paths.addAll(getPath(stops.get(i - 1), stops.get(i), tempEncount, selectedOp));
			}

			drawPaths(paths, selectedOp);
		}
	}

	private void drawPaths(List<City> paths, String selectedOp) {
		for (int i = 1; i < paths.size(); i++) {
			City fr = paths.get(i - 1);
			City to = paths.get(i);

			if (fr.equals(to))
				continue;

			Color routeColor;
			Route r = model.getRoute(fr, to);

			if (selectedOp.equals(ROUTE_OPERATIONS[1])) {
				routeColor = getShortestColor(r);
			} else if (selectedOp.equals(ROUTE_OPERATIONS[2])) {
				routeColor = getEasiestColor(r);
			} else if (selectedOp.equals(ROUTE_OPERATIONS[3])) {
				routeColor = getEasiestColor(r);
			} else {
				routeColor = getShortestColor(r);
			}

			displayedImg = drawRoute(displayedImg, fr, to, routeColor);
		}
	}

	public Color getShortestColor(Route r) {
		double threshColor = r.distance;

		if (threshColor < 150) {
			return Color.GREEN;
		} else if (threshColor > 400) {
			return Color.RED;
		} else {
			return Color.YELLOW;
		}
	}

	public Color getEasiestColor(Route r) {
		double threshColor = r.ease;

		if (threshColor < 4) {
			return Color.RED;
		} else if (threshColor >= 7) {
			return Color.GREEN;
		} else {
			return Color.YELLOW;
		}
	}

	private List<City> getPath(City fromCity, City toCity, List<City> avoidCities, String selectedOp) {
		List<City> path = new ArrayList<>();
		if (selectedOp == null) {
			model.searchGraphDepthFirst(fromCity, null, toCity);
		} else if (selectedOp.equals(ROUTE_OPERATIONS[0])) {
			System.out.println("Find Multiple Routes");
		} else if (selectedOp.equals(ROUTE_OPERATIONS[1])) {
			CostedPath shortestPath = model.findShortestPath(fromCity, avoidCities, toCity);
			path = shortestPath.pathList;
		} else if (selectedOp.equals(ROUTE_OPERATIONS[2])) {
			CostedPath shortestPath = model.findEasiestPath(fromCity, avoidCities, toCity);
			path = shortestPath.pathList;
		} else if (selectedOp.equals(ROUTE_OPERATIONS[3])) {
			CostedPath safestPath = model.findSafestPath(fromCity, avoidCities, toCity);
			path = safestPath.pathList;
		}

		return path;
	}

	public Image drawCity(Image orgImage, City city) {
		if (city == null)
			return orgImage;

		int orgImgWidth = (int) orgImage.getWidth();
		int orgImgHeight = (int) orgImage.getHeight();

		PixelReader reader = orgImage.getPixelReader();
		WritableImage img = new WritableImage(reader, orgImgWidth, orgImgHeight);
		PixelWriter writer = img.getPixelWriter();

		for (int row = city.y - 10; row < city.y + 10; row++) {
			for (int col = city.x - 10; col < city.x + 10; col++) {
				Color color = Color.AQUA;
				writer.setColor(col, row, color);
			}
		}

		return img;
	}

	public Image drawRoute(Image orgImage, City fromCity, City toCity, Color color) {
		int orgImgWidth = (int) orgImage.getWidth();
		int orgImgHeight = (int) orgImage.getHeight();

		PixelReader reader = orgImage.getPixelReader();

		WritableImage img = new WritableImage(reader, orgImgWidth, orgImgHeight);
		PixelWriter writer = img.getPixelWriter();

		int x1 = fromCity.x;
		int x2 = toCity.x;
		int y1 = fromCity.y;
		int y2 = toCity.y;

		int botRow = (y2 >= y1) ? y2 : y1;
		int topRow = (y2 >= y1) ? y1 : y2;
		int leftCol = (x2 >= x1) ? x1 : x2;
		int rightCol = (x2 >= x1) ? x2 : x1;

		double m = (double) (y2 - y1) / (x2 - x1);

		for (int row = topRow; row < botRow; row++) {
			for (int col = leftCol; col < rightCol; col++) {

				double close = m * (col - x1) + y1 - row;
				boolean inRangeOfRoute = (Math.abs(close) < 5);
				if (inRangeOfRoute) {
					writer.setColor(col, row, color);
				}

			}
		}

		return img;
	}

}
