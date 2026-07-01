package es.anescdev.core.view.controller;

import java.net.URL;
import java.util.Map;
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
	protected String getMessage(String key) {
		try {
			return this.resources.getString(key);
		} catch (Exception e) {
			return key;
		}
	}

	public void initData(Map<String, Object> data){
		
	}

	public boolean canExit() {
		return true;
	}

}
