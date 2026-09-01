package es.anescdev.velox.context.customer.data.dao;

import java.sql.SQLException;


import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.core.data.DaoInitializable;

@DaoInitializable
/**
 * DAO de ORMLite para Customer. Anotado con {@code @DaoInitializable}
 * para que {@code DataBaseLoader} cree su tabla automáticamente en el arranque.
 * No añade lógica propia: toda la lógica de acceso a datos vive en el
 * {@code Repository} correspondiente, que envuelve este DAO.
 */
public class CustomerDao extends BaseDaoImpl<Customer, Long> {
    public CustomerDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Customer.class);
    }

}
