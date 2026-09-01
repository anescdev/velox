package es.anescdev.velox.core.view.components;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.When;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * @author AnesCDev
 */
public class TitledPane extends javafx.scene.control.TitledPane {

    private final ObjectBinding<? extends Ikon> iconProperty;

    public TitledPane(String title, Node content) {
        super(title, content);
        this.iconProperty = new When(this.expandedProperty())
                .then(UniconsLine.ANGLE_DOUBLE_DOWN)
                .otherwise(UniconsLine.ANGLE_DOUBLE_RIGHT);
        this.skinProperty().addListener((obs, old, newSkin) -> {
            if( newSkin == null) return;
            this.setArrowIcon();
        });
    }

    public TitledPane() {
        super();
        this.iconProperty = new When(this.expandedProperty())
                .then(UniconsLine.ANGLE_DOUBLE_DOWN)
                .otherwise(UniconsLine.ANGLE_DOUBLE_RIGHT);
        this.skinProperty().addListener((obs, old, newSkin) -> {
            if( newSkin == null) return;
            this.setArrowIcon();
        });
    }

    private void setArrowIcon() {
        FontIcon icon = new FontIcon();
        icon.setIconSize(24);
        icon.iconCodeProperty().bind(this.iconProperty);
        if (this.lookup(".arrow-button") instanceof StackPane arrowButtonContainer)  {
            arrowButtonContainer.getChildren().clear();
            arrowButtonContainer.getChildren().add(icon);
        }
    }
}
