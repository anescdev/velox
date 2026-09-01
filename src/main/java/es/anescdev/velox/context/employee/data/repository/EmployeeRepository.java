package es.anescdev.velox.context.employee.data.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.employee.data.dao.EmployeeDao;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.data.repositories.ORMLiteAbstractRepository;

@Singleton
/**
 * Repositorio del dominio employee: implementación concreta del CRUD genérico
 * (crear, actualizar, borrar, buscar paginado, contar) delegando en el DAO de ORMLite
 * inyectado. No suele contener lógica propia salvo indicar la columna de id y la clase
 * de entidad al repositorio abstracto del que hereda.
 */
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
