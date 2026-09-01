package es.anescdev.velox.core.view.components.monthyearpicker;

import java.time.LocalDate;

import org.kordamp.ikonli.javafx.FontIcon;

import es.anescdev.velox.core.view.components.ComboBox;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxBaseSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.StringConverter;

/**
 * @author AnesCDev
 */
public class MonthYearPickerSkin extends ComboBoxBaseSkin<LocalDate> {
    private final MonthYearPickerPopup popupContent;
    private final TextField fakeEditor = new TextField();
    private final Popup customPopup = new Popup();

    private long lastAutoHide = 0;

    public MonthYearPickerSkin(ComboBoxBase<LocalDate> control) {
        super(control);

        control.getStyleClass().add("month-year-picker");
        control.setEditable(false);

        if (control.lookup(".arrow-button") instanceof StackPane iconPane) {
            iconPane.getChildren().clear();
            iconPane.getChildren().add(new FontIcon(ComboBox.DOWN_ARROW_ICON));
        }

        this.fakeEditor.setMaxWidth(Double.MAX_VALUE);

        control.setOnMouseClicked(this::openComboRequest);
        this.fakeEditor.setOnMouseClicked(this::openComboRequest);

        this.popupContent = new MonthYearPickerPopup();
        this.popupContent.setValue(this.getSkinnable().getValue());

        this.fakeEditor.textProperty().bind(this.popupContent.dateProperty.map(this.getConverter()::toString));
        this.getSkinnable().valueProperty().bind(this.popupContent.dateProperty);
        this.fakeEditor.editableProperty().bind(control.editableProperty());
        this.fakeEditor.textProperty().bind(control.valueProperty().map(this.getConverter()::toString));

        this.customPopup.getContent().setAll(this.popupContent);

        // CONFIGURACIÓN DEL AUTO-HIDE
        this.customPopup.setAutoHide(true);
        this.customPopup.setOnAutoHide(event -> {
            this.lastAutoHide = System.currentTimeMillis();
            if (control.isShowing()) {
                control.hide(); // Desencadena el cierre formal y limpia estilos
            }
        });
    }

    /*
     * =========================================================================
     * IMPLEMENTACIÓN OBLIGATORIA DE SHOW Y HIDE (Exigido por ComboBoxBaseSkin)
     * =========================================================================
     */
    @Override
    public void show() {
        if (!customPopup.isShowing()) {
            ComboBoxBase<LocalDate> control = getSkinnable();
            if (control == null || customPopup.isShowing())
                return;

            // 1. Añadimos tu clase de estilos visuales
            this.popupContent.getStyleClass().add("opened");

            // 2. Calculamos la posición física en pantalla (justo debajo del TextField)
            Point2D p = control.localToScreen(0, control.getHeight());
            if (p != null) {
                customPopup.show(control.getScene().getWindow(), p.getX() - 5, p.getY() - 5);
            }
        }
    }

    @Override
    public void hide() {
        if (customPopup.isShowing()) {
            this.popupContent.getStyleClass().remove("opened");
            customPopup.hide();
        }
    }

    @Override
    public Node getDisplayNode() {
        return this.fakeEditor;
    }

    /*
     * =========================================================================
     * MÉTODOS DE LAYOUT Y CONTRATO DE TAMAÑO PARA EL DIALOG
     * =========================================================================
     */

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset,
            double leftInset) {
        return leftInset + 80 + rightInset;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
            double leftInset) {
        return leftInset + 268 + rightInset;
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset,
            double leftInset) {
        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    private void openComboRequest(MouseEvent event) {
        if (System.currentTimeMillis() - this.lastAutoHide < 150)
            return;
        if (this.getSkinnable().isShowing()) {
            this.getSkinnable().hide();
        } else {
            this.getSkinnable().show();
        }
        event.consume();

    }

    protected StringConverter<LocalDate> getConverter() {
        return ((MonthYearPicker) this.getSkinnable()).getConverter();
    }
}
