package es.anescdev.velox.core.view.components.statusbar;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsMonochrome;

import es.anescdev.velox.app.App;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Componente de interfaz reutilizable del dominio None (diálogo, celda, "skin" de
 * barra de estado, etc.).
 */
public abstract class StateStatusBarSkin extends BaseStatusBarSkin{

    private static final String BASE_STRING_STATE_TRANSLATION = "sumatory.statewidget.state.";

    private static final String DRAFT_COLOR_STRING_CLASS = "draft-state";
    private static final String SAVED_COLOR_STRING_CLASS = "saved-state";

    private ChangeListener<StateStatusBar> stateChangeListener;

    protected final SimpleObjectProperty<StateStatusBar> state = new SimpleObjectProperty<>(StateStatusBar.SAVED);

    protected StateStatusBarSkin(CustomStatusBar control) {
        super(control);
        this.addNode(this.getStateWidget());
        this.addVisualSeparator();
    }

    @Override
    public void dispose() {
        this.state.unbind();
        this.state.removeListener(this.stateChangeListener);
        super.dispose();
    }



    private String getStateStringFromState(StateStatusBar state) {
        return App.instance()
                .getMessage(BASE_STRING_STATE_TRANSLATION + this.state.getValue().name().toLowerCase());
    }

    private HBox getStateWidget() {
        if(stateChangeListener != null) throw new IllegalStateException("A state widget has been created previously");
        FontIcon stateIcon = new FontIcon();
        stateIcon.setIconCode(UniconsMonochrome.CIRCLE);
        stateIcon.getStyleClass().add(SAVED_COLOR_STRING_CLASS);

        Label stateStringLabel = new Label();
        stateStringLabel.textProperty().bind(state.map(state -> this.getStateStringFromState(state)));
        stateStringLabel.getStyleClass().addAll("state-label", SAVED_COLOR_STRING_CLASS);

        this.stateChangeListener = (obs, old, newState) -> {
            if (old == newState)
                return;
            switch (newState) {
                case StateStatusBar.SAVED:
                    stateIcon.getStyleClass().remove(DRAFT_COLOR_STRING_CLASS);
                    stateStringLabel.getStyleClass().remove(DRAFT_COLOR_STRING_CLASS);
                    stateIcon.getStyleClass().add(SAVED_COLOR_STRING_CLASS);
                    stateStringLabel.getStyleClass().add(SAVED_COLOR_STRING_CLASS);
                    break;
                case StateStatusBar.DRAFT:
                    stateIcon.getStyleClass().remove(SAVED_COLOR_STRING_CLASS);
                    stateStringLabel.getStyleClass().remove(SAVED_COLOR_STRING_CLASS);
                    stateIcon.getStyleClass().add(DRAFT_COLOR_STRING_CLASS);
                    stateStringLabel.getStyleClass().add(DRAFT_COLOR_STRING_CLASS);
                    break;
            }
        };
        this.state.addListener(this.stateChangeListener);

        HBox stateWidget = new HBox(stateIcon, stateStringLabel);
        stateWidget.setAlignment(Pos.CENTER_LEFT);
        stateWidget.setSpacing(10);

        return stateWidget;
    }
    
}
