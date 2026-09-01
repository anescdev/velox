package es.anescdev.velox.core.data.datatypes;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.sql.SQLException;

/**
 * Persister a medida de ORMLite que permite guardar/leer directamente un
 * {@link javafx.beans.property.DoubleProperty} de JavaFX como columna {@code DOUBLE}.
 */
public class DoublePropertyDataType extends BaseDataType {

    private static final DoublePropertyDataType singleton = new DoublePropertyDataType();

    public static DoublePropertyDataType getSingleton() {
        return singleton;
    }

    private DoublePropertyDataType() {
        super(SqlType.DOUBLE, new Class<?>[] { DoubleProperty.class, SimpleDoubleProperty.class });
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Double.parseDouble(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getDouble(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return new SimpleDoubleProperty((Double) sqlArg);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) return null;
        return ((DoubleProperty) javaObject).get();
    }
}
