package es.anescdev.velox.context.cod.model.entities;

import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.data.entities.CompositeKey;
import es.anescdev.velox.core.exceptions.NonExistentKeyException;

/**
 * @author AnesCDev
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
