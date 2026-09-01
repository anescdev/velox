package es.anescdev.velox.context.customer.model.services;

import java.util.function.BiConsumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.customer.data.repository.CustomerRepository;
import es.anescdev.velox.context.customer.model.dto.CreateCustomer;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.core.services.AbstractService;
import es.anescdev.velox.core.services.AbstractServiceMessage;

@Singleton
/**
 * Servicio de negocio del dominio customer. Hereda el CRUD genérico de
 * {@code AbstractService} y solo define los mensajes de usuario (éxito/error) propios
 * de este dominio, salvo que añada alguna regla de negocio adicional.
 */
public class CustomerService extends AbstractService<Customer, Long, CreateCustomer> {
    /**
     * @param repo
     */
    @Inject
    public CustomerService(CustomerRepository repo) {
        super(repo);
    }

    @Override
    protected void initializeMessages(BiConsumer<AbstractServiceMessage, String> includeMessage) {
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY, "notifications.customer.create");
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY_ERROR, "notifications.customer.create.error");
        includeMessage.accept(AbstractServiceMessage.ENTITY_EXISTING_ERROR, "notifications.customer.existing.error");
    }
}
