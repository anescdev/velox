package es.anescdev.velox.context.customer.model.dto;

import java.util.List;

import es.anescdev.velox.context.cod.model.entities.Cod;

/**
 * @author AnesCDev
 */
public record SaveCustomer(List<Cod> codsToAdd) {
    
}
