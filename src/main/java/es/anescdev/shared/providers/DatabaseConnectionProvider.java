package es.anescdev.shared.providers;

import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import es.anescdev.shared.exceptions.DatabaseConnectionException;
import es.anescdev.shared.providers.ConfigurationProvider.ConfigurationKey;

public class DatabaseConnectionProvider {
    private final ConfigurationProvider configurationService;
    private final SQLiteConfig config;
    private Connection conn;

    @Inject
    public DatabaseConnectionProvider(ConfigurationProvider configurationService) {
        this.configurationService = configurationService;
        this.config = new SQLiteConfig();
        config.setOpenMode(SQLiteOpenMode.CREATE);
    }

    public Connection getDatabaseConnection() throws DatabaseConnectionException {
        try {
            if (this.conn == null || this.conn.isClosed()) {
                this.conn = DriverManager.getConnection(String.format("jdbc:sqlite:%s", FileSystems.getDefault()
                        .getPath(this.configurationService.get(ConfigurationKey.APP_DIRECTORY),
                                String.format("%s.db", this.configurationService.get(ConfigurationKey.DATABASE_NAME)))),
                        this.config.toProperties());
            }
        } catch (SQLException e) {
            if (e.getMessage().startsWith("[SQLITE_CANTOPEN]"))
                throw new DatabaseConnectionException("Can't connect to database");
            else
                throw new DatabaseConnectionException(e.getMessage());
        }
        return conn;
    }
}
