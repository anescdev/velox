package es.anescdev.velox.context.cod.model.entities;

import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.data.entities.CompositeKey;
import es.anescdev.velox.core.exceptions.NonExistentKeyException;

/**
 * Clave compuesta lógica de {@link Cod}: combina el id interno autogenerado, el código en
 * sí, el empleado y el cliente. {@link #getColumnValue(String)} permite a
 * {@link es.anescdev.velox.core.data.repositories.ORMLiteCompositeKeyAbstractRepository}
 * construir las condiciones {@code WHERE} sin conocer los detalles de esta entidad.
 */
public record CodKey(Long internalId, String cod, Employee employee, Customer customer) implements CompositeKey<Long> {
    public final static String INTERNAL_ID_COLUMN = "cod_internal_id";
    public final static String COD_COLUMN = "cod_id";
    public final static String EMPLOYEE_COLUMN = "employee_id";
    public final static String CUSTOMER_COLUMN = "customer_id";

    @Override
    public Object getColumnValue(String columnName) {
        switch (columnName) {
            case COD_COLUMN:
                return cod();
            case EMPLOYEE_COLUMN:
                return employee().getId();
            case CUSTOMER_COLUMN:
                return customer().getId();
            default:
                throw new NonExistentKeyException(columnName);
        }
    }

    @Override
    public Long getInternalId() {
        return this.internalId();
    }

}
