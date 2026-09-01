package es.anescdev.velox.core.data.datatypes;

import java.sql.SQLException;
import java.time.Duration;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

/**
 * Persister a medida de ORMLite que permite guardar un {@link java.time.Duration}
 * (usado por ejemplo en {@code Cod.timeWorked} y {@code Sumatory.total}) como un entero
 * {@code LONG} (segundos/nanos), ya que ORMLite no soporta este tipo de forma nativa.
 */
public class DurationDataType extends BaseDataType {

    private static DurationDataType instance = new DurationDataType();

    public static DurationDataType getSingleton() {
        return instance;
    }

    protected DurationDataType() {
        super(SqlType.LONG, new Class<?>[] { Duration.class });
    }

    /**
     * @param sqlType
     * @param classes
     */
    public DurationDataType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        return Long.parseLong(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getLong(columnPos);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        return ((Duration) javaObject).toMillis();
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        return Duration.ofMillis((long) sqlArg);
    }
}
