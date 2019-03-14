package ca2;

import ca2.MainModel;
import ca2.MainView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MainController implements EventHandler<ActionEvent>{

	private MainView view;
	private MainModel model;
	
	public MainController(MainView view, MainModel model) {
		this.view = view;
		this.model = model;


	}

	@Override
	public void handle(ActionEvent event) {
		
	}
	
}
