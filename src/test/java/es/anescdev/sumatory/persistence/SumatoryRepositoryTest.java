package es.anescdev.sumatory.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import es.anescdev.shared.domain.exceptions.CreateEntityException;
import es.anescdev.shared.domain.persistence.Repository;
import es.anescdev.shared.persistence.AbstractOrmLiteRepositoryTest;
import es.anescdev.sumatory.persistence.dao.SumatoryDao;
import es.anescdev.sumatory.persistence.entities.Sumatory;
import es.anescdev.sumatory.persistence.repositories.ORMLiteSumatoryRepository;

/**
 * Tests de integración para ORMLiteSumatoryRepository.
 * Las operaciones CRUD comunes están en AbstractOrmLiteRepositoryTest;
 * aquí solo se cubre lo específico de Sumatory: la constraint única
 * y la persistencia del campo total (Duration).
 */
class SumatoryRepositoryTest extends AbstractOrmLiteRepositoryTest<Sumatory, Long, SumatoryDao> {

    @Override
    protected Class<Sumatory> getEntityClass() {
        return Sumatory.class;
    }

    @Override
    protected Sumatory buildEntity(int seed) {
        return new Sumatory(0L, (byte) seed, (short) 2026, "Empleado " + seed, Duration.ZERO);
    }

    @Override
    protected Repository<Sumatory, Long> buildRepository(SumatoryDao dao) {
        return new ORMLiteSumatoryRepository(dao);
    }

    @Override
    protected Long getId(Sumatory entity) {
        return entity.getId();
    }

    @Override
    protected Long nonExistentId() {
        return 999_999L;
    }

    @Override
    protected Long zeroId() {
        return Long.valueOf(0L);
    }

    @Test
    @DisplayName("createEntity respeta la constraint uniqueCombo de month/year/employee")
    void createEntity_deberiaFallarPorConstraintUnica() throws Exception {
        repository.createEntity(buildEntity(6));

        Sumatory duplicado = buildEntity(6);

        assertThrows(CreateEntityException.class, () -> repository.createEntity(duplicado));
    }

    @Test
    @DisplayName("updateEntity actualiza el campo total (Duration) de una entidad existente")
    void updateEntity_deberiaActualizarTotal() throws Exception {
        Sumatory creado = repository.createEntity(buildEntity(6));
        creado.setTotal(Duration.ofMinutes(480));

        repository.updateEntity(creado);

        Optional<Sumatory> actualizado = repository.searchById(creado.getId());
        assertTrue(actualizado.isPresent());
        assertEquals(Duration.ofMinutes(480), actualizado.get().getTotal());
    }

    @Test
    @DisplayName("searchAll respeta el cursor lastSeenId, devolviendo solo IDs mayores")
    void searchAll_deberiaRespetarCursor() throws Exception {
        Sumatory s1 = repository.createEntity(buildEntity(1));
        Sumatory s2 = repository.createEntity(buildEntity(2));
        repository.createEntity(buildEntity(3));

        var pagina = repository.searchAll(s1.getId(), 10);

        assertEquals(2, pagina.entities().size());
        assertTrue(pagina.entities().stream().allMatch(s -> s.getId() > s1.getId()));
        assertTrue(pagina.entities().stream().anyMatch(s -> s.getId() == s2.getId()));
    }
}