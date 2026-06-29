package es.anescdev.sumatory.view.components;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.typicons.Typicons;

import es.anescdev.App;
import es.anescdev.sumatory.model.enums.SumatoryState;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SumaryStateWidget extends HBox {

    private static final String BASE_STRING_STATE_TRANSLATION = "sumatory.statewidget.state";
    public final SimpleObjectProperty<SumatoryState> sumatoryState;
    private final SimpleStringProperty sumatoryStateString;

    private final FontIcon icon;

    public SumaryStateWidget(SumatoryState initialState) {
        super();
        this.sumatoryState = new SimpleObjectProperty<>(initialState);
        this.sumatoryStateString = new SimpleStringProperty(this.getStateStringFromState(initialState));
        this.icon = new FontIcon(this.getIconFromStatus());
        Text stateStringText = new Text();
        stateStringText.textProperty().bind(sumatoryStateString);
        this.getChildren().addAll(
            new Text(App.getResourceBundle().getString("sumatory.statewidget.label" + ": ")),
            icon,
            stateStringText
        );
    }

    public void setSumatoryState(SumatoryState newState) {
        this.sumatoryState.set(newState);
    }
    private Ikon getIconFromStatus() {
        switch (sumatoryState.getValue()) {
            case DRAFT:
                return Typicons.WARNING;
            case SAVED:
                return Typicons.TICK;
            default:
                throw new IllegalStateException("Not handled status error");
        }
    }
    private String getStateStringFromState(SumatoryState state) {
        return BASE_STRING_STATE_TRANSLATION + this.sumatoryState.getValue().name();
    }
}
