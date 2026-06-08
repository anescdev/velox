package es.anescdev;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Hello world!
 */
public class App extends Application {
	private static ResourceBundle resourceBundle;
	private static App instance;
	
	
	@Override
	public void init() throws Exception {
		App.resourceBundle = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
		App.instance = this;
		super.init();
	}

	@Override
	public void stop() throws Exception {
		App.resourceBundle = null;
		super.stop();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		var scene = new Scene(App.<VBox>loadFXML("main"));
		primaryStage.setTitle(App.resourceBundle.getString("app.name"));
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.toFront();
		primaryStage.show();
	}
	/**
	 * Carga un fichero FXML para poder tratarlo dentro del programa
	 * @param <T> tipo del elemento raiz del FXML cargado
	 * @param resourcePath ruta al recurso FXML. Importante que esté dentro de la carpeta de recurso
	 * @return Nodo de JavaFX que ha obtenido en el FXML
	 * @throws LoadFXMLException 
	 */
	public static <T>  T loadFXML(String resourcePath) throws LoadFXMLException {
		if (App.resourceBundle == null) throw new RuntimeException("The resource bundle cannot be loaded");
		try {
			return FXMLLoader.<T>load(App.class.getClassLoader().getResource(String.format("scenes/%s.fxml", resourcePath)), App.resourceBundle);
		} catch (IOException exception) {
			throw new LoadFXMLException(exception.getMessage());
		}
	}
	
	public static App instance() {
		return App.instance;
	}
	
	public static void main(String[] args) {
        App.launch(args);
    }
}
