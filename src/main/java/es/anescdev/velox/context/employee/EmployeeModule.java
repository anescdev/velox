package es.anescdev.velox.context.employee;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.app.module.AppModule;
import es.anescdev.velox.context.employee.data.dao.EmployeeDao;
import es.anescdev.velox.core.exceptions.DatabaseConnectionException;

/**
 * @author AnesCDev
 */
@AppModule
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
