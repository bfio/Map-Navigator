package ca2;
import java.io.File;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MapNavigatorApplication extends Application {

	public static Stage mainStage = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		mainStage = stage;

		MainView mainView = new MainView(new Image("file:map2.jpg"));
		MainModel mainModel = new MainModel(new File("Database.csv"));
		new MainController(mainView, mainModel);
	}

}