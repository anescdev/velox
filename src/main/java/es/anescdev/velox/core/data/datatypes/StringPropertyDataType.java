package es.anescdev.velox.core.data.datatypes;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.SQLException;

/**
 * Persister a medida de ORMLite que permite guardar/leer directamente un
 * {@link javafx.beans.property.StringProperty} de JavaFX como columna {@code TEXT}.
 */
public class StringPropertyDataType extends BaseDataType {

    private static final StringPropertyDataType singleton = new StringPropertyDataType();

    public static StringPropertyDataType getSingleton() {
        return singleton;
    }

    private StringPropertyDataType() {
        super(SqlType.STRING, new Class<?>[] { StringProperty.class, SimpleStringProperty.class });
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return defaultStr;
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getString(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return new SimpleStringProperty((String) sqlArg);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) return null;
        return ((StringProperty) javaObject).get();
    }
}
