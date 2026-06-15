package es.anescdev.shared.domain.providers;

import java.sql.Connection;

import es.anescdev.shared.domain.exceptions.DatabaseConnectionException;

public /**
 * @author AnesCDev
 */
interface DatabaseConnectionProvider {
    /**
     * Obtiene la conexión actual a la base de datos o la crea si no está
     * @return Conexión a la base de datos {@link java.sql.Connection}
     * @throws DatabaseConnectionException Excepción lanzada cuando no ha sido posible conectarse a la base de datos
     */
    public Connection getDatabaseConnection() throws DatabaseConnectionException ;
}
