package es.anescdev.sumatory;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.core.exceptions.DatabaseConnectionException;
import es.anescdev.sumatory.model.Sumatory;
import es.anescdev.sumatory.model.SumatoryEntry;
import es.anescdev.sumatory.persistence.dao.SumatoryDao;
import es.anescdev.sumatory.persistence.dao.SumatoryEntryDao;

public class SumatoryModule {
    @Provides
    @Singleton
    SumatoryDao providesSumatoryDao(ConnectionSource connectionSource) throws DatabaseConnectionException {
        try {
            return DaoManager.createDao(connectionSource, Sumatory.class);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Error during database connection or DAO creation. Error: " + e.getMessage());
        }
    }

    @Provides
    @Singleton
    SumatoryEntryDao providesSumatoryEntryDao(ConnectionSource connectionSource) throws DatabaseConnectionException {
        try {
            return DaoManager.createDao(connectionSource, SumatoryEntry.class);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Error during database connection or DAO creation. Error: " + e.getMessage());
        }
    }
}
