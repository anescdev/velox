package es.anescdev.velox.context.employee.data.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.employee.data.dao.EmployeeDao;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.data.repositories.ORMLiteAbstractRepository;

/**
 * @author AnesCDev
 */
@Singleton
public class EmployeeRepository extends ORMLiteAbstractRepository<Employee, Long, EmployeeDao> {

    @Inject
    public EmployeeRepository(EmployeeDao dao) {
        super(dao);
    }

    @Override
    protected Class<Employee> getEntityClass() {
        return Employee.class;
    }

    @Override
    protected String getIdColumn() {
        return Employee.ID_COLUMN;
    }
    
}
