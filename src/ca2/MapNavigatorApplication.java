package ca2;
import javafx.application.Application;
import javafx.stage.Stage;

public class MapNavigatorApplication extends Application {

	public static Stage mainStage = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		mainStage = stage;

		MainView mainView = new MainView();
		MainModel mainModel = new MainModel();
		new MainController(mainView, mainModel);
	}

}