package es.anescdev.velox.core.view.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * @author AnesCDev
 */
public record TextFieldAction(Node buttonContent, boolean isGraphic, EventHandler<ActionEvent> clickEvent) {
        public static TextFieldAction onlyIcon(Node buttonContent, EventHandler<ActionEvent> clickEvent) {
            return new TextFieldAction(buttonContent, true, clickEvent);
        }

        public static TextFieldAction onlyLabel(Label buttonContent, EventHandler<ActionEvent> clickEvent) {
            return new TextFieldAction(buttonContent, false, clickEvent);
        }

        public Button toButton() {
            Button button = new Button();
            if (!this.isGraphic() && this.buttonContent() instanceof Label text) {
                button.setText(text.getText());
            } else {
                button.setGraphic(this.buttonContent());
            }
            button.setOnAction(this.clickEvent());
            return button;
        }
    }
