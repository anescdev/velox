package es.anescdev.velox.core.data.datatypes; // <-- Nombre del paquete corregido aquí

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.sql.SQLException;

public class ShortPropertyDataType extends BaseDataType {

    private static final ShortPropertyDataType singleton = new ShortPropertyDataType();

    public static ShortPropertyDataType getSingleton() {
        return singleton;
    }

    private ShortPropertyDataType() {
        super(SqlType.BYTE, new Class<?>[] { IntegerProperty.class, SimpleIntegerProperty.class });
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Short.parseShort(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getShort(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        int val = ((Short) sqlArg).intValue();
        return new SimpleIntegerProperty(val);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) return null;
        return (byte) ((IntegerProperty) javaObject).get();
    }
}
