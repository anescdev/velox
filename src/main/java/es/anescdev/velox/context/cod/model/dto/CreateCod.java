package es.anescdev.velox.context.cod.model.dto;

import java.time.Duration;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.model.entities.CodKey;
import es.anescdev.velox.core.mapper.ToEntityMapper;

/**
 * @author AnesCDev
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
