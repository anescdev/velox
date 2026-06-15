package es.anescdev;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.codejargon.feather.Feather;

import es.anescdev.sumatory.SumatoryModule;
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
	private static Stage main;
	private static App instance;
	private Feather feather;
	
	
	@Override
	public void init() throws Exception {
		App.resourceBundle = ResourceBundle.getBundle("i18n/messages", Locale.getDefault());
		App.instance = this;
		this.feather = Feather.with(new AppModule(this.getParameters()), new SumatoryModule());
		super.init();
	}

	@Override
	public void stop() throws Exception {
		this.feather = null;
		App.resourceBundle = null;
		super.stop();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		var scene = new Scene(App.<VBox>loadFXML("workspace"));
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
			FXMLLoader loader = new FXMLLoader(App.class.getClassLoader().getResource(String.format("scenes/%s.fxml", resourcePath)), App.resourceBundle);
			loader.setControllerFactory(type -> App.instance().feather.instance(type));
			return loader.load();
		} catch (IOException exception) {
			throw new LoadFXMLException(exception.getLocalizedMessage(), exception.getCause());
		}
	}
	
	public static App instance() {
		return App.instance;
	}
	public static Stage getMainStage() {
		return App.main;
	}
	
	public static void main(String[] args) {
        App.launch(args);
    }
}
