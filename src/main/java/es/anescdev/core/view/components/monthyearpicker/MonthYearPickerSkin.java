package es.anescdev.core.view.components.monthyearpicker;

import java.time.LocalDate;

import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxPopupControl;
import javafx.util.StringConverter;

/**
 * @author AnesCDev
 */
public class MonthYearPickerSkin extends ComboBoxPopupControl<LocalDate> {
    private MonthYearPickerPopup popupContent;
    private final TextField fakeEditor = new TextField();//TODO: Falta que se pueda hacer editable desde la caja, por ahora no

    public MonthYearPickerSkin(ComboBoxBase<LocalDate> control) {
        super(control);
        this.fakeEditor.editableProperty().bind(control.editableProperty());
        control.setOnMouseClicked(ev -> {
            if (control.isShowing())
                control.hide();
            else
                control.show();
            ev.consume();
        });
        control.setEditable(false);
        this.fakeEditor.setText(this.getConverter().toString(control.getValue()));
    }

    @Override
    protected Node getPopupContent() {
        if (this.popupContent == null) {
            this.popupContent = new MonthYearPickerPopup();
            this.popupContent.setValue(this.getSkinnable().getValue());
            this.popupContent.dateProperty.addListener((o, oldDate, date) -> {
                this.fakeEditor.setText(this.getConverter().toString(date));
                this.getSkinnable().setValue(date);
            });
        }
        return this.popupContent;
    }

    @Override
    protected TextField getEditor() {
        return this.fakeEditor;
    }

    @Override
    protected StringConverter<LocalDate> getConverter() {
        return ((MonthYearPicker) this.getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {
        return this.fakeEditor;
    }

}
