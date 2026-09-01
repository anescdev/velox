package es.anescdev.velox.context.employee.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.anescdev.velox.context.employee.data.dao.EmployeeDao;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@DatabaseTable(tableName = "employee", daoClass = EmployeeDao.class)
public class Employee implements Identificable<Long>, DataEqualable<Employee> {
    public static final String ID_COLUMN = "id_employee";

    @DatabaseField(columnName = ID_COLUMN, generatedId = true)
    private Long id;

    @Exclude
    @DatabaseField(columnName = "name_employee")
    private String name;

    public Employee(String name) {
        this(null, name);
    }

    @Override
    public boolean isDataEqual(Employee other) {
        return this.getId().equals(other.getId());
    }

}
