package es.anescdev.velox.core.providers;

import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;
import org.sqlite.SQLiteConfig.Pragma;

import es.anescdev.velox.core.exceptions.DatabaseConnectionException;
import es.anescdev.velox.core.providers.DatabaseConnectionProvider;
import es.anescdev.velox.core.providers.ConfigurationProvider.ConfigurationKey;

public class DatabaseConnectionProvider {
    private final ConfigurationProvider configurationService;
    private final SQLiteConfig config;

    @Inject
    public DatabaseConnectionProvider(ConfigurationProvider configurationService) {
        this.configurationService = configurationService;
        this.config = new SQLiteConfig();
        config.setOpenMode(SQLiteOpenMode.CREATE);
        config.setPragma(Pragma.FOREIGN_KEYS, "ON");
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
