package es.anescdev.workspace.view.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import es.anescdev.App;
import es.anescdev.shared.BaseController;
import es.anescdev.workspace.view.components.InformationDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class WorkspaceController extends BaseController{
	
	@FXML
	private TabPane workspace;
	private HashMap <String, Tab> workspaceOpenedTabs;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		this.workspaceOpenedTabs = new HashMap<>();
	}

	@FXML
	private void showAppInfo() {
		new InformationDialog(this.getMessages()).showAndWait();
	}
	
	@FXML
	private void exitApp() {
		//TODO: Comprobar que no haya elementos modificandose y exigir que se termine de hacer los cambios
		Platform.exit();
	}
	
	@FXML
	private void openEmployeesList() {
		this.openTab("employee/list", "sheets.employee.title.list");
	}
	
	@FXML
	private void openSumatoryList() {
		this.openTab("sumatory/list", "sumatory.title.list");
	}

	/**
	 * Abre un nodo de JavaFX contenido en un archivo FXML en una pestaña del espacio de trabajo. Si esta ya está abierta, no abrirá otra más.
	 * @param sceneName Nombre del archivo FXML. Si está en alguna subcarpeta de resources/scenes, debe de llamarlo así: carpeta/nombre_fxml. Sin extensión
	 * @param title
	 */
	private void openTab(String sceneName, String title) {
		if(this.hasBeenOpened(sceneName)) {
			this.workspace.getSelectionModel().select(this.workspaceOpenedTabs.get(sceneName));
			return;
		}
		Tab tab = new Tab(this.getMessages().getString(title));
		tab.setId(sceneName);
		tab.setContent(App.loadFXML(sceneName));
		tab.setOnClosed(event -> {
			this.workspaceOpenedTabs.remove(sceneName);
		});
		this.workspace.getTabs().add(tab);
		this.workspaceOpenedTabs.put(sceneName, tab);
		this.workspace.getSelectionModel().select(tab);
	}
	
	/**
	 * Compruena si hay abierta una pestaña con un ID específico
	 * @param tabId Id de la pestaña
	 * @return true en caso de haber una pestaña con ese ID abierta, false si no
	 */
	private boolean hasBeenOpened(String tabId) {
		return this.workspaceOpenedTabs.containsKey(tabId);
	}
}
