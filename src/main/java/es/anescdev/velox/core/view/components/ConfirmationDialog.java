package es.anescdev.velox.core.view.components;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * @author AnesCDev
 */
public class ConfirmationDialog extends BaseDialog<ButtonType>{

    private final SimpleListProperty<ButtonType> buttonTypes = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final Label confirmationLabel = new Label();
    public ConfirmationDialog(String title, String confirmationText) {
        super(title);

        this.showExitbuttonProperty().set(false);
        this.confirmationLabel.textProperty().set(confirmationText);

        this.addButtonType(ButtonType.NO, ButtonType.YES);

        this.setContent(confirmationLabel);
    }    

    // buttonTypes
    public SimpleListProperty<ButtonType> buttonTypesProperty() {
        return buttonTypes;
    }

}
