package es.anescdev.velox.context.customer.view.viewmodel;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.customer.model.dto.CreateCustomer;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.customer.model.services.CustomerService;

import es.anescdev.velox.core.view.viewmodel.AbstractViewModel;


/**
 * @author AnesCDev
 */
@Singleton
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
