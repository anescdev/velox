package es.anescdev.velox.context.employee.view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.employee.view.commands.CreateEmployeeCommand;
import es.anescdev.velox.context.employee.view.commands.DeleteEmployeeCommand;
import es.anescdev.velox.context.employee.view.commands.GoToWorkspaceCommand;
import es.anescdev.velox.context.employee.view.utils.EmployeeStringConverter;
import es.anescdev.velox.context.employee.view.viewmodel.EmployeeViewModel;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.bus.BusKey;
import es.anescdev.velox.core.command.CommandInvoker;
import es.anescdev.velox.core.view.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 * Controlador FXML del dominio employee. Conecta los componentes definidos en el FXML
 * asociado con el ViewModel y los comandos del dominio, siguiendo el patrón de
 * {@code BaseController}/{@code BaseViewTabController}.
 */
public class EmployeeSelectorController extends BaseController {

    public static final BusKey<Scene> SCENE_KEY = new BusKey<>("scene", Scene.class);
    public static final BusKey<Employee> INITIAL_EMPLOYEE_KEY = new BusKey<>("intialEmployee", Employee.class);

    @FXML
    private ComboBox<Employee> employeeCombo;

    @FXML
    private Button deleteButton;

    @FXML
    private Button selectButton;

    private final EmployeeViewModel viewModel;
    private final CommandInvoker commandInvoker;
    private Scene mainScene;

    /**
     * @param viewModel
     */
    @Inject
    public EmployeeSelectorController(EmployeeViewModel viewModel, CommandInvoker commandInvoker) {
        this.viewModel = viewModel;
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.employeeCombo.itemsProperty().bind(this.viewModel.employees);
        this.employeeCombo.getEditor().setVisible(true);
        this.employeeCombo.setConverter(new EmployeeStringConverter());
        var buttonDisabledCondition = this.employeeCombo.valueProperty().isNull();
        this.selectButton.disableProperty().bind(buttonDisabledCondition);
        this.deleteButton.disableProperty().bind(buttonDisabledCondition);
        this.viewModel.searchEmployees();
    }

    @Override
    public void initData(Bus data) {
        var scene = data.getFromBus(SCENE_KEY);
        if (scene.isPresent())
            this.mainScene = scene.get();
        var initialEmployee = data.getFromBus(INITIAL_EMPLOYEE_KEY);
        if (initialEmployee.isPresent())
            this.employeeCombo.getSelectionModel().select(initialEmployee.get());
        App.instance().setTitle(this.getMessage("employee.title"));
    }

    @FXML
    public void createEmployee() {
        this.commandInvoker.executeCommand(new CreateEmployeeCommand(this.employeeCombo.getSelectionModel()));
    }

    @FXML
    public void selectEmployee() {
        this.commandInvoker.executeCommand(new GoToWorkspaceCommand(mainScene, employeeCombo.getValue()));
    }

    @FXML
    public void deleteEmployee() {
        this.commandInvoker.executeCommand(new DeleteEmployeeCommand(this.employeeCombo.getValue(), this.employeeCombo));
    }

}
