/**
 * @author AnesCDev
 */
package es.anescdev;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.jdbc.JdbcSingleConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.shared.domain.exceptions.DatabaseConnectionException;
import es.anescdev.shared.domain.exceptions.InvalidApplicationDirectoryException;
import es.anescdev.shared.domain.providers.ConfigurationProvider;
import es.anescdev.shared.domain.providers.ConfigurationProvider.ConfigurationKey;
import es.anescdev.shared.domain.providers.DatabaseConnectionProvider;
import es.anescdev.shared.infrastructure.providers.InMemoryConfigurationProvider;
import es.anescdev.shared.infrastructure.providers.SQLiteDatabaseConnectionProvider;

import javafx.application.Application.Parameters;

/**
 * @author AnesCDev
 */
public class AppModule {

    public final Parameters args;

    public AppModule(Parameters args) {
        this.args = args;
    }

    @Provides
    @Singleton
    public ConfigurationProvider provideConfiguration() {
        InMemoryConfigurationProvider config = new InMemoryConfigurationProvider();
        this.includeInitialConfiguration(config);
        return config;
    }

    @Provides
    @Singleton
    public DatabaseConnectionProvider provideDatabaseConnection(ConfigurationProvider configurationProvider) {
        return new SQLiteDatabaseConnectionProvider(configurationProvider);
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

    private void includeInitialConfiguration(InMemoryConfigurationProvider configurationService) {
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
