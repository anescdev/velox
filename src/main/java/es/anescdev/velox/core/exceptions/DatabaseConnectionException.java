package es.anescdev.velox.core.exceptions;

import java.sql.SQLException;

/** Error al conectar con la base de datos SQLite (fichero no accesible, URL inválida, etc.). */
public class DatabaseConnectionException extends SQLException{
    public DatabaseConnectionException(String message) {
        super(message);
    }
}