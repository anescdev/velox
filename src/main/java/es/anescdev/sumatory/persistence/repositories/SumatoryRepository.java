package es.anescdev.sumatory.persistence.repositories;

import javax.inject.Inject;

import es.anescdev.core.persistence.repositories.ORMLiteAbstractRepository;
import es.anescdev.sumatory.model.Sumatory;
import es.anescdev.sumatory.persistence.dao.SumatoryDao;

public class SumatoryRepository extends ORMLiteAbstractRepository<Sumatory, Long, SumatoryDao>{

    @Inject
    public SumatoryRepository(SumatoryDao dao) {
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
