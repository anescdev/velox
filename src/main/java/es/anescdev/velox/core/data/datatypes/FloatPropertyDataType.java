package es.anescdev.velox.core.data.datatypes;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import java.sql.SQLException;

public class FloatPropertyDataType extends BaseDataType {

    private static final FloatPropertyDataType singleton = new FloatPropertyDataType();

    public static FloatPropertyDataType getSingleton() {
        return singleton;
    }

    private FloatPropertyDataType() {
        super(SqlType.FLOAT, new Class<?>[] { FloatProperty.class, SimpleFloatProperty.class });
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Float.parseFloat(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getFloat(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return new SimpleFloatProperty((Float) sqlArg);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) return null;
        return ((FloatProperty) javaObject).get();
    }
}
