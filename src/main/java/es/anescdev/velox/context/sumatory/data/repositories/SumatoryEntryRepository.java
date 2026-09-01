package es.anescdev.velox.context.sumatory.data.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.sumatory.data.dao.SumatoryEntryDao;
import es.anescdev.velox.context.sumatory.data.key.SumatoryEntryKey;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.core.data.repositories.ORMLiteCompositeKeyAbstractRepository;

/**
 * @author AnesCDev
 */
@Singleton
public class SumatoryEntryRepository
        extends ORMLiteCompositeKeyAbstractRepository<SumatoryEntry, Long, SumatoryEntryKey, SumatoryEntryDao> {
    private final String[] compositeIdColumns = new String[] {
            SumatoryEntryKey.COD_COLUMN,
            SumatoryEntryKey.SUMATORY_COLUMN,
            SumatoryEntryKey.DAY_COLUMN };
    private final List<String> searchExcludedColumns = List.of(SumatoryEntryKey.DAY_COLUMN, SumatoryEntryKey.COD_COLUMN);

    @Inject
    public SumatoryEntryRepository(SumatoryEntryDao dao) {
        super(dao);
    }

    @Override
    protected Class<SumatoryEntry> getEntityClass() {
        return SumatoryEntry.class;
    }

    @Override
    protected String[] getCompositeIdColumns() {
        return this.compositeIdColumns;
    }

    @Override
    protected List<String> excludedFromSearch() {
        return this.searchExcludedColumns;
    }

    @Override
    protected String getInternalIdColumn() {
        return SumatoryEntryKey.INTERNAL_ID_COLUMN;
    }
}
