package es.anescdev.sumatory.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.anescdev.shared.domain.persistence.Repository;
import es.anescdev.shared.persistence.AbstractOrmLiteRepositoryTest;
import es.anescdev.sumatory.persistence.dao.SumatoryEntryDao;
import es.anescdev.sumatory.persistence.entities.Sumatory;
import es.anescdev.sumatory.persistence.entities.SumatoryEntry;
import es.anescdev.sumatory.persistence.repositories.ORMLiteSumatoryEntryRepository;

/**
 * Tests de integración para ORMLiteSumatoryEntryRepository.
 * Las operaciones CRUD comunes están en AbstractOrmLiteRepositoryTest;
 * aquí solo se cubre lo específico de SumatoryEntry: la persistencia
 * del campo timeWorked (Duration) y el método searchAllEntriesOf.
 */
class SumatoryEntryRepositoryTest extends AbstractOrmLiteRepositoryTest<SumatoryEntry, Long, SumatoryEntryDao> {

    @Override
    protected Class<SumatoryEntry> getEntityClass() {
        return SumatoryEntry.class;
    }

    @Override
    protected SumatoryEntry buildEntity(int seed) {
        return new SumatoryEntry(0L, seed, "COD" + seed, "Cliente " + seed, Duration.ofHours(seed));
    }

    @Override
    protected Repository<SumatoryEntry, Long> buildRepository(SumatoryEntryDao dao) {
        return new ORMLiteSumatoryEntryRepository(dao);
    }

    @Override
    protected Long getId(SumatoryEntry entity) {
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

    private SumatoryEntry buildEntityForSumatory(int seed, long sumatoryId) {
        SumatoryEntry entry = buildEntity(seed);
        entry.setSumatoryId(sumatoryId);
        return entry;
    }

    @Test
    @DisplayName("updateEntity actualiza el campo timeWorked (Duration) de una entidad existente")
    void updateEntity_deberiaActualizarTimeWorked() throws Exception {
        SumatoryEntry creada = repository.createEntity(buildEntity(1));
        creada.setTimeWorked(Duration.ofMinutes(125));

        repository.updateEntity(creada);

        Optional<SumatoryEntry> actualizada = repository.searchById(creada.getId());
        assertTrue(actualizada.isPresent());
        assertEquals(Duration.ofMinutes(125), actualizada.get().getTimeWorked());
    }

    @Test
    @DisplayName("searchAllEntriesOf devuelve solo las entries del sumatorio indicado")
    void searchAllEntriesOf_deberiaDevolverSoloEntriesDelSumatorioIndicado() throws Exception {
        ORMLiteSumatoryEntryRepository entryRepository = (ORMLiteSumatoryEntryRepository) repository;

        repository.createEntity(buildEntityForSumatory(1, 10L));
        repository.createEntity(buildEntityForSumatory(2, 10L));
        repository.createEntity(buildEntityForSumatory(3, 20L)); // de otro sumatorio, no debe aparecer

        Sumatory sumatory = new Sumatory(10L, (byte) 6, (short) 2026, "Empleado A", Duration.ZERO);

        var pagina = entryRepository.searchAllEntriesOf(sumatory, 0, 10);

        assertEquals(2, pagina.entities().size());
        assertTrue(pagina.entities().stream().allMatch(e -> e.getSumatoryId() == 10L));
    }

    @Test
    @DisplayName("searchAllEntriesOf respeta el límite indicado")
    void searchAllEntriesOf_deberiaRespetarLimite() throws Exception {
        ORMLiteSumatoryEntryRepository entryRepository = (ORMLiteSumatoryEntryRepository) repository;

        for (int i = 1; i <= 5; i++) {
            repository.createEntity(buildEntityForSumatory(i, 10L));
        }

        Sumatory sumatory = new Sumatory(10L, (byte) 6, (short) 2026, "Empleado A", Duration.ZERO);

        var pagina = entryRepository.searchAllEntriesOf(sumatory, 0, 2);

        assertEquals(2, pagina.entities().size());
    }

    @Test
    @DisplayName("searchAllEntriesOf devuelve página vacía si el sumatorio no tiene entries")
    void searchAllEntriesOf_deberiaDevolverPaginaVaciaSiSumatorioSinEntries() throws Exception {
        ORMLiteSumatoryEntryRepository entryRepository = (ORMLiteSumatoryEntryRepository) repository;

        repository.createEntity(buildEntityForSumatory(1, 10L));

        Sumatory sumatorySinEntries = new Sumatory(999L, (byte) 6, (short) 2026, "Empleado Z", Duration.ZERO);

        var pagina = entryRepository.searchAllEntriesOf(sumatorySinEntries, 0, 10);

        assertTrue(pagina.entities().isEmpty());
    }
}