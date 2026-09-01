package es.anescdev.velox.core.view.components;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;
import javafx.scene.layout.StackPane;

/**
 * Componente de interfaz reutilizable del dominio None (diálogo, celda, "skin" de
 * barra de estado, etc.).
 */
public class ComboBox<T> extends javafx.scene.control.ComboBox<T> {
    public static final Ikon DOWN_ARROW_ICON = UniconsLine.ANGLE_DOWN;

    /**
     * @param items
     */
    public ComboBox(ObservableList<T> items) {
        super(items);
        this.changeIcon();
    }

    /**
     * 
     */
    public ComboBox() {
        super();
        this.changeIcon();
    }

    private void changeIcon() {
        var combo = this;
        this.skinProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                if(newValue == null) return;
                if (combo.lookup(".arrow-button") instanceof StackPane iconPane) {
                    iconPane.getChildren().clear();
                    iconPane.getChildren().add(new FontIcon(DOWN_ARROW_ICON));
                    observable.removeListener(this);
                }
            }

        });
    }

}
