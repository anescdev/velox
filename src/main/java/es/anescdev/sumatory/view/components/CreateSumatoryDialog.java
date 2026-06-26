package es.anescdev.sumatory.view.components;

import java.time.LocalDate;

import es.anescdev.App;
import es.anescdev.core.view.components.BaseDialog;
import es.anescdev.core.view.components.monthyearpicker.MonthYearPicker;
import es.anescdev.sumatory.dto.CreateSumatory;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * @author AnesCDev
 */
public class CreateSumatoryDialog extends BaseDialog<CreateSumatory> {

    private final MonthYearPicker monthYearPicker;
    private final TextField employeeTextField;

    public CreateSumatoryDialog() {
        super(App.getResourceBundle().getString("sumatory.title.add"));
        this.monthYearPicker = new MonthYearPicker();
        this.employeeTextField = new TextField();
        this.getDialogPane().setPrefHeight(100);
        this.getDialogPane().setMaxWidth(Region.USE_PREF_SIZE);
        this.createContent();
        this.createButtons();
        this.employeeTextField.textProperty().addListener((o, old, newEmployee) -> {
            Button okButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
            if (newEmployee == "")
                okButton.setDisable(true);
            else
                okButton.setDisable(false);

        });
    }

    private void createContent() {
        this.monthYearPicker.setMaxWidth(Double.MAX_VALUE);
        this.employeeTextField.setMaxWidth(Double.MAX_VALUE);
        VBox form = new VBox(
                new Label(App.getResourceBundle().getString("sumatory.dialog.add.date")),
                this.monthYearPicker,
                new Label(App.getResourceBundle().getString("sumatory.dialog.add.employee")),
                this.employeeTextField);
        form.setFillWidth(true);
        this.getDialogPane().setContent(form);
    }

    private void createButtons() {
        this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        Button okButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        okButton.setOnAction(ev -> {
            if (okButton.isDisable()) ev.consume();
        });
        this.setResultConverter(buttonType -> {
            if(okButton.isDisable()) return null;
            LocalDate selectedDate = this.monthYearPicker.getValue();
            return new CreateSumatory(
                (byte)selectedDate.getMonthValue(), 
                (short)selectedDate.getYear(),
                this.employeeTextField.getText()
            );
        });
    }
}
