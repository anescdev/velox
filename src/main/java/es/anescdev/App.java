package es.anescdev;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.codejargon.feather.Feather;

import com.j256.ormlite.field.DataPersisterManager;

import es.anescdev.core.CoreModule;
import es.anescdev.core.data.datatypes.DurationDataType;
import es.anescdev.core.view.controller.BaseController;
import es.anescdev.core.view.controller.WorkspaceController;
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
	private static ResourceBundle resourceBundle;//TODO: refactor para eliminar el static
	private static Stage main;
	private static App instance;
	
	private Feather feather;

	@Override
	public void init() throws Exception {
		App.resourceBundle = ResourceBundle.getBundle("i18n/messages", Locale.getDefault()); //TODO: Eliminar esto y que se obtenga desde feather, con todos los que usa esta instancia estática
		App.instance = this;
		this.feather = Feather.with(new CoreModule(this.getParameters()), new SumatoryModule());
		DataPersisterManager.registerDataPersisters(DurationDataType.getSingleton());
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
		var scene = new Scene(App.<VBox, WorkspaceController>loadFXML("workspace").node());
		scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
		primaryStage.setTitle(App.resourceBundle.getString("app.name"));
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.toFront();
		primaryStage.show();
		main = primaryStage;
	}

	/**
	 * Carga un fichero FXML para poder tratarlo dentro del programa
	 * 
	 * @param <T>          tipo del elemento raiz del FXML cargado
	 * @param resourcePath ruta al recurso FXML. Importante que esté dentro de la
	 *                     carpeta de recurso
	 * @return Nodo de JavaFX que ha obtenido en el FXML
	 * @throws LoadFXMLException
	 */
	public static <T, C extends BaseController> LoadFXMLResult<T, C> loadFXML(String resourcePath) throws LoadFXMLException {
		if (App.resourceBundle == null)
			throw new RuntimeException("The resource bundle cannot be loaded");
		try {
			FXMLLoader loader = new FXMLLoader(
					App.class.getClassLoader().getResource(String.format("scenes/%s.fxml", resourcePath)),
					App.resourceBundle);
			loader.setControllerFactory(App.instance().feather::instance);
			T node = loader.<T>load();
			return new LoadFXMLResult<T,C>(node, loader.<C>getController());
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

	public static ResourceBundle getResourceBundle() {
		return App.resourceBundle;
	}
	public Feather getFeather() {
		return this.feather;
	}

	public static void main(String[] args) {
		App.launch(args);
	}
}
