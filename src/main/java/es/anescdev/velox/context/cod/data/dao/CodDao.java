package es.anescdev.velox.context.cod.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.core.data.DaoInitializable;

/**
 * @author AnesCDev
 */
@DaoInitializable
public class CodDao extends BaseDaoImpl<Cod, Long>{

    public CodDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Cod.class);
    }
    
}
