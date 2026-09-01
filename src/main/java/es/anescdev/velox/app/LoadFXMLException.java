package es.anescdev.velox.app;

/**
 * Excepción lanzada cuando {@link es.anescdev.velox.app.App#loadFXML(String)} no consigue
 * cargar un fichero FXML (por ejemplo, ruta incorrecta o error de parseo).
 */
public class LoadFXMLException extends RuntimeException {

	private static final long serialVersionUID = 1101L;
	
	public LoadFXMLException(String message, Throwable cause) {
		super(message, cause);
	}
}
