package ca2;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainView extends Stage{

	public MainView() {
		super.setTitle("Map Navigator");
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1200, 675);
		
		super.setScene(scene);
		super.show();
	}
	
}
