package ca2;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
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

public class MainView extends Stage{

	private List<MenuItem> menuItems = new ArrayList<>();
	private MenuItem menuItemExit;
	
	private ScrollPane pane = new ScrollPane();
	private ImageView mapImageView = new ImageView();
	
	private ComboBox<City> fromCityDropdown = new ComboBox<>();
	private ComboBox<City> toCityDropdown = new ComboBox<>();
	
	public MainView() {
		super.setTitle("Map Navigator");
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1280, 720);
		
		VBox menuBox = new VBox(getMenuBar());
		root.setTop(menuBox);
		
		pane.setContent(mapImageView);
		root.setCenter(pane);
		
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(10, 40, 40, 40));
		
		Label fromLabel = new Label("From City");
		Label toLabel = new Label("To City");
		vBox.getChildren().addAll(fromLabel, fromCityDropdown, toLabel, toCityDropdown);
		
		root.setRight(vBox);
		
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

	public List<MenuItem> getAllMenuItems() {
		return menuItems;
	}

	public MenuItem getMenuItemExit() {
		return menuItemExit;
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

	public ComboBox<City> getFromCityDropdown() {
		return fromCityDropdown;
	}

	public ComboBox<City> getToCityDropdown() {
		return toCityDropdown;
	}
	
}
