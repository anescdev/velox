package es.anescdev.sumatory.persistence.repositories;

import es.anescdev.sumatory.persistence.dao.SumatoryDao;
import es.anescdev.sumatory.persistence.entities.Sumatory;

public class ORMLiteSumatoryRepository extends ORMLiteAbstractRepository<Sumatory, Long, SumatoryDao> implements SumatoryRepository {

    public ORMLiteSumatoryRepository(SumatoryDao dao) {
        super(dao);
    }

    @Override
    protected Class<Sumatory> getEntityClass() {
        return Sumatory.class;
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

}
