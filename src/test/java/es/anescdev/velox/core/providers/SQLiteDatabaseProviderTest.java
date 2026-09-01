package es.anescdev.velox.core.providers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.PosixFilePermissions;
import java.sql.Connection;
import java.util.Collections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import es.anescdev.velox.core.exceptions.DatabaseConnectionException;
import es.anescdev.velox.core.providers.ConfigurationProvider.ConfigurationKey;

/**
 * @author AnesCDev
 */
public class SQLiteDatabaseProviderTest {
    public static ConfigurationProvider generateConfigurationService() {
        ConfigurationProvider configurationService = new ConfigurationProvider();
        String appDir = System.getProperty("java.io.tmpdir");
        configurationService.set(ConfigurationKey.APP_DIRECTORY, appDir);
        return configurationService;
    }

    private static File getDatabaseFile(ConfigurationProvider configurationService) {
        return FileSystems.getDefault()
                .getPath(configurationService.get(ConfigurationKey.APP_DIRECTORY),
                        String.format("%s.db", configurationService.get(ConfigurationKey.DATABASE_NAME)))
                .toFile();
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    public class NonAccessibleDatabase {
        private final ConfigurationProvider configurationService = SQLiteDatabaseProviderTest
                .generateConfigurationService();
        private final DatabaseConnectionProvider databaseService = new DatabaseConnectionProvider(configurationService);
        private File nonAccesibleDatabase;

        @BeforeAll
        public void setupNonAccesible() throws IOException {
            configurationService.set(ConfigurationKey.DATABASE_NAME, "velox_non");
            nonAccesibleDatabase = getDatabaseFile(this.configurationService);
            if (nonAccesibleDatabase.exists() && !nonAccesibleDatabase.delete())
                throw new IOException("Cannot remove the old file");
            if (!nonAccesibleDatabase.createNewFile())
                throw new IOException(
                        String.format("Can't create the database file on %s", nonAccesibleDatabase.toPath()));
            try {
                Files.setPosixFilePermissions(nonAccesibleDatabase.toPath(),
                        PosixFilePermissions.fromString("---------"));
            } catch (ClassCastException | IOException e) {
                throw new IOException(
                        String.format("Can't change the database file permissions. Reason: %s", e.getMessage()),
                        e.getCause());
            } catch (UnsupportedOperationException e) {
                AclFileAttributeView view = Files.getFileAttributeView(nonAccesibleDatabase.toPath(), AclFileAttributeView.class);

                if (view != null) {
                    view.setAcl(Collections.emptyList());
                }
            }

        }

        @Test
        @DisplayName("Test if can't connect to a non accesible database")
        public void testCantConnectToNonAccesibleDatabase() {
            DatabaseConnectionException connError = assertThrowsExactly(DatabaseConnectionException.class,
                    () -> databaseService.getDatabaseConnection());
            assertEquals("Can't connect to database", connError.getMessage());
        }

        @AfterAll
        public void cleanUpNonAccesible() {
            nonAccesibleDatabase.delete();
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    public class AccesibleDatabase {
        private final ConfigurationProvider configurationService = SQLiteDatabaseProviderTest
                .generateConfigurationService();
        private final DatabaseConnectionProvider databaseService = new DatabaseConnectionProvider(configurationService);
        private File accesibleDatabase;

        @BeforeAll
        public void setupAccesible() throws IOException {
            configurationService.set(ConfigurationKey.DATABASE_NAME, "velox");
            accesibleDatabase = getDatabaseFile(this.configurationService);
            if (accesibleDatabase.exists())
                accesibleDatabase.delete();
        }

        @Test
        @DisplayName("Test if can connect to non existing database")
        public void testCanConnectToNewDatabase() {
            assertDoesNotThrow(() -> {
                Connection conn = databaseService.getDatabaseConnection();
                assertNotNull(conn);
                assertDoesNotThrow(() -> assertFalse(conn.isClosed()));
            });
        }

        @AfterAll
        public void cleanUp() {
            accesibleDatabase.delete();
        }
    }
}
