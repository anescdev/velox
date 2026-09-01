package es.anescdev.velox.context.cod;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.app.module.AppModule;
import es.anescdev.velox.context.cod.data.dao.CodDao;
import es.anescdev.velox.core.exceptions.DatabaseConnectionException;

@AppModule
/**
 * Módulo de Feather del dominio Código de trabajo (Cod). Detectado automáticamente por
 * {@link es.anescdev.velox.app.App#searchAppModules()} gracias a {@code @AppModule}.
 * Provee el DAO de {@code Cod}, que usa clave compuesta (empleado + cliente + código).
 */
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
