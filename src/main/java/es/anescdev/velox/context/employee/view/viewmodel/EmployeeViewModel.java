package es.anescdev.velox.context.employee.view.viewmodel;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.employee.model.dto.CreateEmployee;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.employee.model.service.EmployeeService;
import es.anescdev.velox.core.exceptions.CreateEntityException;
import es.anescdev.velox.core.utils.Reseteable;
import es.anescdev.velox.core.view.utils.NotificationsUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

@Singleton
/**
 * ViewModel del dominio employee, usado por los controladores para bindear listas
 * observables a la UI (tablas, combos...) sin acceder directamente al {@code Service}.
 * Hereda de {@code AbstractViewModel} la búsqueda paginada, creación y borrado.
 */
public class EmployeeViewModel implements Reseteable {
    public final SimpleListProperty<Employee> employees = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final EmployeeService employeeService;

    /**
     * @param employeeService
     */
    @Inject
    public EmployeeViewModel(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public void searchEmployees() {
        Thread.startVirtualThread(() -> {
            var result = this.employeeService.searchEntity(-1L, 100);
            Platform.runLater(() -> {
                this.employees.addAll(result.entities());
            });
        });
    }

    public CompletableFuture<Employee> createEmployee(CreateEmployee employeeDTO) {
        CompletableFuture<Employee> employee = new CompletableFuture<>();
        Thread.startVirtualThread(() -> {
            var result = this.employeeService.createEntity(employeeDTO);
            if (result.success()) {
                employee.complete(result.entity());
                NotificationsUtils.showInformation(
                        App.instance().getMessage(result.message().get()),
                        App.instance()
                                .getMessage("notifications.entity.confirmation.created"));
            } else {
                employee.completeExceptionally(new CreateEntityException(result.message().get()));
            }
        });
        return employee;
    }

    public CompletableFuture<Boolean> deleteEmployee(Employee employee) {
        var completableFuture = new CompletableFuture<Boolean>();
        Thread.startVirtualThread(() -> {
            var result = this.employeeService.deleteEntity(employee);
            if (result.isSuccess()) {
                Platform.runLater(() -> this.employees.remove(employee));
                NotificationsUtils.showInformation(
                        App.instance().getMessage("notifications.entity.confirmation.deleted"),
                        App.instance().getMessage("notifications.employee.delete"));
                completableFuture.complete(true);
            } else
                completableFuture.complete(false);
        });
        return completableFuture;
    }

    @Override
    public void reset() {
        this.employees.clear();
        App.instance().getLogger().info("Reseted " + this.getClass().getSimpleName());
    }

    @Override
    public boolean canReset() {
        return true;
    }
}
