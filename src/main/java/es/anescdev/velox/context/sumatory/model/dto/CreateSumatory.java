package es.anescdev.velox.context.sumatory.model.dto;

import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.core.mapper.ToEntityMapper;

/**
 * DTO (record) usado para crear/actualizar un sumatory desde la interfaz. Implementa
 * {@code ToEntityMapper} para poder convertirse en la entidad persistible sin que el
 * {@code Service} necesite conocer el mapeo concreto.
 */
public record CreateSumatory(byte month, short year, Employee employee) implements ToEntityMapper<Sumatory, Long>{

    @Override
    public Sumatory mapToEntity() {
        return new Sumatory(this.month(), this.year(), this.employee());
    }
}