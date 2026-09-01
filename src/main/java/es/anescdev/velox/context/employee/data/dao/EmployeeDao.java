package es.anescdev.velox.context.employee.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.data.DaoInitializable;

@DaoInitializable
/**
 * DAO de ORMLite para Employee. Anotado con {@code @DaoInitializable}
 * para que {@code DataBaseLoader} cree su tabla automáticamente en el arranque.
 * No añade lógica propia: toda la lógica de acceso a datos vive en el
 * {@code Repository} correspondiente, que envuelve este DAO.
 */
public class EmployeeDao extends BaseDaoImpl<Employee, Long> {
    public EmployeeDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Employee.class);
    }
    
}
