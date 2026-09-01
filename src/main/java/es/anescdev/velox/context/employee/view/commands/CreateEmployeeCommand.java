package es.anescdev.velox.context.employee.view.commands;

import java.util.Optional;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.employee.model.dto.CreateEmployee;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.employee.view.viewmodel.EmployeeViewModel;
import es.anescdev.velox.core.command.FeatherCommand;
import es.anescdev.velox.core.view.components.CustomTextInputDialog;

import javafx.application.Platform;
import javafx.scene.control.SelectionModel;


/**
 * @author AnesCDev
 */
public class CreateEmployeeCommand extends FeatherCommand<Void> {

    @Inject
    private EmployeeViewModel viewModel;

    private final SelectionModel<Employee> selectionModel;

    /**
     * @param selectionModel
     */
    public CreateEmployeeCommand(SelectionModel<Employee> selectionModel) {
        this.selectionModel = selectionModel;
    }

    @Override
    public Void executeCommand() {
        var createEmployeeData = this.askCreateEmployeeData();
        if (createEmployeeData.isEmpty())
            return null;
        this.viewModel.createEmployee(createEmployeeData.get())
                .thenAccept(employee -> {
                    this.viewModel.employees.add(employee);
                    Platform.runLater(() -> {
                        this.selectionModel.select(employee);
                    });
                });
        return null;
    }

    private Optional<CreateEmployee> askCreateEmployeeData() {
        CustomTextInputDialog dialog = new CustomTextInputDialog(
                App.instance().getMessage("employee.dialog.create.title"),
                App.instance().getMessage("employee.dialog.create.header"),
                "");

        return dialog.showAndWait().map(name -> new CreateEmployee(name));
    }

}
