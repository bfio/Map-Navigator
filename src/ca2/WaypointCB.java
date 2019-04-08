package ca2;

import java.util.List;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WaypointCB extends VBox {
	
	public ComboBox<City> dropdown;
	
	public WaypointCB(String labelName, List<City> cities) {
		Label label = new Label(labelName);
		dropdown = new ComboBox<City>();
		dropdown.getItems().addAll(cities);
		dropdown.setMinWidth(150);
        this.getChildren().addAll(label, dropdown);
	}

}
