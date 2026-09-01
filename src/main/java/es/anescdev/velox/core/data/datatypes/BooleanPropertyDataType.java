package es.anescdev.velox.core.data.datatypes;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.sql.SQLException;

public class BooleanPropertyDataType extends BaseDataType {

    private static final BooleanPropertyDataType singleton = new BooleanPropertyDataType();

    public static BooleanPropertyDataType getSingleton() {
        return singleton;
    }

    private BooleanPropertyDataType() {
        super(SqlType.BOOLEAN, new Class<?>[] { BooleanProperty.class, SimpleBooleanProperty.class });
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Boolean.parseBoolean(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getBoolean(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return new SimpleBooleanProperty((Boolean) sqlArg);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) return null;
        return ((BooleanProperty) javaObject).get();
    }
}
