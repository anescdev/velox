package es.anescdev.velox.context.sumatory.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.core.data.DaoInitializable;

/**
 * @author AnesCDev
 */
@DaoInitializable
public class SumatoryEntryDao extends BaseDaoImpl<SumatoryEntry, Long>{

    public SumatoryEntryDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SumatoryEntry.class);
    }
    
}
