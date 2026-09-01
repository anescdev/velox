package es.anescdev.velox.context.sumatory.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.model.entities.CodKey;
import es.anescdev.velox.context.sumatory.data.dao.SumatoryDao;
import es.anescdev.velox.context.sumatory.data.key.SumatoryEntryKey;
import es.anescdev.velox.core.data.entities.Identificable;
import es.anescdev.velox.core.model.DataEqualable;
import es.anescdev.velox.core.model.EntityState;
import es.anescdev.velox.core.model.StateableEntity;
import es.anescdev.velox.core.utils.LombokableBuilder;
import javafx.util.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode.Include;

/**
 * @author AnesCDev
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@lombok.Builder()
@DatabaseTable(tableName = "sumentry", daoClass = SumatoryDao.class)
public class SumatoryEntry
        extends StateableEntity
        implements Identificable<SumatoryEntryKey>, DataEqualable<SumatoryEntry>, LombokableBuilder<SumatoryEntry> {

    @DatabaseField(generatedId = true, columnName = SumatoryEntryKey.INTERNAL_ID_COLUMN)
    @Include
    private Long internalId;

    @DatabaseField(
        columnDefinition = "INTEGER REFERENCES cod(" + CodKey.INTERNAL_ID_COLUMN + ") ON DELETE CASCADE",
        uniqueCombo = true, 
        foreign = true, 
        foreignAutoRefresh = true, 
        columnName = SumatoryEntryKey.COD_COLUMN)
    private Cod cod;

    @DatabaseField(columnDefinition = "INTEGER REFERENCES sumatory(" + Sumatory.ID_COLUMN + ") ON DELETE CASCADE",
        uniqueCombo = true, 
        foreign = true, 
        columnName = SumatoryEntryKey.SUMATORY_COLUMN,
        index = true)
    private Sumatory sumatory;

    @DatabaseField(uniqueCombo = true, columnName = SumatoryEntryKey.DAY_COLUMN)
    private byte day;

    public SumatoryEntry(Long internalId, Cod cod, Sumatory sumatory, byte day, EntityState state) {
        super(state);
        this.internalId = internalId;
        this.cod = cod;
        this.sumatory = sumatory;
        this.day = day;
    }

    public SumatoryEntry(Long internalId, Cod cod, Sumatory sumatory, byte day) {
        this(internalId, cod, sumatory, day, EntityState.SAVED);
    }

    public SumatoryEntry(Cod cod, Sumatory sumatory, byte day) {
        this(null, cod, sumatory, day);
    }

    public SumatoryEntry() {
        super(EntityState.SAVED);
    }

    public static SumatoryEntry draft(Long internalId, Cod cod, Sumatory sumatory, byte day) {
        return new SumatoryEntry(internalId, cod, sumatory, day, EntityState.DRAFT);
    }

    @Override
    public boolean isDataEqual(SumatoryEntry other) {
        return (this.cod == null ? this.cod == other.getCod() : this.cod.isDataEqual(other.getCod())) &&
                (this.getSumatory() == null ? this.getSumatory() == other.getSumatory()
                        : this.getSumatory().isDataEqual(other.getSumatory()))
                && this.getDay() == other.getDay();
    }

    @Override
    public SumatoryEntryKey getId() {
        return new SumatoryEntryKey(this.internalId, this.cod, this.sumatory, this.day);
    }

    @Override
    public Builder<SumatoryEntry> toBuilder() {
        return SumatoryEntry.builder().internalId(internalId).cod(cod).sumatory(sumatory).day(day);
    }

    public static class SumatoryEntryBuilder implements Builder<SumatoryEntry> {

    }
}
