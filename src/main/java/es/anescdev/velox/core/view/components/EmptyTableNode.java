package es.anescdev.velox.core.view.components;

import es.anescdev.velox.app.App;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * @author AnesCDev
 */
public class EmptyTableNode extends VBox {
    
    public EmptyTableNode(String message) {
        this.getStyleClass().add("empty-table-node");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(8);

        Label iconLabel = new Label("▹ ▹ ▹");
        iconLabel.getStyleClass().addAll("no-lolo", "symbol");
        Label titleLabel = new Label(App.instance().getMessage("emptytablenode.title"));
        titleLabel.getStyleClass().add("empty-table-node-title");

        this.getChildren().addAll(iconLabel, titleLabel);

        if(!message.isBlank()) {
            Label messageLabel = new Label(message);
            messageLabel.getStyleClass().add("no-lolo");
            this.getChildren().add(messageLabel);
        }
    }
    public EmptyTableNode() {
        this("");
    }
    
}
