package es.anescdev.velox.context.customer.view.components;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.customer.model.dto.CreateCustomer;
import es.anescdev.velox.core.view.components.BaseDialog;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * @author AnesCDev
 */
public class CreateCustomerDialog extends BaseDialog<CreateCustomer> {

    private final TextField nameTextField;
    private final TextField abreviationTextField;
    private final Button okButton;

    public CreateCustomerDialog() {
        super(App.instance().getMessage("customer.title.add"));
        this.nameTextField = new TextField();
        this.abreviationTextField = new TextField();

        this.nameTextField.setPrefWidth(300);
        this.abreviationTextField.setPrefWidth(300);

        this.getDialogPane().setPrefHeight(100);
        this.showExitbuttonProperty().set(false);
        this.createContent();
        this.createButtons();
        this.okButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        this.nameTextField.textProperty().addListener(this.validator(abreviationTextField));
        this.abreviationTextField.textProperty().addListener(this.validator(nameTextField));
    }

    private void createContent() {
        Label nameLabel = new Label(App.instance().getMessage("customer.dialog.add.name")),
        abreviationLabel = new Label(App.instance().getMessage("customer.dialog.add.abreviation"));

        nameLabel.getStyleClass().add("input-label");
        abreviationLabel.getStyleClass().add("input-label");

        VBox.setMargin(abreviationLabel, new Insets(14, 0, 0, 0));

        VBox form = new VBox(
                nameLabel,
                this.nameTextField,
                abreviationLabel,
                this.abreviationTextField);
        this.setContent(form);
    }

    private void createButtons() {
        this.addButtonType(ButtonType.CANCEL, ButtonType.OK);
        Button okButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        okButton.setOnAction(ev -> {
            if (okButton.isDisable())
                ev.consume();
        });
        this.setResultConverter(buttonType -> {
            if (okButton.isDisable())
                return null;
            return new CreateCustomer(
                    this.nameTextField.getText(),
                    this.abreviationTextField.getText());
        });
    }

    private ChangeListener<String> validator(TextField other) {
        return (o, old, newCustomer) -> {
            if (newCustomer == "" || other.getText() == "")
                this.okButton.setDisable(true);
            else
                this.okButton.setDisable(false);

        };
    }
}
