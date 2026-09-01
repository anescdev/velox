package es.anescdev.velox.core.view.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.codejargon.feather.Key;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.employee.view.controller.EmployeeSelectorController;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.command.FeatherCommand;
import es.anescdev.velox.core.utils.Reseteable;
import es.anescdev.velox.core.view.components.ConfirmationDialog;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;

/**
 * Comando que cierra la sesión del empleado actual y vuelve a la pantalla de selección.
 * Antes de hacerlo:
 * <ol>
 *   <li>Busca, por reflexión sobre el mapa interno de singletons de Feather, todas las
 *       instancias activas que implementan {@link es.anescdev.velox.core.utils.Reseteable}
 *       (típicamente los {@code ViewModel}).</li>
 *   <li>Si alguna indica que no se puede resetear todavía ({@code canReset() == false},
 *       p. ej. por cambios sin guardar), pide confirmación al usuario antes de continuar.</li>
 *   <li>Llama a {@code reset()} en todas ellas y vacía el {@link es.anescdev.velox.core.bus.Bus}.</li>
 * </ol>
 * <b>Nota:</b> el acceso al campo privado {@code singletons} de Feather vía reflexión es
 * un acoplamiento a un detalle interno de la librería; si Feather cambia su implementación
 * este método podría dejar de encontrar los singletons.
 */
public class BackToEmployeeSelectorCommand extends FeatherCommand<Void> {
    private final Scene mainScene;
    private final Employee selectedEmployee;

    @Inject
    private Bus bus;

    public BackToEmployeeSelectorCommand(Scene mainScene, Employee selectedEmployee) {
        this.mainScene = mainScene;
        this.selectedEmployee = selectedEmployee;
    }

    @Override
    public Void executeCommand() {
        var reseteables = this.getAllReseteablesViewModels();
        if (reseteables == null)
            return null;
        reseteables.forEach(reseteable -> reseteable.reset());
        var employeeSelector = App.<BorderPane, EmployeeSelectorController>loadFXML("employee/selector");
        var data = new Bus();
        data.setData(EmployeeSelectorController.SCENE_KEY, mainScene);
        data.setData(EmployeeSelectorController.INITIAL_EMPLOYEE_KEY, this.selectedEmployee);
        bus.clearBus();
        employeeSelector.controller().initData(data);
        this.mainScene.setRoot(employeeSelector.node());
        return null;
    }

    private List<Reseteable> getAllReseteablesViewModels() {
        List<Reseteable> reseteables = new LinkedList<>();
        try {
            var singletonInstancesField = App.instance().getFeather().getClass().getDeclaredField("singletons");
            singletonInstancesField.setAccessible(true);
            var singletonInstanceMap = (Map<Key<?>, Object>) singletonInstancesField.get(App.instance().getFeather());
            singletonInstancesField.setAccessible(false);
            for (Object instance : singletonInstanceMap.values()) {
                if (instance instanceof Reseteable reseteable) {
                    if (!reseteable.canReset()) {
                        ConfirmationDialog confirmation = new ConfirmationDialog(
                                App.instance().getMessage("main.dialog.changeemployee.title"),
                                App.instance().getMessage("main.dialog.changeemployee.message"));
                        if (confirmation.showAndWait().filter(buttonType -> buttonType == ButtonType.NO).isPresent())
                            return null;
                    }
                    reseteables.add(reseteable);
                }
            }
            return reseteables;
        } catch (NoSuchFieldException e) {
            App.instance().getLogger().severe("We cant find the map of instances to search reseteables");
        } catch (IllegalArgumentException e) {
            App.instance().getLogger().severe("We cant find the singleton instances map in the object");
        } catch (IllegalAccessException e) {
            App.instance().getLogger().severe("Ensure you can access to the field data");
        }
        return reseteables;

    }
}
