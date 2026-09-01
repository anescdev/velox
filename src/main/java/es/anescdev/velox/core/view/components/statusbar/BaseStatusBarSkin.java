package es.anescdev.velox.core.view.components.statusbar;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * @author AnesCDev
 * 
 */
public abstract class BaseStatusBarSkin extends SkinBase<CustomStatusBar>{
    private final HBox statusBarContent;

    protected BaseStatusBarSkin(CustomStatusBar control) {
        super(control);

        this.statusBarContent = new HBox();
        this.statusBarContent.setSpacing(18);
        this.statusBarContent.getStyleClass().add("custom-status-bar");

        this.getChildren().add(this.statusBarContent);
    }

    protected void addVisualSeparator() {
        this.statusBarContent.getChildren().add(new Separator(Orientation.VERTICAL));
    }

    protected void addInvisibleSeparator() {
        Region separator = new Region();
        HBox.setHgrow(separator, Priority.ALWAYS);
        this.statusBarContent.getChildren().add(separator);
    }

    protected void addNode(Node node) {
        this.statusBarContent.getChildren().add(node);
    }

    protected void addAll(Node ...nodes) {
        this.statusBarContent.getChildren().addAll(nodes);
    }
    protected abstract void updateSkin();
    
}
