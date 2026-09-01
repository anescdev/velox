package es.anescdev.velox.core.view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.bus.Bus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Controlador base de JavaFX del que heredan todos los controladores FXML de la
 * aplicación. Da acceso cómodo a los mensajes i18n ({@link #getMessage(String)}) y define
 * el punto de entrada {@link #initData(Bus)} por el que cada pantalla recibe los datos que
 * le pasa quien la abrió (a través del {@link es.anescdev.velox.core.bus.Bus}).
 * {@link #canExit()} permite a una pantalla bloquear su cierre (p. ej. si hay cambios sin guardar).
 */
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
