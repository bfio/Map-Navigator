package ca2;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainView extends Stage{

	@FXML
	private ImageView mapImageView;
	
	public MainView() throws IOException {
		super.setTitle("Map Navigator");
		
		Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
		Scene scene = new Scene(root);
		
		super.setScene(scene);
		super.show();
	}
	
}
