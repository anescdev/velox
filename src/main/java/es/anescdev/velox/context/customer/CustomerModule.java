package es.anescdev.velox.context.customer;

import java.sql.SQLException;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.j256.ormlite.support.ConnectionSource;

import es.anescdev.velox.app.module.AppModule;
import es.anescdev.velox.context.customer.data.dao.CustomerDao;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.core.bus.BusKey;
import es.anescdev.velox.core.exceptions.DatabaseConnectionException;

@AppModule
/**
 * Módulo de Feather del dominio Cliente. Al estar anotado con {@code @AppModule},
 * {@link es.anescdev.velox.app.App#searchAppModules()} lo detecta y carga automáticamente.
 * Provee el DAO de {@code Customer} (que necesita la {@code ConnectionSource} de ORMLite,
 * por lo que no puede construirse con un simple {@code @Inject} en el constructor) y
 * expone la {@link es.anescdev.velox.core.bus.BusKey} usada para pasar un {@code Customer}
 * entre pantallas a través del {@code Bus}.
 */
public class CustomerModule {
    public static final BusKey<Customer> CUSTOMER_KEY = new BusKey<>("customer", Customer.class);
    @Singleton
    @Provides
    CustomerDao providesCustomerDao(ConnectionSource source) throws DatabaseConnectionException {
        try {
            return new CustomerDao(source);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Error during database connection or DAO creation. Error: " + e.getMessage());
        }
    }
}
