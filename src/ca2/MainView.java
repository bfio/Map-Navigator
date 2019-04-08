package ca2;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Stage {

	private List<MenuItem> menuItems = new ArrayList<>();
	private MenuItem menuItemExit;

	private ScrollPane pane = new ScrollPane();
	private Image mapImage;
	private ImageView mapImageView = new ImageView();

	private VBox controlPane = new VBox();
	private Button findRouteButtton = new Button("Find Route");
	private ComboBox<String> routeOperationsDropdown = new ComboBox<>();
	private ComboBox<City> fromCityDropdown = new ComboBox<>();
	private ComboBox<City> toCityDropdown = new ComboBox<>();
	private Button addWaypoint = new Button("Add Waypoint");
	private List<ComboBox<City>> waypoints = new ArrayList<>();
	private Button addAvoidCity = new Button("Avoid City");
	private List<ComboBox<City>> avoidCities = new ArrayList<>();
	private Button clearWaypoints = new Button("Clear All");

	public MainView(Image map) {
		super.setTitle("Map Navigator");
		super.setMaximized(true);

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);

		VBox menuBox = new VBox(getMenuBar());
		root.setTop(menuBox);

		this.mapImage = map;
		setImageView(mapImage);
		pane.setContent(mapImageView);
		root.setCenter(pane);

		controlPane.setSpacing(10);
		controlPane.setPadding(new Insets(10, 40, 40, 40));

		Label opsLabel = new Label("Options");
		Label fromLabel = new Label("From City");
		Label toLabel = new Label("To City");

		findRouteButtton.setMinWidth(150);
		routeOperationsDropdown.setMinWidth(150);
		fromCityDropdown.setMinWidth(150);
		toCityDropdown.setMinWidth(150);
		controlPane.getChildren().addAll(findRouteButtton, opsLabel, routeOperationsDropdown, fromLabel,
				fromCityDropdown, toLabel, toCityDropdown, addWaypoint, addAvoidCity, clearWaypoints);

		root.setRight(controlPane);

		super.setScene(scene);
		super.show();
	}

	private MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(createFileMenu());
		return menuBar;
	}

	private Menu createFileMenu() {
		Menu menuFile = new Menu("File");

		menuItemExit = new MenuItem("Exit");
		menuItems.add(menuItemExit);

		menuFile.getItems().addAll(menuItemExit);
		return menuFile;
	}

	public void createWaypointView(List<City> cities) {
		WaypointCB waypoint = new WaypointCB("Waypoint", cities);
		controlPane.getChildren().addAll(waypoint);
		waypoints.add(waypoint.dropdown);
	}

	public void createAvoidCityView(List<City> cities) {
		WaypointCB avoidCity = new WaypointCB("Avoid", cities);
		controlPane.getChildren().addAll(avoidCity);
		avoidCities.add(avoidCity.dropdown);
	}

	public List<MenuItem> getAllMenuItems() {
		return menuItems;
	}

	public MenuItem getMenuItemExit() {
		return menuItemExit;
	}

	public Button getFindRouteButton() {
		return findRouteButtton;
	}

	public Image getMapImg() {
		return mapImage;
	}

	public void setImageView(Image img) {
		mapImageView.setImage(img);
	}

	public void setFromCityDropdown(List<City> cities) {
		this.fromCityDropdown.getItems().addAll(cities);
	}

	public void setToCityDropdown(List<City> cities) {
		this.toCityDropdown.getItems().addAll(cities);
	}

	public void setRouteOperationsDropdown(String[] operations) {
		this.routeOperationsDropdown.getItems().addAll(operations);
	}

	public ComboBox<City> getFromCityDropdown() {
		return fromCityDropdown;
	}

	public ComboBox<City> getToCityDropdown() {
		return toCityDropdown;
	}

	public ComboBox<String> getRouteOperationsDropdown() {
		return routeOperationsDropdown;
	}

	public List<ComboBox<City>> getWaypointDropdown() {
		return waypoints;
	}

	public Button getAddWaypointButton() {
		return addWaypoint;
	}

	public List<ComboBox<City>> getAvoidCityDropdown() {
		return avoidCities;
	}

	public Button getAddAvoidCityButton() {
		return addAvoidCity;
	}
	
	public Button getClearWaypointsButton() {
		return clearWaypoints;
	}
	
	public void clearWaypoints() {
		waypoints.clear();
		avoidCities.clear();
		
		controlPane.getChildren().removeIf(n -> (n instanceof WaypointCB));
	}

}
