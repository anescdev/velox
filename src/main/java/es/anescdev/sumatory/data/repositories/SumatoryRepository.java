package es.anescdev.sumatory.data.repositories;

import javax.inject.Inject;

import es.anescdev.core.data.repositories.ORMLiteAbstractRepository;
import es.anescdev.sumatory.data.dao.SumatoryDao;
import es.anescdev.sumatory.model.entities.Sumatory;

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
