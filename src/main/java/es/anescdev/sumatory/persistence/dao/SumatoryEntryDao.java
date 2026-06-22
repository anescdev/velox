package es.anescdev.sumatory.persistence.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.sumatory.model.SumatoryEntry;

/**
 * @author AnesCDev
 */
public class SumatoryEntryDao extends BaseDaoImpl<SumatoryEntry, Long> {

    public SumatoryEntryDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SumatoryEntry.class);
    }

}
