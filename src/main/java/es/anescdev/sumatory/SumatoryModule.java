package es.anescdev.sumatory;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.core.exceptions.DatabaseConnectionException;
import es.anescdev.core.exceptions.SetupRepositoryException;
import es.anescdev.sumatory.model.Sumatory;
import es.anescdev.sumatory.model.TimeLog;
import es.anescdev.sumatory.persistence.dao.SumatoryDao;
import es.anescdev.sumatory.persistence.dao.TimeLogDao;
import es.anescdev.sumatory.persistence.repositories.SumatoryRepository;
import es.anescdev.sumatory.service.SumatoryService;
import es.anescdev.sumatory.viewmodel.SumatoryListViewModel;

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
    TimeLogDao providesTimeLogDao(ConnectionSource connectionSource) throws DatabaseConnectionException {
        try {
            return DaoManager.createDao(connectionSource, TimeLog.class);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Error during database connection or DAO creation. Error: " + e.getMessage());
        }
    }
          

    @Provides
    @Singleton
    SumatoryRepository providesSumatoryRepository(SumatoryDao dao) throws SetupRepositoryException{
        SumatoryRepository repo = new SumatoryRepository(dao);
        repo.setup();
        return repo;
    }
    @Provides
    @Singleton
    SumatoryListViewModel providesSumatoryListViewModel(SumatoryService service) {
        SumatoryListViewModel viewModel = new SumatoryListViewModel(service);
        viewModel.searchSumatories(); //TODO: Arreglar problema de concurrencia si lo hubiera al crear a la vez el viewmodel e intentar añadir uno de golpe, no debería pero si hay conflicto, para saberlo
        return viewModel;
    }
}
