package es.anescdev.velox.core;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.jdbc.JdbcSingleConnectionSource;
import com.j256.ormlite.jdbc.spring.TableCreator;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.exceptions.DatabaseConnectionException;
import es.anescdev.velox.core.exceptions.InvalidApplicationDirectoryException;
import es.anescdev.velox.core.providers.ConfigurationProvider;
import es.anescdev.velox.core.providers.DatabaseConnectionProvider;
import es.anescdev.velox.core.providers.ConfigurationProvider.ConfigurationKey;
import javafx.application.Application.Parameters;

/**
 * Módulo de Feather con la configuración de infraestructura compartida por toda la
 * aplicación (no es un módulo de dominio, por eso no lleva {@code @AppModule}: se añade
 * siempre explícitamente en {@link es.anescdev.velox.app.App#searchAppModules()}).
 * Provee:
 * <ul>
 *   <li>{@link es.anescdev.velox.core.providers.ConfigurationProvider}: configuración
 *       inicial (directorio de la app, nombre de la base de datos), leída de los
 *       argumentos de lanzamiento o con valores por defecto en {@code ~/.velox}.</li>
 *   <li>{@link es.anescdev.velox.core.providers.DatabaseConnectionProvider} y la
 *       {@link com.j256.ormlite.support.ConnectionSource} de ORMLite que usan todos los DAO.</li>
 *   <li>El {@code TableCreator} de ORMLite usado por el loader de base de datos.</li>
 * </ul>
 */
public class CoreModule {

    public final Parameters args;

    public CoreModule(Parameters args) {
        this.args = args;
    }

    public CoreModule() {
        this(App.instance().getParameters());
    }

    @Provides
    @Singleton
    public ConfigurationProvider provideConfiguration() throws IOException {
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

    @Provides
    @Singleton
    TableCreator providesTableCreator(ConnectionSource connectionSource) {
        var tableCreator = new TableCreator();
        tableCreator.setConnectionSource(connectionSource);
        return tableCreator;
    }

    private void includeInitialConfiguration(ConfigurationProvider configurationService) throws IOException {
        Map<String, String> namedArgs = args.getNamed();
        if (namedArgs.containsKey("app-dir")) {
            String appDir = namedArgs.get("app-dir");
            File appDirFile = new File(appDir);
            if (!appDirFile.isDirectory() || !(appDirFile.canRead() && appDirFile.canWrite()))
                throw new InvalidApplicationDirectoryException("The app directory isn't valid");
            configurationService.set(ConfigurationKey.APP_DIRECTORY, appDir);
        } else {
            File appDirFile = new File(System.getProperty("user.home"), ".velox");
            if (!appDirFile.exists() && !appDirFile.mkdir())
                throw new IOException("Cannot create the app directory on " + appDirFile.getAbsolutePath());
            configurationService.set(ConfigurationKey.APP_DIRECTORY, appDirFile.getAbsolutePath());
        }

        configurationService.set(ConfigurationKey.DATABASE_NAME, namedArgs.getOrDefault("--database-name", "velox"));
    }
}
