package es.anescdev.velox.core.data.datatypes;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import java.sql.SQLException;

/**
 * Persister a medida de ORMLite que permite guardar/leer directamente un
 * {@link javafx.beans.property.LongProperty} de JavaFX como columna {@code LONG}.
 */
public class LongPropertyDataType extends BaseDataType {

    private static final LongPropertyDataType singleton = new LongPropertyDataType();

    public static LongPropertyDataType getSingleton() {
        return singleton;
    }

    private LongPropertyDataType() {
        super(SqlType.LONG, new Class<?>[] { LongProperty.class, SimpleLongProperty.class });
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Long.parseLong(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getLong(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return new SimpleLongProperty((Long) sqlArg);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) return null;
        return ((LongProperty) javaObject).get();
    }
}
