package es.anescdev.velox.context.cod.model.dto;

import java.time.Duration;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.model.entities.CodKey;
import es.anescdev.velox.core.mapper.ToEntityMapper;

/**
 * DTO (record) usado para crear/actualizar un cod desde la interfaz. Implementa
 * {@code ToEntityMapper} para poder convertirse en la entidad persistible sin que el
 * {@code Service} necesite conocer el mapeo concreto.
 */
public record CreateCod(CodKey codKey, String description, Duration timeWorked) implements ToEntityMapper<Cod, CodKey> {

    @Override
    public Cod mapToEntity() {
        return Cod.draftCod(
                null,
                this.codKey.cod(),
                this.codKey.customer(),
                this.codKey.employee(),
                this.description(),
                this.timeWorked());
    }

}
