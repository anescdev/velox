package es.anescdev.sumatory.model.entities;

import java.time.Duration;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.anescdev.core.data.datatypes.DurationDataType;
import es.anescdev.sumatory.data.dao.SumatoryDao;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode.Exclude;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@DatabaseTable(tableName = "sumatory", daoClass = SumatoryDao.class)
public class Sumatory {
    @DatabaseField(generatedId = true)
    @Exclude
    private long id;

    @DatabaseField(uniqueCombo = true)
    private byte month;

    @DatabaseField(uniqueCombo = true)
    private short year;

    @DatabaseField(uniqueCombo = true)
    private String employee;

    @Exclude
    @DatabaseField(persisterClass = DurationDataType.class)
    private Duration total;

    public Sumatory(byte month, short year, String employee) {
        this(0, month, year, employee, Duration.ZERO);
    }
}
