package es.anescdev.shared.lib;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import javax.inject.Inject;

import org.codejargon.feather.Feather;
import org.codejargon.feather.Provides;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.anescdev.shared.domain.exceptions.CountEntityException;
import es.anescdev.shared.domain.exceptions.CreateEntityException;
import es.anescdev.shared.domain.exceptions.RemoveEntityException;
import es.anescdev.shared.domain.exceptions.SearchEntityException;
import es.anescdev.shared.domain.exceptions.SetupRepositoryException;
import es.anescdev.shared.domain.exceptions.UpdateEntityException;
import es.anescdev.shared.domain.persistence.Page;
import es.anescdev.shared.domain.persistence.Repository;

/**
 * @author AnesCDev
 */
@Disabled("Test exploratorio: ya confirmó que Feather necesita interfaces específicas por tipo genérico, no forma parte de la suite regular ya que no es capaz de diferenciar interfaces genéricas con distintos tipos")
public class FeatherTest {

    static class Rep1 implements Repository<FeatherTest.TestEntity, Integer> {

        @Override
        public TestEntity createEntity(TestEntity entity) throws CreateEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'createEntity'");
        }

        @Override
        public void updateEntity(TestEntity entity) throws UpdateEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'updateEntity'");
        }

        @Override
        public void removeEntityById(Integer entityId) throws RemoveEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'removeEntityById'");
        }

        @Override
        public Page<TestEntity> searchAll(Integer lastSeenId, int limit) throws SearchEntityException {

            throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
        }

        @Override
        public Optional<TestEntity> searchById(Integer entityId) throws SearchEntityException {

            throw new UnsupportedOperationException("Unimplemented method 'searchById'");
        }

        @Override
        public long count() throws CountEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'count'");
        }

        @Override
        public void setup() throws SetupRepositoryException {
            throw new UnsupportedOperationException("Unimplemented method 'setup'");
        }

    }

    static class Rep2 implements Repository<FeatherTest.TestEntityEpic, Integer> {

        @Override
        public TestEntityEpic createEntity(TestEntityEpic entity) throws CreateEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'createEntity'");
        }

        @Override
        public void updateEntity(TestEntityEpic entity) throws UpdateEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'updateEntity'");
        }

        @Override
        public void removeEntityById(Integer entityId) throws RemoveEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'removeEntityById'");
        }

        @Override
        public Page<TestEntityEpic> searchAll(Integer lastSeenId, int limit) throws SearchEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'searchAll'");
        }

        @Override
        public Optional<TestEntityEpic> searchById(Integer entityId) throws SearchEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'searchById'");
        }

        @Override
        public long count() throws CountEntityException {
            throw new UnsupportedOperationException("Unimplemented method 'count'");
        }

        @Override
        public void setup() throws SetupRepositoryException {
            throw new UnsupportedOperationException("Unimplemented method 'setup'");
        }

    }

    static class TestModule {
        @Provides
        Repository<TestEntity, Integer> testEntitRepository() {
            return new Rep1();
        }

        @Provides
        Repository<TestEntityEpic, Integer> testEntitEpicRepository() {
            return new Rep2();
        }
    }

    static class TestEntity {
    }

    static class TestEntityEpic {
    }

    static class Con1 {
        @Inject
        private Repository<TestEntity, Integer> repo;
    }

    static class Con2 {
        @Inject
        private Repository<TestEntityEpic, Integer> repo;
    }

    @Test
    @DisplayName("Comprueba que Feather haga bien la inyección de dependencias con genéricos")
    void feather_compruebaInyectaCorrectamenteLasDependencias() {
        var feather = Feather.with(new TestModule());

        Con1 con1 = new Con1();
        Con2 con2 = new Con2();

        assertDoesNotThrow(() -> {
            feather.injectFields(con1);
            feather.injectFields(con2);
        });

        assertNotNull(con1.repo);
        assertNotNull(con2.repo);

        assertInstanceOf(Rep1.class, con1.repo);
        assertInstanceOf(Rep2.class, con2.repo);
    }
}
