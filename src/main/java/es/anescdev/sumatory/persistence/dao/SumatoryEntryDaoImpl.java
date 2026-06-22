package es.anescdev.sumatory.persistence.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.sumatory.persistence.entities.SumatoryEntry;

/**
 * @author AnesCDev
 */
public class SumatoryEntryDaoImpl extends BaseDaoImpl<SumatoryEntry, Long> implements SumatoryEntryDao {

    public SumatoryEntryDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SumatoryEntry.class);
    }

}
