package es.anescdev.sumatory.persistence.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.sumatory.model.Sumatory;

/**
 * @author AnesCDev
 */
public class SumatoryDao extends BaseDaoImpl<Sumatory, Long>{

    public SumatoryDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Sumatory.class);
    }

}
