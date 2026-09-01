package es.anescdev.velox.app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codejargon.feather.Feather;

import es.anescdev.velox.app.loader.controller.LoadController;
import es.anescdev.velox.app.loader.events.LoadEvent;
import es.anescdev.velox.app.logging.LoggingLoader;
import es.anescdev.velox.app.module.AppModule;
import es.anescdev.velox.context.customer.view.components.CustomerDetailsStatusBarSkin;
import es.anescdev.velox.context.customer.view.utils.CustomerDetailsStatusBarData;
import es.anescdev.velox.context.employee.view.controller.EmployeeSelectorController;
import es.anescdev.velox.context.sumatory.view.components.SumatoryStatusBarSkin;
import es.anescdev.velox.context.sumatory.view.utils.SumatoryStatusBarData;
import es.anescdev.velox.core.CoreModule;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.view.components.statusbar.CustomStatusBar;
import es.anescdev.velox.core.view.controller.BaseController;
import es.anescdev.velox.core.view.utils.StringUtilities;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Hello world!
 */
public class App extends Application {
	private ResourceBundle resourceBundle;// TODO: refactor para eliminar el static
	private Stage main;
	private static App instance;
	private Logger logger;

	private Feather feather;
	private SimpleStringProperty title = new SimpleStringProperty("");

	@Override
	public void init() throws Exception {
		this.resourceBundle = ResourceBundle.getBundle("i18n/messages", Locale.getDefault()); // 3
																								// que se obtenga desde
																								// feather, con todos
																								// los que usa esta
																								// instancia estática
		App.instance = this;
		String[] fonts = {
				// Plus Jakarta Sans
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Bold.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-BoldItalic.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-ExtraBold.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-ExtraBoldItalic.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-ExtraLight.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-ExtraLightItalic.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Italic.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Light.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-LightItalic.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Medium.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-MediumItalic.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-Regular.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-SemiBold.ttf",
				"/fonts/Plus_Jakarta_Sans/static/PlusJakartaSans-SemiBoldItalic.ttf",

				// Inter
				"/fonts/Inter/static/Inter_18pt-Bold.ttf",
				"/fonts/Inter/static/Inter_18pt-BoldItalic.ttf",
				"/fonts/Inter/static/Inter_18pt-ExtraBold.ttf",
				"/fonts/Inter/static/Inter_18pt-ExtraBoldItalic.ttf",
				"/fonts/Inter/static/Inter_18pt-ExtraLight.ttf",
				"/fonts/Inter/static/Inter_18pt-ExtraLightItalic.ttf",
				"/fonts/Inter/static/Inter_18pt-Italic.ttf",
				"/fonts/Inter/static/Inter_18pt-Light.ttf",
				"/fonts/Inter/static/Inter_18pt-LightItalic.ttf",
				"/fonts/Inter/static/Inter_18pt-Medium.ttf",
				"/fonts/Inter/static/Inter_18pt-MediumItalic.ttf",
				"/fonts/Inter/static/Inter_18pt-Regular.ttf",
				"/fonts/Inter/static/Inter_18pt-SemiBold.ttf",
				"/fonts/Inter/static/Inter_18pt-SemiBoldItalic.ttf",
				"/fonts/Inter/static/Inter_18pt-Thin.ttf",
				"/fonts/Inter/static/Inter_18pt-ThinItalic.ttf",

				// JetBrains Mono
				"/fonts/JetBrainsMono/static/JetBrainsMono-Bold.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-BoldItalic.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-ExtraBold.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-ExtraBoldItalic.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-ExtraLight.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-ExtraLightItalic.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-Italic.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-Light.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-LightItalic.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-Medium.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-MediumItalic.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-Regular.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-SemiBold.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-SemiBoldItalic.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-Thin.ttf",
				"/fonts/JetBrainsMono/static/JetBrainsMono-ThinItalic.ttf"
		};

		for (String font : fonts) {
			var loaded = Font.loadFont(getClass().getResourceAsStream(font), 13);
			System.out.println(font + " -> " + (loaded != null ? loaded.getName() : "NULL (falló la carga)"));
		}
		Application.setUserAgentStylesheet(null);
		CustomStatusBar.registerDataSkin(SumatoryStatusBarSkin.class, SumatoryStatusBarData.class);
		CustomStatusBar.registerDataSkin(CustomerDetailsStatusBarSkin.class, CustomerDetailsStatusBarData.class);
		this.feather = Feather.with(this.searchAppModules());
		super.init();
	}

	@Override
	public void stop() throws Exception {
		this.feather = null;
		this.resourceBundle = null;
		super.stop();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.getIcons().addAll(
				new Image(getClass().getResourceAsStream("/img/icons/icon_16.png")),
				new Image(getClass().getResourceAsStream("/img/icons/icon_32.png")),
				new Image(getClass().getResourceAsStream("/img/icons/icon_48.png")),
				new Image(getClass().getResourceAsStream("/img/icons/icon_64.png")),
				new Image(getClass().getResourceAsStream("/img/icons/icon_128.png")),
				new Image(getClass().getResourceAsStream("/img/icons/icon_256.png")));
		primaryStage.addEventHandler(LoggingLoader.LOGGING_LOAD_EVENT_TYPE, event -> {
			this.initLogger();
			this.logger.config("Logger has been configured");
		});

		primaryStage.addEventHandler(LoadEvent.COMPLETED_EVENT, event -> {
			Platform.runLater(() -> {
				this.main.close();
				primaryStage.titleProperty().bind(
						new When(this.title.isEmpty())
								.then(this.resourceBundle.getString("app.name"))
								.otherwise(
										ReadOnlyStringProperty.stringExpression(this.title
												.<String>map(titleString -> this.resourceBundle.getString("app.name")
														+ " - " + titleString))));
				var employeeSelector = App.<BorderPane, EmployeeSelectorController>loadFXML("employee/selector");
				var data = new Bus();
				var mainScene = new Scene(employeeSelector.node());
				mainScene.setUserAgentStylesheet(getClass().getResource("/styles/main.css").toExternalForm());
				mainScene.getStylesheets()
						.addAll(
								getClass().getResource("/styles/main.css").toExternalForm(),
								getClass().getResource("/styles/notifications-override.css").toExternalForm());
				data.setData(EmployeeSelectorController.SCENE_KEY, mainScene);
				employeeSelector.controller().initData(data);
				primaryStage.setScene(mainScene);
				primaryStage.toFront();
				primaryStage.centerOnScreen();
				primaryStage.setMaximized(true);
				primaryStage.show();
				this.main = primaryStage;
			});
		});

		var loadStage = this.createLoadStage(primaryStage);
		loadStage.show();
		this.main = loadStage;
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
	public static <T, C extends BaseController> LoadFXMLResult<T, C> loadFXML(String resourcePath)
			throws LoadFXMLException {
		if (App.instance().resourceBundle == null)
			throw new RuntimeException("The resource bundle cannot be loaded");
		try {
			FXMLLoader loader = new FXMLLoader(
					App.class.getClassLoader().getResource(String.format("scenes/%s.fxml", resourcePath)),
					App.instance().resourceBundle);
			loader.setControllerFactory(App.instance().feather::instance);
			T node = loader.<T>load();
			return new LoadFXMLResult<T, C>(node, loader.<C>getController());
		} catch (IOException exception) {
			throw new LoadFXMLException(exception.getLocalizedMessage(), exception.getCause());
		}
	}

	public static App instance() {
		return App.instance;
	}

	public Stage getMainStage() {
		return this.main;
	}

	public ResourceBundle getResourceBundle() {
		return this.resourceBundle;
	}

	public String getMessage(String key) {
		try {
			return this.resourceBundle.getString(key);
		} catch (Exception singleKeyException) {
			return Stream.of(key.split(" "))
					.map(keyPart -> {
						try {
							if (keyPart.startsWith("%"))
								return this.resourceBundle.getString(keyPart.substring(1));
							else
								return keyPart;
						} catch (Exception composedKeyException) {
							return keyPart;

						}
					})
					.collect(Collectors.joining(" "));
		}
	}

	public Feather getFeather() {
		return this.feather;
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public Logger getLogger() {
		return this.logger;
	}

	public void initLogger() {
		if (this.logger != null) {
			return;
		}
		this.logger = Logger.getLogger("Velox");

	}

	private Stage createLoadStage(Stage parentStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				App.class.getClassLoader().getResource("scenes/load.fxml"),
				this.resourceBundle);
		loader.setControllerFactory(App.instance().feather::instance);
		Stage loadStage = new Stage(StageStyle.UNDECORATED);
		var loadScene = new Scene(loader.<AnchorPane>load());
		// loadScene.setUserAgentStylesheet(getClass().getResource("/styles/main.css").toExternalForm());
		loadScene.getStylesheets()
				.addAll(
						getClass().getResource("/styles/main.css").toExternalForm());
		loader.<LoadController>getController().setLoadStage(parentStage);
		loadStage.setScene(loadScene);
		loadStage.initModality(Modality.APPLICATION_MODAL);
		loadStage.initOwner(parentStage);
		loadStage.centerOnScreen();
		return loadStage;
	}

	public List<Object> searchAppModules() throws IllegalAccessException {
		List<Object> modules = new ArrayList<>();
		CoreModule coreModule = new CoreModule();
		modules.add(coreModule);
		try {
			String packagePath = "es/anescdev/velox/context";
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL resource = classLoader.getResource(packagePath);

			if (resource == null) {
				System.err.println("[Velox] No se pudo encontrar la ruta del recurso.");
				return List.of();
			}

			// Usamos un Set para almacenar los nombres de los directorios internos de forma
			// única
			Set<String> subFolderNames = new HashSet<>();

			// CASO 1: Ejecución desde un archivo JAR
			if (resource.getProtocol().equals("jar")) {
				JarURLConnection conn = (JarURLConnection) resource.openConnection();
				try (JarFile jar = conn.getJarFile()) {
					Enumeration<JarEntry> entries = jar.entries();
					while (entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						String name = entry.getName();

						// Buscamos elementos que estén dentro de nuestro paquete objetivo
						if (name.startsWith(packagePath + "/") && !name.equals(packagePath + "/")) {
							// Extraemos solo el nombre de la subcarpeta inmediata (ej: de
							// "es/.../context/auth/AuthModule.class" sacamos "auth")
							String relativePath = name.substring(packagePath.length() + 1);
							int firstSlash = relativePath.indexOf('/');
							if (firstSlash != -1) {
								subFolderNames.add(relativePath.substring(0, firstSlash));
							}
						}
					}
				}
			}
			// CASO 2: Ejecución desde entorno de desarrollo (IDE / Archivos sueltos)
			else {
				File contextPackage = new File(resource.toURI());
				if (!contextPackage.exists() || !contextPackage.isDirectory()) {
					return List.of();
				}
				File[] files = contextPackage.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isDirectory()) {
							subFolderNames.add(file.getName());
						}
					}
				}
			}

			// Procesamos cada subcarpeta/módulo encontrado de forma unificada
			for (String folderName : subFolderNames) {
				String moduleFileName = StringUtilities.toFirstUpper(folderName + "Module");
				Constructor<?> moduleConstructor;
				try {
					Class<?> clazz = Class.forName("es.anescdev.velox.context." + folderName + "." + moduleFileName);
					if (!clazz.isAnnotationPresent(AppModule.class)) {
						continue;
					}

					moduleConstructor = clazz.getDeclaredConstructor();
					moduleConstructor.setAccessible(true);
					modules.add(moduleConstructor.newInstance());
					moduleConstructor.setAccessible(false);
				} catch (ClassNotFoundException e) {
					System.err.println("[Velox] Cannot found the module " + moduleFileName);
				} catch (InstantiationException e) {
					System.err.println("[Velox] The module " + moduleFileName + " need to be a instantiable class");
				} catch (IllegalArgumentException e) {
					System.err.println("[Velox] The module " + moduleFileName + " dont have a no-args constructor");
				} catch (InvocationTargetException e) {
					System.err.println("[Velox] Occurs an error while instantiating " + moduleFileName + " module");
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					System.err.println("[Velox] The module " + moduleFileName + " dont have a no-args constructor");
				}
			}

		} catch (Exception e) {
			System.err.println("[Velox] Cannot load the modules folder");
			e.printStackTrace();
			return List.of(coreModule);
		}

		return modules;
	}
}
