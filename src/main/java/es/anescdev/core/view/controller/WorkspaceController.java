package es.anescdev.core.view.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import es.anescdev.App;
import es.anescdev.LoadFXMLResult;
import es.anescdev.core.command.CommandInvoker;
import es.anescdev.core.view.TabManager;
import es.anescdev.core.view.components.InformationDialog;
import es.anescdev.sumatory.view.commands.CreateSumatoryCommand;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class WorkspaceController extends BaseController {

	@FXML
	private TabPane workspace;
	private HashMap<String, Tab> workspaceOpenedTabs;
	private final CommandInvoker commandInvoker;
	private final TabManager tabManager;

	/**
	 * @author AnesCDev
	 */
	@Inject
	public WorkspaceController(CommandInvoker commandInvoker, TabManager tabManager) {
		this.commandInvoker = commandInvoker;
		this.tabManager = tabManager;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		this.workspaceOpenedTabs = new HashMap<>();
		this.workspace.setMaxHeight(Double.MAX_VALUE);
		this.tabManager.setOnOpenNewTab(this::openTab);
		this.tabManager.setOnTryOpenExistingTab(idTab -> {
			Platform.runLater(() -> {
				this.workspace.getSelectionModel().select(this.workspaceOpenedTabs.get(idTab));
			});
		});
	}

	@FXML
	private void showAppInfo() {
		new InformationDialog(this.getMessages()).showAndWait();
	}

	@FXML
	private void exitApp() {
		// TODO: Comprobar que no haya elementos modificandose y exigir que se termine
		// de hacer los cambios
		Platform.exit();
	}

	@FXML
	private void openEmployeesList() {
		var data = new HashMap<String, Object>();
		data.put(TabManager.TITLE_KEY, "sheets.employee.title.list");
		data.put(TabManager.SCENE_KEY, "employee/list");
		this.tabManager.openTab("empList", data);
	}

	@FXML
	private void openSumatoryList() {
		var data = new HashMap<String, Object>();
		data.put(TabManager.TITLE_KEY, "sumatory.title.list");
		data.put(TabManager.SCENE_KEY, "sumatory/list");
		this.tabManager.openTab("sumList", data);
	}

	@FXML
	private void createSumatory() {
		this.commandInvoker.executeCommand(new CreateSumatoryCommand());
	}

	private <T extends BaseController> void openTab(String sceneId, Map<String, Object> data) {
		if (data.get(TabManager.TITLE_KEY) instanceof String title
				&& data.get(TabManager.SCENE_KEY) instanceof String scene) {
			Platform.runLater(() -> {
				Tab tab = new Tab(this.getMessage(title));
				tab.setId(sceneId);
				LoadFXMLResult<Node, T> tabScene = App.loadFXML(scene);
				tabScene.controller().initData(data);
				tab.setContent(tabScene.node());
				tab.setOnClosed(event -> {
					this.workspaceOpenedTabs.remove(sceneId);
					this.tabManager.removeTab(((Tab) event.getSource()).getId());
				});
				
				this.workspace.getTabs().add(tab);
				this.workspaceOpenedTabs.put(sceneId, tab);
				this.workspace.getSelectionModel().select(tab);
			});
		}
	}
}
