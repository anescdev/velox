package es.anescdev.velox.context.sumatory.model.dto;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.sumatory.data.key.SumatoryEntryKey;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.core.mapper.ToEntityMapper;


public record CreateSumatoryEntry(Cod cod, Sumatory sumatory, byte day) implements ToEntityMapper<SumatoryEntry, SumatoryEntryKey>{

    @Override
    public SumatoryEntry mapToEntity() {
        return new SumatoryEntry(this.cod(), this.sumatory(), this.day());
    }
}