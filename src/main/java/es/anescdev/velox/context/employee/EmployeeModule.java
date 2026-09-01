package es.anescdev.velox.context.employee;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.app.module.AppModule;
import es.anescdev.velox.context.employee.data.dao.EmployeeDao;
import es.anescdev.velox.core.exceptions.DatabaseConnectionException;

@AppModule
/**
 * Módulo de Feather del dominio Empleado. Detectado automáticamente por
 * {@link es.anescdev.velox.app.App#searchAppModules()} gracias a {@code @AppModule}.
 * Provee el DAO de {@code Employee}, necesario para poder construirlo con la
 * {@code ConnectionSource} de ORMLite en vez de con {@code @Inject} directo.
 */
public class EmployeeModule {

    @Provides
    @Singleton
    EmployeeDao providesEmployeeDao(ConnectionSource connectionSource) throws DatabaseConnectionException {
        try {
            return new EmployeeDao(connectionSource);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Error during database connection or DAO creation. Error: " + e.getMessage());
        }
    }

}
