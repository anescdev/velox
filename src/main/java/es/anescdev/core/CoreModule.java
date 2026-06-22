/**
 * @author AnesCDev
 */
package es.anescdev.core;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.jdbc.JdbcSingleConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.core.exceptions.DatabaseConnectionException;
import es.anescdev.core.exceptions.InvalidApplicationDirectoryException;
import es.anescdev.core.providers.ConfigurationProvider;
import es.anescdev.core.providers.DatabaseConnectionProvider;
import es.anescdev.core.providers.ConfigurationProvider.ConfigurationKey;

import javafx.application.Application.Parameters;

/**
 * @author AnesCDev
 */
public class CoreModule {

    public final Parameters args;

    public CoreModule(Parameters args) {
        this.args = args;
    }

    @Provides
    @Singleton
    public ConfigurationProvider provideConfiguration() {
        ConfigurationProvider config = new ConfigurationProvider();
        this.includeInitialConfiguration(config);
        return config;
    }

    @Provides
    @Singleton
    public DatabaseConnectionProvider provideDatabaseConnection(ConfigurationProvider configurationProvider) {
        return new DatabaseConnectionProvider(configurationProvider);
    }

    @Provides
    @Singleton
    public ConnectionSource providesORMLitConnectionSource(DatabaseConnectionProvider databaseConnectionProvider)
            throws DatabaseConnectionException {
        Connection nativeConn = databaseConnectionProvider.getDatabaseConnection();
        try {
            return new JdbcSingleConnectionSource(nativeConn.getMetaData().getURL(), nativeConn);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Cannot connect to the database. Error" + e.getMessage());
        }
    }

    private void includeInitialConfiguration(ConfigurationProvider configurationService) {
        Map<String, String> namedArgs = args.getNamed();
        if (namedArgs.isEmpty() || !namedArgs.containsKey("--app-dir"))
            throw new InvalidApplicationDirectoryException(
                    "You must to pass the app directory argument (--app-dir) in the command line");
        String appDir = namedArgs.get("--app-dir");
        File appDirFile = new File(appDir);
        if (appDirFile.isDirectory() || (appDirFile.canRead() && appDirFile.canWrite()))
            throw new InvalidApplicationDirectoryException("The app directory isn't valid");
        configurationService.set(ConfigurationKey.APP_DIRECTORY, appDir);
        configurationService.set(ConfigurationKey.DATABASE_NAME, namedArgs.getOrDefault("--database-name", "velox"));
    }
}
