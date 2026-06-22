package es.anescdev.sumatory.persistence.repositories;

import java.sql.SQLException;

import es.anescdev.shared.domain.exceptions.SearchEntityException;
import es.anescdev.shared.domain.persistence.Page;
import es.anescdev.sumatory.persistence.dao.SumatoryEntryDao;
import es.anescdev.sumatory.persistence.entities.Sumatory;
import es.anescdev.sumatory.persistence.entities.SumatoryEntry;

public class ORMLiteSumatoryEntryRepository extends ORMLiteAbstractRepository<SumatoryEntry, Long, SumatoryEntryDao>
        implements SumatoryEntryRepository {

    public ORMLiteSumatoryEntryRepository(SumatoryEntryDao dao) {
        super(dao);
    }

    @Override
    protected Class<SumatoryEntry> getEntityClass() {
        return SumatoryEntry.class;
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    public Page<SumatoryEntry> searchAllEntriesOf(Sumatory sumatory, int lastSeenId, int limit)
            throws SearchEntityException {
        var queryBuilder = this.dao.queryBuilder();
        try {
            queryBuilder
                    .limit((long) limit)
                    .setWhere(
                            queryBuilder.where()
                                    .gt("id", lastSeenId)
                                    .and()
                                    .eq("sumatory_id", sumatory.getId()));
            return new Page<>(queryBuilder.query(), -1, limit, -1);
        } catch (SQLException e) {
            throw new SearchEntityException("Error searching the entities. Error: " + e.getMessage());
        }
    }
}
