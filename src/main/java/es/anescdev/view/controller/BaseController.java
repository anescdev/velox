package es.anescdev.view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public abstract class BaseController implements Initializable{
	
	private ResourceBundle resources;

	@FXML
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
	}
	
	protected ResourceBundle getMessages() {
		return this.resources;
	}

}
