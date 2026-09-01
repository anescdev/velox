package es.anescdev.velox.context.cod;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.app.module.AppModule;
import es.anescdev.velox.context.cod.data.dao.CodDao;
import es.anescdev.velox.core.exceptions.DatabaseConnectionException;

/**
 * @author AnesCDev
 */
@AppModule
public class CodModule {
    @Singleton
    @Provides
    CodDao providesCodDao(ConnectionSource connectionSource) throws DatabaseConnectionException {
        try {
            return new CodDao(connectionSource);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Error during database connection or DAO creation. Error: " + e.getMessage());
        }
    }
}
