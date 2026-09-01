package es.anescdev.velox.context.customer.data.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.customer.data.dao.CustomerDao;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.core.data.repositories.ORMLiteAbstractRepository;

/**
 * @author AnesCDev
 */
@Singleton
public class CustomerRepository extends ORMLiteAbstractRepository<Customer, Long, CustomerDao> {

    @Inject
    public CustomerRepository(CustomerDao dao) {
        super(dao);
    }

    @Override
    protected Class<Customer> getEntityClass() {
        return Customer.class;
    }

    @Override
    protected String getIdColumn() {
        return Customer.ID_COLUMN;
    }

}
