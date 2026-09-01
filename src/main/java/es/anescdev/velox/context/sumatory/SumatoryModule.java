package es.anescdev.velox.context.sumatory;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.app.module.AppModule;
import es.anescdev.velox.context.sumatory.data.dao.SumatoryDao;
import es.anescdev.velox.context.sumatory.data.dao.SumatoryEntryDao;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.context.sumatory.model.service.SumatoryService;
import es.anescdev.velox.context.sumatory.view.viewmodel.SumatoryListViewModel;
import es.anescdev.velox.core.bus.BusKey;
import es.anescdev.velox.core.exceptions.DatabaseConnectionException;

@AppModule
/**
 * Módulo de Feather del dominio Sumatorio (resumen mensual de horas). Detectado
 * automáticamente por {@link es.anescdev.velox.app.App#searchAppModules()} gracias a
 * {@code @AppModule}. Provee los DAO de {@code Sumatory} (cabecera mensual) y
 * {@code SumatoryEntry} (entradas diarias).
 */
public class SumatoryModule {
    public static final BusKey<Sumatory> SUMATORY_KEY = new BusKey<>("sumatory", Sumatory.class);

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

    @Provides
    @Singleton
    SumatoryListViewModel providesSumatoryListViewModel(SumatoryService service) {
        SumatoryListViewModel viewModel = new SumatoryListViewModel(service);
        viewModel.searchEntities();
        return viewModel;
    }
}
