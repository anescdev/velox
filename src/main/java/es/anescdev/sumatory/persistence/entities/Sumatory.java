package es.anescdev.sumatory.persistence.entities;

import java.time.Duration;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.anescdev.shared.infrastructure.persistence.DurationDataType;
import es.anescdev.sumatory.persistence.dao.SumatoryDaoImpl;
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
@DatabaseTable(tableName = "sumatory", daoClass = SumatoryDaoImpl.class)
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
}
