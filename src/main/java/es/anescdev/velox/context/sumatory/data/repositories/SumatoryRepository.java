package es.anescdev.velox.context.sumatory.data.repositories;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.sumatory.data.dao.SumatoryDao;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.core.data.repositories.ORMLiteAbstractRepository;
import es.anescdev.velox.core.exceptions.SearchEntityException;

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
        return Sumatory.ID_COLUMN;
    }

    public List<Sumatory> searchByEmployee(Employee employee) throws SearchEntityException {
        try {
            return this.dao.queryForEq(Sumatory.EMPLOYEE_COLUMN, employee);
        } catch (SQLException e) {
            throw new SearchEntityException(e.getMessage());
        }
    }

}
