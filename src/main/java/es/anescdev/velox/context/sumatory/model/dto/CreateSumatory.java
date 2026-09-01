package es.anescdev.velox.context.sumatory.model.dto;

import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.core.mapper.ToEntityMapper;

public record CreateSumatory(byte month, short year, Employee employee) implements ToEntityMapper<Sumatory, Long>{

    @Override
    public Sumatory mapToEntity() {
        return new Sumatory(this.month(), this.year(), this.employee());
    }
}