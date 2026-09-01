package es.anescdev.velox.context.customer.model.dto;

import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.core.mapper.ToEntityMapper;

/**
 * @author AnesCDev
 */
public record CreateCustomer(String name, String abbreviation) implements ToEntityMapper<Customer, Long>
{

    @Override
    public Customer mapToEntity() {
        return new Customer(this.name(), this.abbreviation());
    }
    
}
