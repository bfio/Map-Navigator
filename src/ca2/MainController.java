package ca2;

import java.util.List;

import ca2.MainModel;
import ca2.MainView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

public class MainController implements EventHandler<ActionEvent>{

	private MainView view;
	private MainModel model;
	
	public MainController(MainView view, MainModel model) {
		this.view = view;
		this.model = model;

		view.getAllMenuItems().forEach(m -> m.setOnAction(this));
		view.getFromCityDropdown().setOnAction(this);
		view.getToCityDropdown().setOnAction(this);
		
		displayMap();
		setUpControllerPane();
	}

	private void displayMap() {
		Image img = new Image("file:map.jpg");
		view.setImageView(img);
	}
	
	private void setUpControllerPane() {
		List<City> cities = model.getCities();
		
		view.setFromCityDropdown(cities);
		view.setToCityDropdown(cities);
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
		
		if(fromCity != null && toCity != null)
			model.searchGraphDepthFirst(fromCity, null, toCity);
		
	}
	
}
