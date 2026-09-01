package es.anescdev.velox.core.view.components;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CustomTextInputDialog extends BaseDialog<String> {

    private final TextField textField = new TextField();
    private final Label messageLabel = new Label();

    public CustomTextInputDialog(String title, String label, String defaultValue) {
        super(title);
        this.createContent(label, defaultValue);
        this.createButtons();
    }

    private void createContent(String label, String defaultValue) {
        // 1. Configuración de estilos para el CSS
        VBox layoutContainer = new VBox();

        this.messageLabel.setText(label);
        this.messageLabel.getStyleClass().add("input-label");

        this.textField.setText(defaultValue);
        this.textField.setPrefWidth(300);
        this.textField.requestFocus();

        layoutContainer.getChildren().addAll(this.messageLabel, this.textField);

        this.setContent(layoutContainer);
    }

    private void createButtons() {
        this.addButtonType(ButtonType.CANCEL, ButtonType.OK);
        this.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(this.textField.textProperty().isEmpty());
        this.setResultConverter(dialogButton -> {
            if(dialogButton.equals(ButtonType.OK)) return this.textField.getText();
            return null;
        });
    }
}
