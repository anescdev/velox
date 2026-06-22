package es.anescdev.sumatory.persistence.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.sumatory.persistence.entities.Sumatory;

/**
 * @author AnesCDev
 */
public class SumatoryDaoImpl extends BaseDaoImpl<Sumatory, Long> implements SumatoryDao{

    public SumatoryDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Sumatory.class);
    }

}
