package es.anescdev.velox.context.employee.view.commands;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.employee.view.viewmodel.EmployeeViewModel;
import es.anescdev.velox.core.command.FeatherCommand;
import es.anescdev.velox.core.view.components.ConfirmationDialog;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

/**
 * Comando de la capa de vista del dominio employee: encapsula una acción disparada desde
 * la interfaz (ver {@code Command}/{@code FeatherCommand}), coordinando el ViewModel,
 * los diálogos y la navegación necesarios para completarla.
 */
public class DeleteEmployeeCommand extends FeatherCommand<Void> {

    private final Employee employee;
    private final ComboBox<Employee> combo;

    @Inject
    private EmployeeViewModel viewModel;

    /**
     * @param employee
     */
    public DeleteEmployeeCommand(Employee employee, ComboBox<Employee> combo) {
        this.employee = employee;
        this.combo = combo;
    }

    @Override
    public Void executeCommand() {
        if (this.employee == null)
            return null;
        var res = new ConfirmationDialog(
                App.instance().getMessage("employee.dialog.remove.title"),
                App.instance().getMessage("employee.dialog.remove.message"));
        if(res.showAndWait().filter(button -> button.equals(ButtonType.YES)).isEmpty()) return null;
        this.viewModel.deleteEmployee(employee).thenAccept(result -> {
            if (result)
                Platform.runLater(() -> {
                    combo.getSelectionModel().selectFirst();
                });
        });
        return null;
    }

}
