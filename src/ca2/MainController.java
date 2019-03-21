package ca2;

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
		
		displayMap();
	}

	private void displayMap() {
		Image img = new Image("file:map.jpg");
		view.setImageView(img);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource().equals(view.getMenuItemExit())) {
			Platform.exit();
		}
	}
	
}
