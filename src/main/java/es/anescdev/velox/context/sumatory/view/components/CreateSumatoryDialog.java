package es.anescdev.velox.context.sumatory.view.components;

import java.time.LocalDate;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.sumatory.model.dto.CreateSumatory;
import es.anescdev.velox.core.view.components.BaseDialog;
import es.anescdev.velox.core.view.components.monthyearpicker.MonthYearPicker;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Componente de interfaz reutilizable del dominio sumatory (diálogo, celda, "skin" de
 * barra de estado, etc.).
 */
public class CreateSumatoryDialog extends BaseDialog<CreateSumatory> {

    private final MonthYearPicker monthYearPicker;

    public CreateSumatoryDialog() {
        super(App.instance().getMessage("sumatory.title.add"));
        this.monthYearPicker = new MonthYearPicker();
        this.showExitbuttonProperty().set(false);
        this.createButtons();
        this.createContent();
    }

    private void createContent() {
        Label pickerLabel = new Label(App.instance().getMessage("sumatory.dialog.add.date"));
        pickerLabel.getStyleClass().add("input-label");
        VBox form = new VBox(
                pickerLabel,
                this.monthYearPicker);
        this.setContent(form);
    }

    private void createButtons() {
        this.addButtonType(ButtonType.CANCEL, ButtonType.OK);
        Button okButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(ev -> {
            if (okButton.isDisable())
                ev.consume();
        });
        this.setResultConverter(buttonType -> {
            if (okButton.isDisable())
                return null;
            if(buttonType.equals(ButtonType.CANCEL)) return null;
            LocalDate selectedDate = this.monthYearPicker.getValue();
            return new CreateSumatory(
                    (byte) selectedDate.getMonthValue(),
                    (short) selectedDate.getYear(),
                    null);
        });
    }
}
