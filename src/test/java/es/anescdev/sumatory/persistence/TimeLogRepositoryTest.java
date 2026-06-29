package es.anescdev.sumatory.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.anescdev.core.data.AbstractOrmLiteRepositoryTest;
import es.anescdev.core.data.repositories.Repository;
import es.anescdev.sumatory.model.entities.Sumatory;
import es.anescdev.sumatory.model.entities.TimeLog;
import es.anescdev.sumatory.data.dao.TimeLogDao;
import es.anescdev.sumatory.data.repositories.TimeLogRepository;



/**
 * Tests de integración para TimeLogRepository.
 * Las operaciones CRUD comunes están en AbstractOrmLiteRepositoryTest;
 * aquí solo se cubre lo específico de TimeLog: la persistencia
 * del campo timeWorked (Duration) y el método searchAllEntriesOf.
 */
class TimeLogRepositoryTest extends AbstractOrmLiteRepositoryTest<TimeLog, Long, TimeLogDao> {

    @Override
    protected Class<TimeLog> getEntityClass() {
        return TimeLog.class;
    }

    @Override
    protected TimeLog buildEntity(int seed) {
        return new TimeLog(0L, seed, "COD" + seed, "Cliente " + seed, Duration.ofHours(seed));
    }

    @Override
    protected Repository<TimeLog, Long> buildRepository(TimeLogDao dao) {
        return new TimeLogRepository(dao);
    }

    @Override
    protected Long getId(TimeLog entity) {
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

    private TimeLog buildEntityForSumatory(int seed, long sumatoryId) {
        TimeLog entry = buildEntity(seed);
        entry.setSumatoryId(sumatoryId);
        return entry;
    }

    @Test
    @DisplayName("updateEntity actualiza el campo timeWorked (Duration) de una entidad existente")
    void updateEntity_deberiaActualizarTimeWorked() throws Exception {
        TimeLog creada = repository.createEntity(buildEntity(1));
        creada.setTimeWorked(Duration.ofMinutes(125));

        repository.updateEntity(creada);

        Optional<TimeLog> actualizada = repository.searchById(creada.getId());
        assertTrue(actualizada.isPresent());
        assertEquals(Duration.ofMinutes(125), actualizada.get().getTimeWorked());
    }

    @Test
    @DisplayName("searchAllEntriesOf devuelve solo las entries del sumatorio indicado")
    void searchAllEntriesOf_deberiaDevolverSoloEntriesDelSumatorioIndicado() throws Exception {
        TimeLogRepository entryRepository = (TimeLogRepository) repository;

        repository.createEntity(buildEntityForSumatory(1, 10L));
        repository.createEntity(buildEntityForSumatory(2, 10L));
        repository.createEntity(buildEntityForSumatory(3, 20L)); // de otro sumatorio, no debe aparecer

        Sumatory sumatory = new Sumatory(10L, (byte) 6, (short) 2026, "Empleado A", Duration.ZERO);

        var pagina = entryRepository.searchAllEntriesOf(sumatory, 0, 10);

        assertEquals(2, pagina.size());
        assertTrue(pagina.stream().allMatch(e -> e.getSumatoryId() == 10L));
    }

    @Test
    @DisplayName("searchAllEntriesOf respeta el límite indicado")
    void searchAllEntriesOf_deberiaRespetarLimite() throws Exception {
        TimeLogRepository entryRepository = (TimeLogRepository) repository;

        for (int i = 1; i <= 5; i++) {
            repository.createEntity(buildEntityForSumatory(i, 10L));
        }

        Sumatory sumatory = new Sumatory(10L, (byte) 6, (short) 2026, "Empleado A", Duration.ZERO);

        var pagina = entryRepository.searchAllEntriesOf(sumatory, 0, 2);

        assertEquals(2, pagina.size());
    }

    @Test
    @DisplayName("searchAllEntriesOf devuelve página vacía si el sumatorio no tiene entries")
    void searchAllEntriesOf_deberiaDevolverPaginaVaciaSiSumatorioSinEntries() throws Exception {
        TimeLogRepository entryRepository = (TimeLogRepository) repository;

        repository.createEntity(buildEntityForSumatory(1, 10L));

        Sumatory sumatorySinEntries = new Sumatory(999L, (byte) 6, (short) 2026, "Empleado Z", Duration.ZERO);

        var pagina = entryRepository.searchAllEntriesOf(sumatorySinEntries, 0, 10);

        assertTrue(pagina.isEmpty());
    }
}