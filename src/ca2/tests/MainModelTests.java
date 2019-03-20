package ca2.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca2.MainModel;

class MainModelTests {

	private MainModel model;
	
	@BeforeEach
	void setUp() throws Exception {
		model = new MainModel();
		model.parseDatabase(new File("Database.csv"));
	}

	@Test
	void test() {
		assertFalse(model.getCities().isEmpty());
	}

}
