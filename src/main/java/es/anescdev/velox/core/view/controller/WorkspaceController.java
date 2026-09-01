package es.anescdev.velox.core.view.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;

import es.anescdev.velox.app.App;
import es.anescdev.velox.app.LoadFXMLResult;
import es.anescdev.velox.context.customer.view.commands.CreateCustomerCommand;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.employee.view.components.EmployeeChip;
import es.anescdev.velox.context.sumatory.view.commands.CreateSumatoryCommand;
import es.anescdev.velox.core.bus.BusKey;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.command.CommandInvoker;
import es.anescdev.velox.core.view.TabManager;
import es.anescdev.velox.core.view.commands.BackToEmployeeSelectorCommand;
import es.anescdev.velox.core.view.components.ConfirmationDialog;
import es.anescdev.velox.core.view.components.InformationDialog;
import es.anescdev.velox.core.view.components.statusbar.CustomStatusBar;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;

public class WorkspaceController extends BaseController {
	public static final BusKey<Employee> EMPLOYEE_KEY = new BusKey<>("employee", Employee.class);

	@FXML
	private TabPane workspace;

	@FXML
	private EmployeeChip employeeChip;

	private HashMap<String, Tab> workspaceOpenedTabs;
	private final ObservableList<BaseController> openedControllerProperty = FXCollections.observableArrayList();
	private final CommandInvoker commandInvoker;
	private final TabManager tabManager;
	private final Bus bus;
	private Employee employee;

	/**
	 * @author AnesCDev
	 */
	@Inject
	public WorkspaceController(CommandInvoker commandInvoker, TabManager tabManager, Bus bus) {
		this.commandInvoker = commandInvoker;
		this.tabManager = tabManager;
		this.bus = bus;
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
		this.tabManager.setOnForceCloseTab(this::onForceClose);
		this.tabManager.setCheckCanClose(() -> {
			var controllers = this.openedControllerProperty;
			return !controllers.stream().map(controller -> controller.canExit()).anyMatch(ctrlCanExit -> ctrlCanExit == false);
		});
	}

	@Override
	public void initData(Bus data) {
		var employee = data.getFromBus(EMPLOYEE_KEY);
		if (employee.isPresent()) {
			this.bus.setData(EMPLOYEE_KEY, employee.get());
			this.employee = employee.get();
			this.employeeChip.setEmployee(employee.get());
		} else
			Platform.exit();
		this.workspace.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		App.instance().setTitle(this.employee.getName());
	}

	@FXML
	private void showAppInfo() {
		new InformationDialog(this.getMessages()).showAndWait();
	}

	@FXML
	private void exitApp() {
		Platform.exit();
	}

	@FXML
	private void changeEmployee() {
		this.commandInvoker.executeCommand(
				new BackToEmployeeSelectorCommand(App.instance().getMainStage().getScene(),
						this.employee));
	}

	@FXML
	private void openSumatoryList() {
		var data = new Bus();
		data.setData(TabManager.TITLE_KEY, "sumatory.title.list");
		data.setData(TabManager.SCENE_KEY, "sumatory/list");
		this.tabManager.openTab("sumList", data);
	}

	@FXML
	private void createSumatory() {
		this.commandInvoker.executeCommand(new CreateSumatoryCommand());
	}

	@FXML
	private void openCustomersList() {
		var data = new Bus();
		data.setData(TabManager.TITLE_KEY, "customer.title.list");
		data.setData(TabManager.SCENE_KEY, "customer/list");
		this.tabManager.openTab("customerList", data);
	}

	@FXML
	private void createCustomer() {
		this.commandInvoker.executeCommand(new CreateCustomerCommand());
	}

	private <T extends BaseController> void openTab(String sceneId, Bus data) {
		var title = data.getFromBus(TabManager.TITLE_KEY);
		var scene = data.getFromBus(TabManager.SCENE_KEY);
		if (title.isPresent() && scene.isPresent()) {
			Platform.runLater(() -> {
				Tab tab = new Tab(this.getMessage(title.get()));
				tab.setId(sceneId);
				LoadFXMLResult<Node, T> tabScene = App.loadFXML(scene.get());
				tabScene.controller().initData(data);
				Button closeButton = new Button();
				closeButton.setGraphic(new FontIcon(UniconsLine.TIMES));
				closeButton.getStyleClass().add("custom-close-tab-button");
				closeButton.setOnAction(ev -> {
					var closeEvent = new Event(tab, tab, Tab.TAB_CLOSE_REQUEST_EVENT);
					Event.fireEvent(tab, closeEvent);
					if (closeEvent.isConsumed())
						return;
					this.workspace.getTabs().remove(tab);
					Event.fireEvent(tab, new Event(Tab.CLOSED_EVENT));
				});
				tab.setOnSelectionChanged(event -> {
					if (tabScene.controller() instanceof StatusBarBaseViewTab statusViewController)
						statusViewController.updateControlUserData();
					else
						CustomStatusBar.mainStatusBar().setUserData(null);
				});
				tab.setGraphic(closeButton);
				tab.setContent(tabScene.node());
				tab.setOnCloseRequest(event -> {
					if (tabScene.controller().canExit())
						return;
					this.workspace.getSelectionModel().select(tab);
					ConfirmationDialog confirmation = new ConfirmationDialog(this.getMessage("main.tab.closeTitle"),
							this.getMessage("main.tab.closeMessage"));
					if (confirmation.showAndWait().filter(button -> button == ButtonType.NO).isPresent()) {
						event.consume();
					}
				});
				tab.setOnClosed(event -> {
					this.workspaceOpenedTabs.remove(sceneId);
					this.tabManager.removeTab(((Tab) event.getSource()).getId());
					this.openedControllerProperty.remove(tabScene.controller());
				});

				this.workspace.getTabs().add(tab);
				this.workspaceOpenedTabs.put(sceneId, tab);
				this.workspace.getSelectionModel().select(tab);
				this.openedControllerProperty.add(tabScene.controller());
			});
		}
	}

	private void onForceClose(String id) {
		this.workspaceOpenedTabs.computeIfPresent(id, (keyId, tab) -> {
			this.workspace.getTabs().remove(tab);
			this.workspace.fireEvent(new Event(Tab.CLOSED_EVENT));
			return null;
		});
	}
}
