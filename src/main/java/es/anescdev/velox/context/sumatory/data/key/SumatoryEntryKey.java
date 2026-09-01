package es.anescdev.velox.context.sumatory.data.key;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.core.data.entities.CompositeKey;
import es.anescdev.velox.core.exceptions.NonExistentKeyException;

/**
 * @author AnesCDev
 */
public record SumatoryEntryKey(Long internalId, Cod cod, Sumatory sumatory, byte day) implements CompositeKey<Long> {
    public static final String INTERNAL_ID_COLUMN = "sumentry_id",
            COD_COLUMN = "sumentry_cod_id",
            SUMATORY_COLUMN = "sumentry_sumatory_id",
            DAY_COLUMN = "sumentry_day";

    @Override
    public Object getColumnValue(String columnName) {
        switch (columnName) {
            case COD_COLUMN:
                return this.cod();
            case SUMATORY_COLUMN:
                return this.sumatory();
            case DAY_COLUMN:
                return this.day();
            default:
                throw new NonExistentKeyException(columnName);
        }
    }

    @Override
    public Long getInternalId() {
        return this.internalId();
    }

}
