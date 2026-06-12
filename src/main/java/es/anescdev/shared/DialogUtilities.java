package es.anescdev.shared;

import javafx.application.Platform;
import javafx.scene.control.Dialog;

/**
 * Clase de utilidad que proporciona métodos para facilitar el manejo de
 * {@link javafx.scene.control.Dialog}
 */
public final class DialogUtilities {
    /**
     * Centra un díalogo al centro de la pantalla
     * 
     * @param dialog diálogo a centrar
     */
    public static void centerDialog(Dialog<?> dialog) {
        dialog.setOnShown((a) -> {
            Platform.runLater(
                ()->Platform.runLater(
                    () -> dialog.getDialogPane().getScene().getWindow().centerOnScreen()
                )
            );
        });
    }
}
