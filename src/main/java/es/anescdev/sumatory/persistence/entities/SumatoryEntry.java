package es.anescdev.sumatory.persistence.entities;

import java.time.Duration;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.anescdev.shared.infrastructure.persistence.DurationDataType;
import es.anescdev.sumatory.persistence.dao.SumatoryEntryDaoImpl;
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
@DatabaseTable(tableName = "sumatory_entry", daoClass = SumatoryEntryDaoImpl.class)
public class SumatoryEntry {
    @Exclude
    @DatabaseField(generatedId = true)
    private long id;

    @Exclude
    @DatabaseField(columnName = "sumatory_id")
    private long sumatoryId;

    //TODO: Esto hay que refactorizarlo cuando vayamos a optimizar el programa. Es la parte del "codigo de mi padre para los días de la semana"
    @DatabaseField()
    private String cod; 

    @DatabaseField()
    private String customer;

    @Exclude
    @DatabaseField(persisterClass = DurationDataType.class, columnName = "time_worked")
    private Duration timeWorked;

}
