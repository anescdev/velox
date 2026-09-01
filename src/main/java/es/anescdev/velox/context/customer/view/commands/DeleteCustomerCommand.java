package es.anescdev.velox.context.customer.view.commands;

import java.util.List;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.customer.view.viewmodel.CustomerViewModel;
import es.anescdev.velox.core.command.FeatherCommand;
import es.anescdev.velox.core.view.components.ConfirmationDialog;
import javafx.scene.control.ButtonType;

/**
 * @author AnesCDev
 */
public class DeleteCustomerCommand extends FeatherCommand<Void> {

    @Inject
    private CustomerViewModel viewModel;

    private final List<Customer> toRemove;

    /**
     * @param viewModel
     */
    public DeleteCustomerCommand(List<Customer> toRemove) {
        super();
        this.toRemove = toRemove;
    }

    @Override
    /**
     * Los elementos a borrar deberán de ser incluidos en una lista, guardados en la
     * clave "toDelete"
     */
    public Void executeCommand() {
        if (toRemove.size() == 0)
            throw new IllegalArgumentException("The selected customer cannot be empty");
        var alert = new ConfirmationDialog(App.instance().getMessage("customer.dialog.remove.title"),
                App.instance().getMessage(this.toRemove.size() > 1 ? 
                    "customer.dialog.remove.text.multiple" : 
                    "customer.dialog.remove.text"));
        alert.showAndWait().ifPresent(button -> {
            if (ButtonType.YES == button) {
                this.viewModel.deleteEntities(toRemove);
            }
        });
        return null;
    }

}
