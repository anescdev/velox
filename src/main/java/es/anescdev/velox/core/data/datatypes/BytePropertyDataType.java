package es.anescdev.velox.core.data.datatypes; // <-- Nombre del paquete corregido aquí

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.sql.SQLException;

/**
 * Persister a medida de ORMLite que mapea un valor de tipo {@code byte} envuelto en una
 * {@link javafx.beans.property.IntegerProperty} a una columna SQLite.
 */
public class BytePropertyDataType extends BaseDataType {

    private static final BytePropertyDataType singleton = new BytePropertyDataType();

    public static BytePropertyDataType getSingleton() {
        return singleton;
    }

    private BytePropertyDataType() {
        super(SqlType.BYTE, new Class<?>[] { IntegerProperty.class, SimpleIntegerProperty.class });
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Byte.parseByte(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getByte(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        int val = ((Byte) sqlArg).intValue();
        return new SimpleIntegerProperty(val);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) return null;
        return (byte) ((IntegerProperty) javaObject).get();
    }
}
