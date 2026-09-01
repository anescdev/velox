package es.anescdev.velox.context.customer.view.commands;

import java.util.Optional;
import java.util.function.Consumer;

import javax.inject.Inject;

import es.anescdev.velox.context.customer.model.dto.CreateCustomer;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.customer.view.components.CreateCustomerDialog;
import es.anescdev.velox.context.customer.view.utils.CustomerUtils;
import es.anescdev.velox.context.customer.view.viewmodel.CustomerViewModel;
import es.anescdev.velox.core.command.FeatherCommand;
import es.anescdev.velox.core.view.TabManager;


/**
 * Comando de la capa de vista del dominio customer: encapsula una acción disparada desde
 * la interfaz (ver {@code Command}/{@code FeatherCommand}), coordinando el ViewModel,
 * los diálogos y la navegación necesarios para completarla.
 */
public class CreateCustomerCommand extends FeatherCommand<Void> {

    @Inject
    private CustomerViewModel customerViewModel;
    @Inject
    private TabManager tabManagerService;

    private final Consumer<Customer> onCreated;

        public CreateCustomerCommand(Consumer<Customer> onCreated) {
        super();
        this.onCreated = onCreated;
    }
    public CreateCustomerCommand() {
        this(null);
    }

    @Override
    public Void executeCommand() {
        Optional<CreateCustomer> data = new CreateCustomerDialog().showAndWait();
        if (data.isEmpty())
            return null;
        customerViewModel.createEntity(data.get())
                .thenAccept(customer -> {
                    this.customerViewModel.searchEntities();
                    if(onCreated != null) this.onCreated.accept(customer);
                    CustomerUtils.openCustomerDetails(tabManagerService, customer);
                });
        return null;
    }

}
