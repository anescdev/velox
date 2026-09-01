package es.anescdev.velox.core.view.utils;

import javafx.application.Platform;
import javafx.scene.control.Dialog;

/**
 * Utilidad de la capa de vista del dominio None (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public final class DialogUtilities {
    public DialogUtilities() {

    }

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
