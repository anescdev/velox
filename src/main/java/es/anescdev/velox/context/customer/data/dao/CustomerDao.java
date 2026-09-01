package es.anescdev.velox.context.customer.data.dao;

import java.sql.SQLException;


import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.core.data.DaoInitializable;

/**
 * @author AnesCDev
 */
@DaoInitializable
public class CustomerDao extends BaseDaoImpl<Customer, Long> {
    public CustomerDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Customer.class);
    }

}
