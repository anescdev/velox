package es.anescdev.velox.context.customer.model.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.anescdev.velox.context.customer.data.dao.CustomerDao;
import es.anescdev.velox.core.data.entities.Identificable;
import es.anescdev.velox.core.model.DataEqualable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode.Exclude;

/**
 * @author AnesCDev
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@DatabaseTable(tableName = "customer", daoClass = CustomerDao.class)
public class Customer implements Identificable<Long>, DataEqualable<Customer> {
    public static final String ID_COLUMN = "customer_id";
    public static final String NAME_COLUMN = "customer_name";
    public static final String ABBREVIATION_COLUMN = "customer_abbreviation";

    @DatabaseField(columnName = ID_COLUMN, generatedId = true)
    private Long id;
    @Exclude
    @DatabaseField(columnName = NAME_COLUMN)
    private String name;
    @Exclude
    @DatabaseField(columnName = ABBREVIATION_COLUMN, unique = true, index = true)
    private String abbreviation;

    public Customer(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    @Override
    public boolean isDataEqual(Customer other) {
        return this.getId().equals(other.getId());
    }
}
