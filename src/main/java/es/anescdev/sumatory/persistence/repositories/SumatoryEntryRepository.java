package es.anescdev.sumatory.persistence.repositories;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import es.anescdev.core.exceptions.SearchEntityException;
import es.anescdev.core.persistence.repositories.ORMLiteAbstractRepository;
import es.anescdev.sumatory.model.Sumatory;
import es.anescdev.sumatory.model.SumatoryEntry;
import es.anescdev.sumatory.persistence.dao.SumatoryEntryDao;

public class SumatoryEntryRepository extends ORMLiteAbstractRepository<SumatoryEntry, Long, SumatoryEntryDao> {

    @Inject
    public SumatoryEntryRepository(SumatoryEntryDao dao) {
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

    public List<SumatoryEntry> searchAllEntriesOf(Sumatory sumatory, int lastSeenId, int limit)
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
            return queryBuilder.query();
        } catch (SQLException e) {
            throw new SearchEntityException("Error searching the entities. Error: " + e.getMessage());
        }
    }
}
