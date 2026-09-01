package es.anescdev.velox.context.sumatory.model.entity;

import java.time.Duration;
import java.time.LocalDate;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.sumatory.data.dao.SumatoryDao;
import es.anescdev.velox.core.data.datatypes.DurationDataType;
import es.anescdev.velox.core.data.entities.Identificable;
import es.anescdev.velox.core.model.DataEqualable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode.Include;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DatabaseTable(tableName = "sumatory", daoClass = SumatoryDao.class)
/**
 * Resumen mensual de horas trabajadas por un empleado (cabecera). Único por la
 * combinación (mes, año, empleado). {@code total} se recalcula a partir de la suma de las
 * duraciones de sus {@link es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry}.
 * Al borrarse un {@code Employee}, sus sumatorios se borran en cascada (ver {@code ON DELETE CASCADE}
 * en la columna del empleado).
 */
public class Sumatory implements Identificable<Long>, DataEqualable<Sumatory> {

        public static final String ID_COLUMN = "sumatory_id",
                        MONTH_COLUMN = "month",
                        YEAR_COLUMN = "year",
                        EMPLOYEE_COLUMN = "employee_id",
                        DURATION_COLUMN = "total";

        @Include
        @DatabaseField(generatedId = true, columnName = ID_COLUMN)
        private Long id;

        @DatabaseField(columnName = MONTH_COLUMN, uniqueCombo = true)
        private byte month;

        @DatabaseField(columnName = YEAR_COLUMN, uniqueCombo = true)
        private short year;

        @DatabaseField(
                columnDefinition = "INTEGER REFERENCES employee(" + Employee.ID_COLUMN + ") ON DELETE CASCADE", 
                columnName = EMPLOYEE_COLUMN, 
                uniqueCombo = true,
                foreign = true, 
                foreignAutoRefresh = true,
                index = true)
        private Employee employee;

        @DatabaseField(columnName = DURATION_COLUMN, persisterClass = DurationDataType.class)
        private Duration total;

        public Sumatory(byte month, short year, Employee employee) {
                this(null, month, year, employee, Duration.ZERO);
        }

        @Override
        public boolean isDataEqual(Sumatory other) {
                return this.id == null ? this.id == other.id
                                : this.id.equals(other.id) &&
                                                this.month == other.month &&
                                                this.year == other.year &&
                                                this.employee == null ? this.employee == other.employee
                                                                : this.employee.isDataEqual(other.employee) &&
                                                                                this.total == null
                                                                                                ? this.total == other.total
                                                                                                : this.total.equals(
                                                                                                                other.total);
        }

        public LocalDate getFullDate() {
                return LocalDate.of(this.getYear(), this.getMonth(), 1);
        }
}
