package ca2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
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
	
	public MainView() throws IOException {
		super.setTitle("Map Navigator");
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1280, 720);
		
		VBox menuBox = new VBox(getMenuBar());
		root.setTop(menuBox);
		
		pane.setContent(mapImageView);
		root.setCenter(pane);
		
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
	
}
