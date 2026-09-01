package es.anescdev.velox.context.employee.model.dto;

import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.mapper.ToEntityMapper;

/**
 * @author AnesCDev
 */
public record CreateEmployee(String name) implements ToEntityMapper<Employee, Long>{

    @Override
    public Employee mapToEntity() {
        return new Employee(this.name());
    }
    
}
