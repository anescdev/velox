package es.anescdev.velox.context.cod.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.core.data.DaoInitializable;

@DaoInitializable
/**
 * DAO de ORMLite para Cod. Anotado con {@code @DaoInitializable}
 * para que {@code DataBaseLoader} cree su tabla automáticamente en el arranque.
 * No añade lógica propia: toda la lógica de acceso a datos vive en el
 * {@code Repository} correspondiente, que envuelve este DAO.
 */
public class CodDao extends BaseDaoImpl<Cod, Long>{

    public CodDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Cod.class);
    }
    
}
