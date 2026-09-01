package es.anescdev.velox.core.data.datatypes;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.sql.SQLException;

public class IntegerPropertyDataType extends BaseDataType {

    private static final IntegerPropertyDataType singleton = new IntegerPropertyDataType();

    public static IntegerPropertyDataType getSingleton() {
        return singleton;
    }

    private IntegerPropertyDataType() {
        super(SqlType.INTEGER, new Class<?>[] { IntegerProperty.class, SimpleIntegerProperty.class });
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Integer.parseInt(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getInt(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return new SimpleIntegerProperty((Integer) sqlArg);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) return null;
        return ((IntegerProperty) javaObject).get();
    }
}
