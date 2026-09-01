package es.anescdev.velox;

import es.anescdev.velox.app.App;
import javafx.application.Application;

/**
 * Punto de entrada de la aplicación Velox.
 * Simplemente delega en JavaFX para lanzar {@link es.anescdev.velox.app.App},
 * que es quien realmente inicializa la inyección de dependencias, la base de
 * datos y la interfaz gráfica.
 */
public class Main {
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
     // TODO: Habilitar logging de sentencias SQL para así poder analizar las
     // sentencias y optimizarlas
}
