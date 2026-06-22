package es.anescdev.shared.infrastructure.providers;

import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import es.anescdev.shared.domain.exceptions.DatabaseConnectionException;
import es.anescdev.shared.domain.providers.ConfigurationProvider;
import es.anescdev.shared.domain.providers.DatabaseConnectionProvider;
import es.anescdev.shared.domain.providers.ConfigurationProvider.ConfigurationKey;

public class SQLiteDatabaseConnectionProvider implements DatabaseConnectionProvider {
    private final ConfigurationProvider configurationService;
    private final SQLiteConfig config;

    @Inject
    public SQLiteDatabaseConnectionProvider(ConfigurationProvider configurationService) {
        this.configurationService = configurationService;
        this.config = new SQLiteConfig();
        config.setOpenMode(SQLiteOpenMode.CREATE);
    }

    public Connection getDatabaseConnection() throws DatabaseConnectionException {
        try {
            String connUrl = "jdbc:sqlite:" + FileSystems.getDefault()
                    .getPath(this.configurationService.get(ConfigurationKey.APP_DIRECTORY),
                            this.configurationService.get(ConfigurationKey.DATABASE_NAME))
                    + ".db";
            return DriverManager.getConnection(connUrl, this.config.toProperties());

        } catch (SQLException e) {
            if (e.getMessage().startsWith("[SQLITE_CANTOPEN]"))
                throw new DatabaseConnectionException("Can't connect to database");
            else
                throw new DatabaseConnectionException(e.getMessage());
        }
    }
}
