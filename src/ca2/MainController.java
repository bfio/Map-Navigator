package ca2;

import java.util.List;

import ca2.MainModel;
import ca2.MainView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class MainController implements EventHandler<ActionEvent> {

	private static final String[] ROUTE_OPERATIONS = {"Multiple Routes", "Shortest Route", "Easiest Route", "Safest Route"};
	
	private MainView view;
	private MainModel model;

	private Image displayedImg;

	public MainController(MainView view, MainModel model) {
		this.view = view;
		this.model = model;

		view.getAllMenuItems().forEach(m -> m.setOnAction(this));
		view.getFromCityDropdown().setOnAction(this);
		view.getToCityDropdown().setOnAction(this);
		view.getRouteOperationsDropdown().setOnAction(this);

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

		City fromCity = view.getFromCityDropdown().getValue();
		City toCity = view.getToCityDropdown().getValue();

		if (event.getSource().equals(view.getFromCityDropdown())) {
			System.out.println("From City updated to: " + fromCity);

		} else if (event.getSource().equals(view.getToCityDropdown())) {
			System.out.println("To City updated to: " + toCity);
		}

		resetDisplayedImg();
		displayedImg = drawCity(displayedImg, fromCity);
		displayedImg = drawCity(displayedImg, toCity);

		view.setImageView(displayedImg);

		if (fromCity != null && toCity != null) {
			model.searchGraphDepthFirst(fromCity, null, toCity);
		}

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

	public Image drawRoute(Image orgImage, Route route) {
		int orgImgWidth = (int) orgImage.getWidth();
		int orgImgHeight = (int) orgImage.getHeight();

		PixelReader reader = orgImage.getPixelReader();

		WritableImage img = new WritableImage(reader, orgImgWidth, orgImgHeight);
		PixelWriter writer = img.getPixelWriter();

		int x1 = route.getFromCity().x;
		int x2 = route.getToCity().x;
		int y1 = route.getFromCity().y;
		int y2 = route.getToCity().y;

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
					Color color = Color.PURPLE;
					writer.setColor(col, row, color);
				}

			}
		}

		return img;
	}

}
