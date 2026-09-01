package es.anescdev.velox.context.customer.model.dto;

import java.util.List;

import es.anescdev.velox.context.cod.model.entities.Cod;

/**
 * DTO (record) usado para crear/actualizar un customer desde la interfaz. Implementa
 * {@code ToEntityMapper} para poder convertirse en la entidad persistible sin que el
 * {@code Service} necesite conocer el mapeo concreto.
 */
public record SaveCustomer(List<Cod> codsToAdd) {
    
}
