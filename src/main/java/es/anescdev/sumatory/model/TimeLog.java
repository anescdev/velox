package es.anescdev.sumatory.model;

import java.time.Duration;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.anescdev.core.persistence.datatypes.DurationDataType;
import es.anescdev.sumatory.persistence.dao.TimeLogDao;
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
@DatabaseTable(tableName = "time_log", daoClass = TimeLogDao.class)
public class TimeLog {
    @Exclude
    @DatabaseField(generatedId = true)
    private long id;

    @Exclude
    @DatabaseField(columnName = "sumatory_id") //TODO: vincularlo al sumatory para que no se pueda
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
