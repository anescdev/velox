package es.anescdev.velox.context.customer.view.viewmodel;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.customer.model.dto.CreateCustomer;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.customer.model.services.CustomerService;

import es.anescdev.velox.core.view.viewmodel.AbstractViewModel;


@Singleton
/**
 * ViewModel del dominio customer, usado por los controladores para bindear listas
 * observables a la UI (tablas, combos...) sin acceder directamente al {@code Service}.
 * Hereda de {@code AbstractViewModel} la búsqueda paginada, creación y borrado.
 */
public class CustomerViewModel extends AbstractViewModel<Customer, Long, CustomerService, CreateCustomer> {

    @Inject
    public CustomerViewModel(CustomerService service) {
        super(service);
    }

    @Override
    public Long initialLastId() {
        return -1L;
    }
}
