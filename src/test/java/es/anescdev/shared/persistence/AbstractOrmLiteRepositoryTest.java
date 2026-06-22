package es.anescdev.shared.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import es.anescdev.shared.domain.exceptions.RemoveEntityException;
import es.anescdev.shared.domain.exceptions.UpdateEntityException;
import es.anescdev.shared.domain.persistence.Page;
import es.anescdev.shared.domain.persistence.Repository;

/**
 * Clase base con los tests de integración comunes a cualquier repositorio
 * ORMLite que implemente {@link Repository}: create, update, remove,
 * searchById, count, searchAll y setup.
 *
 * Usa SQLite en memoria real en vez de mocks, ya que mockear el QueryBuilder
 * de ORMLite testearía la implementación interna en vez del comportamiento.
 *
 * Cada test concreto debe extender esta clase y solo definir:
 * - cómo construir una entidad nueva (sin ID) numerada de forma única
 * - cómo extraer el ID de una entidad
 * - cómo construir el repositorio a partir del Dao
 * - la clase de la entidad, para crear/eliminar la tabla
 *
 * @param <T>  la entidad
 * @param <ID> el tipo del identificador de la entidad
 */
public abstract class AbstractOrmLiteRepositoryTest<T, ID, DAO extends Dao<T, ID>> {

    protected ConnectionSource connectionSource;
    protected DAO dao;
    protected Repository<T, ID> repository;

    /**
     * Clase de la entidad, usada para crear/eliminar la tabla vía TableUtils.
     */
    protected abstract Class<T> getEntityClass();

    /**
     * Construye una entidad nueva (sin ID) y distinguible mediante un índice
     * numérico, usado para crear varias entidades distintas en un mismo test.
     */
    protected abstract T buildEntity(int seed);

    /**
     * Construye el repositorio concreto a partir del Dao ya creado.
     */
    protected abstract Repository<T, ID> buildRepository(DAO dao);

    /**
     * Extrae el ID de una entidad ya persistida.
     */
    protected abstract ID getId(T entity);

    /**
     * Devuelve un ID que con total seguridad no existe en la base de datos,
     * usado para los casos de "entidad no encontrada".
     */
    protected abstract ID nonExistentId();

    @BeforeEach
    void setUpBase() throws SQLException {
        // Conexión nueva por test para evitar estado compartido entre tests
        this.connectionSource = new JdbcConnectionSource("jdbc:sqlite::memory:");
        TableUtils.createTableIfNotExists(connectionSource, getEntityClass());
        this.dao = DaoManager.createDao(connectionSource, getEntityClass());
        this.repository = buildRepository(dao);
    }

    @AfterEach
    void tearDownBase() throws Exception {
        this.connectionSource.close();
    }

    @Test
    @DisplayName("createEntity guarda la entidad y le asigna un ID generado")
    void createEntity_deberiaAsignarIdYPersistir() throws Exception {
        T entity = buildEntity(1);

        T creada = repository.createEntity(entity);

        assertEquals(1, repository.count());
        assertDoesNotThrow(() -> getId(creada));
    }

    @Test
    @DisplayName("updateEntity lanza UpdateEntityException si la entidad no existe")
    void updateEntity_deberiaLanzarExcepcionSiNoExiste() throws Exception {
        T creada = repository.createEntity(buildEntity(1));
        // Creamos y eliminamos para tener una entidad "fantasma" con datos válidos pero sin fila real
        repository.removeEntityById(getId(creada));

        assertThrows(UpdateEntityException.class, () -> repository.updateEntity(creada));
    }

    @Test
    @DisplayName("removeEntityById elimina una entidad existente")
    void removeEntityById_deberiaEliminarEntidadExistente() throws Exception {
        T creada = repository.createEntity(buildEntity(1));

        repository.removeEntityById(getId(creada));

        assertFalse(repository.searchById(getId(creada)).isPresent());
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("removeEntityById lanza RemoveEntityException si la entidad no existe")
    void removeEntityById_deberiaLanzarExcepcionSiNoExiste() {
        assertThrows(RemoveEntityException.class, () -> repository.removeEntityById(nonExistentId()));
    }

    @Test
    @DisplayName("searchById devuelve Optional vacío si la entidad no existe")
    void searchById_deberiaDevolverOptionalVacioSiNoExiste() throws Exception {
        Optional<T> resultado = repository.searchById(nonExistentId());

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("searchById devuelve la entidad si existe")
    void searchById_deberiaDevolverEntidadSiExiste() throws Exception {
        T creada = repository.createEntity(buildEntity(1));

        Optional<T> resultado = repository.searchById(getId(creada));

        assertTrue(resultado.isPresent());
    }

    @Test
    @DisplayName("count devuelve el número total de entidades")
    void count_deberiaDevolverNumeroTotalDeEntidades() throws Exception {
        repository.createEntity(buildEntity(1));
        repository.createEntity(buildEntity(2));
        repository.createEntity(buildEntity(3));

        assertEquals(3, repository.count());
    }

    @Test
    @DisplayName("count devuelve 0 si no hay entidades")
    void count_deberiaDevolverCeroSiNoHayEntidades() throws Exception {
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("searchAll respeta el límite indicado")
    void searchAll_deberiaRespetarLimite() throws Exception {
        for (int i = 1; i <= 5; i++) {
            repository.createEntity(buildEntity(i));
        }

        Page<T> pagina = repository.searchAll(zeroId(), 2);

        assertEquals(2, pagina.entities().size());
    }

    @Test
    @DisplayName("searchAll devuelve página vacía si no hay entidades con ID mayor al cursor")
    void searchAll_deberiaDevolverPaginaVaciaSiNoHayMasResultados() throws Exception {
        T unica = repository.createEntity(buildEntity(1));

        Page<T> pagina = repository.searchAll(getId(unica), 10);

        assertTrue(pagina.entities().isEmpty());
    }

    @Test
    @DisplayName("setup crea la tabla correctamente cuando no existe")
    void setup_deberiaCrearTablaSiNoExiste() throws Exception {
        // El @BeforeEach ya crea la tabla, la eliminamos para partir de un estado limpio
        TableUtils.dropTable(connectionSource, getEntityClass(), true);

        repository.setup();

        // Si la tabla se creó correctamente, debe poder usarse sin lanzar excepción
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("setup no falla si la tabla ya existe")
    void setup_noDeberiaFallarSiLaTablaYaExiste() {
        // La tabla ya existe gracias al @BeforeEach
        assertDoesNotThrow(() -> repository.setup());
    }

    /**
     * ID "cero" usado como cursor inicial en los tests de paginación.
     */
    protected abstract ID zeroId();
}