package es.anescdev.velox.context.employee.model.dto;

import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.mapper.ToEntityMapper;

/**
 * DTO (record) usado para crear/actualizar un employee desde la interfaz. Implementa
 * {@code ToEntityMapper} para poder convertirse en la entidad persistible sin que el
 * {@code Service} necesite conocer el mapeo concreto.
 */
public record CreateEmployee(String name) implements ToEntityMapper<Employee, Long>{

    @Override
    public Employee mapToEntity() {
        return new Employee(this.name());
    }
    
}
