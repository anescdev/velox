package es.anescdev.velox.context.customer.model.dto;

import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.core.mapper.ToEntityMapper;

/**
 * DTO (record) usado para crear/actualizar un customer desde la interfaz. Implementa
 * {@code ToEntityMapper} para poder convertirse en la entidad persistible sin que el
 * {@code Service} necesite conocer el mapeo concreto.
 */
public record CreateCustomer(String name, String abbreviation) implements ToEntityMapper<Customer, Long>
{

    @Override
    public Customer mapToEntity() {
        return new Customer(this.name(), this.abbreviation());
    }
    
}
