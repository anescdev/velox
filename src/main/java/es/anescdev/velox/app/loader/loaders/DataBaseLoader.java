package es.anescdev.velox.app.loader.loaders;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.reflections.Reflections;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import es.anescdev.velox.app.App;
import es.anescdev.velox.app.loader.LoadException;
import es.anescdev.velox.app.loader.Loader;
import es.anescdev.velox.app.loader.events.LoadEvent;
import es.anescdev.velox.core.data.DaoInitializable;
import es.anescdev.velox.core.data.datatypes.DurationDataType;

import javafx.event.EventType;

@Singleton
/**
 * Loader de arranque encargado de dejar la base de datos lista para usarse:
 * <ol>
 *   <li>Registra en ORMLite los "persisters" a medida (ver {@code core/data/datatypes})
 *       que permiten guardar directamente tipos de JavaFX ({@code IntegerProperty}, etc.)
 *       y {@link java.time.Duration} en SQLite.</li>
 *   <li>Escanea por reflexión (vía {@link org.reflections.Reflections}) todas las clases
 *       DAO anotadas con {@link es.anescdev.velox.core.data.DaoInitializable} y crea su
 *       tabla en SQLite si todavía no existe (ignorando el error "already exists").</li>
 * </ol>
 * Gracias a este escaneo, un módulo de dominio nuevo no necesita registrar su tabla a mano:
 * basta con anotar su DAO con {@code @DaoInitializable}.
 */
public class DataBaseLoader implements Loader {
    public final static EventType<LoadEvent> DATABASE_LOAD_EVENT_TYPE = new EventType<>("DATABASE");
    public final static String LOADING_STRING = "load.database.message";
    private final ConnectionSource connectionSource;

    @Inject
    public DataBaseLoader(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    @Override
    public void load() throws LoadException {
        try {
            this.setupDataTypePersisters();
            App.instance().getLogger().config("Configured ORMLite persisters");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new LoadException("load.database.error.datatype", e.getMessage());
        }
        try {
            this.setupTables();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException e) {
            throw new LoadException("load.database.error.find", e.getMessage());
        } catch (SQLException e) {
            throw new LoadException("load.database.error.setup", e.getMessage());
        }
    }

    @Override
    public EventType<LoadEvent> eventType() {
        return DATABASE_LOAD_EVENT_TYPE;
    }

    @Override
    public String getLoadingMessage() {
        return LOADING_STRING;
    }

    private void setupDataTypePersisters()
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Reflections persirstersReflection = new Reflections("es.anescdev.velox.context.core.data.datatypes");
        for (Class<? extends BaseDataType> persisterClass : persirstersReflection.getSubTypesOf(BaseDataType.class)) {
            DataPersisterManager.registerDataPersisters(
                    ((BaseDataType) persisterClass.getDeclaredMethod("getSingleton").invoke(null)));
        }
        DataPersisterManager.registerDataPersisters(DurationDataType.getSingleton());
    }

    private void setupTables() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, SQLException {
        Reflections reflections = new Reflections("es.anescdev.velox.context");
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(DaoInitializable.class)) {
            var daoConstructor = clazz.getDeclaredConstructor(ConnectionSource.class);
            daoConstructor.setAccessible(true);
            Dao<?, ?> dao = (Dao<?, ?>) daoConstructor.newInstance(connectionSource);
            try {
                TableUtils.createTable(connectionSource, dao.getDataClass());
                App.instance().getLogger().config("Created table " + clazz.getSimpleName());
            } catch (SQLException e) {
                if(!e.getCause().getMessage().endsWith("already exists)")) throw e;
            }
        }
    }
}
