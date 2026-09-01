package es.anescdev.velox.context.customer.data.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.customer.data.dao.CustomerDao;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.core.data.repositories.ORMLiteAbstractRepository;

@Singleton
/**
 * Repositorio del dominio customer: implementación concreta del CRUD genérico
 * (crear, actualizar, borrar, buscar paginado, contar) delegando en el DAO de ORMLite
 * inyectado. No suele contener lógica propia salvo indicar la columna de id y la clase
 * de entidad al repositorio abstracto del que hereda.
 */
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
