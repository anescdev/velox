package es.anescdev.velox.core.view.utils;

import org.controlsfx.control.Notifications;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;
import org.kordamp.ikonli.unicons.UniconsSolid;

import es.anescdev.velox.app.App;
import javafx.application.Platform;
import javafx.scene.paint.Color;

/**
 * Utilidad de la capa de vista del dominio None (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public class NotificationsUtils {
    public static void showInformation(String title, String text) {
        var fontIcon = new FontIcon(UniconsLine.INFO_CIRCLE);
        fontIcon.setIconSize(50);
        fontIcon.setIconColor(Color.web("#4F46E5"));

        Platform.runLater(() -> {
            getBasicBuilder(title, text)
                    .graphic(fontIcon)
                    .show();
        });
    }

    public static void showConfirm(String title, String text) {
        var fontIcon = new FontIcon(UniconsLine.QUESTION_CIRCLE);
        fontIcon.setIconSize(50);
        fontIcon.setIconColor(Color.web("#4676e5"));

        Platform.runLater(() -> {
            getBasicBuilder(title, text)
                    .graphic(fontIcon)
                    .show();
        });
    }

    public static void showWarning(String title, String text) {
        var fontIcon = new FontIcon(UniconsSolid.EXCLAMATION_TRIANGLE);
        fontIcon.setIconSize(50);
        fontIcon.setIconColor(Color.web("#e7e319"));

        Platform.runLater(() -> {
            getBasicBuilder(title, text)
                    .graphic(fontIcon)
                    .show();
        });
    }

    public static void showError(String title, String text) {
        var fontIcon = new FontIcon(UniconsSolid.TIMES_CIRCLE);
        fontIcon.setIconSize(50);
        fontIcon.setIconColor(Color.web("#DC2626"));

        Platform.runLater(() -> {
            getBasicBuilder(title, text)
                    .graphic(fontIcon)
                    .show();
        });
    }

    private static Notifications getBasicBuilder(String title, String text) {
        return Notifications
                .create()
                .owner(App.instance().getMainStage())
                .title(title)
                .text(text);
    }
}
