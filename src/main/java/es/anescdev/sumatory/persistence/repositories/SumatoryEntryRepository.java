package es.anescdev.sumatory.persistence.repositories;

import es.anescdev.shared.domain.exceptions.SearchEntityException;
import es.anescdev.shared.domain.persistence.Page;
import es.anescdev.shared.domain.persistence.Repository;
import es.anescdev.sumatory.persistence.entities.Sumatory;
import es.anescdev.sumatory.persistence.entities.SumatoryEntry;

/**
 * @author AnesCDev
 */
public interface SumatoryEntryRepository extends Repository<SumatoryEntry, Long> {
    public Page<SumatoryEntry> searchAllEntriesOf(Sumatory sumatory, int lastSeenId, int limit)
            throws SearchEntityException;
}
