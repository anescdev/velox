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

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@DatabaseTable(tableName = "employee", daoClass = EmployeeDao.class)
/**
 * Empleado que usa la aplicación. No hay autenticación por contraseña: la pantalla de
 * selector de empleado ({@code EmployeeSelectorController}) simplemente elige entre los
 * empleados existentes (o crea uno nuevo) para asociar todo el trabajo registrado en la
 * sesión a esa persona.
 */
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
