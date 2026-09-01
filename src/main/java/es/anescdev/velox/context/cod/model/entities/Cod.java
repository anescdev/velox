package es.anescdev.velox.context.cod.model.entities;

import java.time.Duration;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.anescdev.velox.context.cod.data.dao.CodDao;
import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.data.datatypes.DurationDataType;
import es.anescdev.velox.core.data.entities.Identificable;
import es.anescdev.velox.core.model.DataEqualable;
import es.anescdev.velox.core.model.EntityState;
import es.anescdev.velox.core.model.StateableEntity;
import es.anescdev.velox.core.utils.LombokableBuilder;
import javafx.util.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode.Include;

/**
 * @author AnesCDev
 */
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "cod", daoClass = CodDao.class)
@lombok.Builder
public class Cod extends StateableEntity implements Identificable<CodKey>, DataEqualable<Cod>, LombokableBuilder<Cod> {
    @Getter
    @Setter
    @DatabaseField(columnName = CodKey.INTERNAL_ID_COLUMN, generatedId = true)
    @Include
    private Long internalId;

    @Getter
    @Setter
    @DatabaseField(
        columnName = CodKey.EMPLOYEE_COLUMN, 
        columnDefinition = "INTEGER REFERENCES employee(" + Employee.ID_COLUMN + ") ON DELETE CASCADE",
        uniqueCombo = true, 
        foreign = true, 
        canBeNull = false, 
        indexName =  "emp_cus_cod_idx")
    private Employee employee;

    @Getter
    @Setter
    @DatabaseField(
        columnName = CodKey.CUSTOMER_COLUMN, 
        columnDefinition = "INTEGER REFERENCES customer(" + Customer.ID_COLUMN + ") ON DELETE CASCADE",
        uniqueCombo = true, foreign = true, 
        foreignAutoRefresh = true, 
        canBeNull = false, 
        indexName =  "emp_cus_cod_idx")
    private Customer customer;

    @Getter
    @Setter
    @DatabaseField(
        columnName = CodKey.COD_COLUMN, 
        uniqueCombo = true, 
        canBeNull = false, 
        indexName =  "emp_cus_cod_idx")
    private String cod;

    @Getter
    @Setter
    @DatabaseField(columnName = "cod_description")
    private String description;

    @Getter
    @Setter
    @DatabaseField(columnName = "time_worked", persisterClass = DurationDataType.class)
    private Duration timeWorked;

    public Cod() {
        super(EntityState.SAVED);
    }

    public Cod(Long internalId, String cod, Customer customer, Employee employee, String description,
            Duration timeWorked, EntityState state) {
        super(state);
        this.internalId = internalId;
        this.cod = cod;
        this.customer = customer;
        this.employee = employee;
        this.description = description;
        this.timeWorked = timeWorked;
    }

    public Cod(Long internalId, Employee employee, Customer customer, String cod, String description,
            Duration timeWorked) {
                this(internalId, cod, customer, employee, description, timeWorked);
            }

    public Cod(Long internalId, String cod, Customer customer, Employee employee, String description,
            Duration timeWorked) {
        this(internalId, cod, customer, employee, description, timeWorked, EntityState.SAVED);
    }

    public static Cod draftCod(Long internalId, String cod, Customer customer, Employee employee, String description,
            Duration timeWorked) {
        return new Cod(internalId, cod, customer, employee, description, timeWorked, EntityState.DRAFT);
    }

    public static Cod savedCod(Long internalId, String cod, Customer customer, Employee employee, String description,
            Duration timeWorked) {
        return new Cod(internalId, cod, customer, employee, description, timeWorked, EntityState.SAVED);
    }

    @Override
    public CodKey getId() {
        return new CodKey(this.internalId, this.cod, this.employee, this.customer);
    }

    @Override
    public boolean isDataEqual(Cod other) {
        return (this.cod == null ? this.cod == other.getCod() : this.cod.equalsIgnoreCase(other.getCod())) &&
                (this.getCustomer() == null ? this.getCustomer() == other.getCustomer()
                        : this.getCustomer().isDataEqual(other.getCustomer()))
                &&
                (this.getEmployee() == null ? this.getEmployee() == other.getEmployee()
                        : this.getEmployee().isDataEqual(other.getEmployee()));
    }

    @Override
    public Builder<Cod> toBuilder() {
        return Cod.builder().internalId(internalId).cod(cod).customer(customer).employee(employee)
                .description(description).timeWorked(timeWorked);
    }

    public String getCompleteCod() {
        if (this.customer == null)
            return "";
        return this.customer.getAbbreviation() + "-" + this.cod;
    }

    public static class CodBuilder implements Builder<Cod> {

    }

}
