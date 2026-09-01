package es.anescdev.velox.context.employee.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.data.DaoInitializable;

/**
 * @author AnesCDev
 */
@DaoInitializable
public class EmployeeDao extends BaseDaoImpl<Employee, Long> {
    public EmployeeDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Employee.class);
    }
    
}
