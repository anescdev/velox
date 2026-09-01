package es.anescdev.velox.core.view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.bus.Bus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public abstract class BaseController implements Initializable {

	private ResourceBundle resources;

	@FXML
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
	}

	protected ResourceBundle getMessages() {
		return this.resources;
	}

	protected String getMessage(String key) {
		return App.instance().getMessage(key);
	}

	public void initData(Bus busData) {

	}

	public boolean canExit() {
		return true;
	}

}
