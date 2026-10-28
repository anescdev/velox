package es.anescdev.velox.core.view.components;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class NavigationPane extends BorderPane {

    public static final String STYLE_CLASS = "navigation-pane";
    public static final String MENU_STYLE_CLASS = "navigation-menu";
    public static final String CONTENT_STYLE_CLASS = "navigation-content";
    public static final String BUTTON_STYLE_CLASS = "navigation-button";
    public static final String SELECTED_STYLE_CLASS = "selected";

    private final VBox menu = new VBox();
    private final StackPane content = new StackPane();
    private final ToggleGroup group = new ToggleGroup();
    private final List<Page> pages = new ArrayList<>();

    private boolean disposed = false;

    public NavigationPane() {
        getStyleClass().add(STYLE_CLASS);
        menu.getStyleClass().add(MENU_STYLE_CLASS);
        content.getStyleClass().add(CONTENT_STYLE_CLASS);

        BorderPane.setMargin(content, new Insets(0, 0, 0, 14));

        setLeft(menu);
        setCenter(content);
    }

    /** Añade una página. Si es la primera, se selecciona automáticamente. */
    public void addPage(String title, Node node) {
        if (disposed) {
            throw new IllegalStateException("NavigationPane ya fue liberado (dispose)");
        }

        Page page = new Page(title, node);
        pages.add(page);
        menu.getChildren().add(page.button);

        if (pages.size() == 1) {
            page.button.setSelected(true);
            show(page);
        }
    }

    /** Elimina la página en el índice dado, liberando sus listeners. */
    public void removePage(int index) {
        if (index < 0 || index >= pages.size()) {
            return;
        }

        Page page = pages.remove(index);
        boolean wasSelected = page.button.isSelected();

        menu.getChildren().remove(page.button);
        page.dispose();

        if (wasSelected) {
            content.getChildren().clear();
            if (!pages.isEmpty()) {
                select(Math.min(index, pages.size() - 1));
            }
        }
    }

    /** Selecciona programáticamente una página por índice. */
    public void select(int index) {
        if (index < 0 || index >= pages.size()) {
            return;
        }
        Page page = pages.get(index);
        page.button.setSelected(true);
        show(page);
    }

    /**
     * Libera todos los listeners, handlers y referencias.
     * Llamar cuando el componente ya no se vaya a usar.
     */
    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;

        for (Page page : pages) {
            page.dispose();
        }
        pages.clear();

        menu.getChildren().clear();
        content.getChildren().clear();
        group.getToggles().clear();
    }

    private void show(Page page) {
        content.getChildren().setAll(page.node);
    }

    /** Agrupa un botón, su nodo y los listeners asociados para poder quitarlos. */
    private final class Page {
        private final ToggleButton button;
        private final Node node;

        private final ChangeListener<Boolean> selectedListener;
        private final EventHandler<MouseEvent> pressedFilter;
        private final EventHandler<ActionEvent> actionHandler;

        private Page(String title, Node node) {
            this.node = node;
            this.button = new ToggleButton(title);

            button.getStyleClass().addAll("button", BUTTON_STYLE_CLASS);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setToggleGroup(group);

            selectedListener = (obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    if (!button.getStyleClass().contains(SELECTED_STYLE_CLASS)) {
                        button.getStyleClass().add(SELECTED_STYLE_CLASS);
                    }
                } else {
                    button.getStyleClass().remove(SELECTED_STYLE_CLASS);
                }
            };

            // Evita deseleccionar el botón activo al volver a pulsarlo
            pressedFilter = e -> {
                if (button.isSelected()) {
                    e.consume();
                }
            };

            actionHandler = e -> show(this);

            button.selectedProperty().addListener(selectedListener);
            button.addEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilter);
            button.setOnAction(actionHandler);
        }

        private void dispose() {
            button.selectedProperty().removeListener(selectedListener);
            button.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilter);
            button.setOnAction(null);

            button.getStyleClass().remove(SELECTED_STYLE_CLASS);
            button.setSelected(false);
            button.setToggleGroup(null);
        }
    }
}