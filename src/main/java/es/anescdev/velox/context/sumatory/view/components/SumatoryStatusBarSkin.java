package es.anescdev.velox.context.sumatory.view.components;

import java.time.Duration;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.sumatory.view.utils.SumatoryStatusBarData;
import es.anescdev.velox.core.view.components.statusbar.CustomStatusBar;
import es.anescdev.velox.core.view.components.statusbar.StateStatusBarSkin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

/**
 * Componente de interfaz reutilizable del dominio sumatory (diálogo, celda, "skin" de
 * barra de estado, etc.).
 */
public class SumatoryStatusBarSkin extends StateStatusBarSkin {
    private final SimpleStringProperty titleProperty = new SimpleStringProperty();
    private final SimpleObjectProperty<Duration> sumatoryTotalProperty = new SimpleObjectProperty<Duration>(
            Duration.ZERO);
    private final SimpleObjectProperty<StringConverter<Duration>> converterProperty = new SimpleObjectProperty<StringConverter<Duration>>();
    private final SimpleStringProperty durationStringProperty = new SimpleStringProperty();

    private ChangeListener<StringConverter<Duration>> changedConverterListener;
    private ChangeListener<Duration> changedSumatoryTotalListener;

    public SumatoryStatusBarSkin(CustomStatusBar control) {
        super(control);

        this.changedConverterListener = (obs, old, newConverter) -> {
            if (newConverter == null)
                return;
            this.durationStringProperty.set(newConverter.toString(this.sumatoryTotalProperty.get()));
        };
        this.converterProperty.addListener(changedConverterListener);

        this.changedSumatoryTotalListener = (obs, old, newTotal) -> {
            if (newTotal == null)
                return;
            this.durationStringProperty.set(this.converterProperty.get().toString(newTotal));
        };
        this.sumatoryTotalProperty.addListener(this.changedSumatoryTotalListener);

        this.updateSkin();

        Label sumatoryTitleLabel = new Label();
        sumatoryTitleLabel.textProperty().bind(this.titleProperty);

        this.addNode(sumatoryTitleLabel);
        this.addInvisibleSeparator();
        this.addNode(this.getTotalWidget());
    }

    @Override
    public void dispose() {
        this.titleProperty.unbind();
        this.sumatoryTotalProperty.unbind();
        this.converterProperty.unbind();
        this.durationStringProperty.unbind();

        this.converterProperty.removeListener(this.changedConverterListener);
        this.sumatoryTotalProperty.removeListener(this.changedSumatoryTotalListener);

        super.dispose();
    }

    private HBox getTotalWidget() {
        Label durationLabel = new Label();
        durationLabel.textProperty().bind(this.durationStringProperty);

        return new HBox(durationLabel, new Label(App.instance().getMessage("sumatory.statewidget.label.total")));
    }

    @Override
    protected void updateSkin() {
        if (this.getSkinnable().getUserData() instanceof SumatoryStatusBarData data) {
            this.state.unbind();
            this.sumatoryTotalProperty.unbind();

            this.converterProperty.set(data.totalWorkDurationConverter());
            this.titleProperty.set(data.title().getValue());
            this.state.set(data.sumatoryState().getValue());
            this.sumatoryTotalProperty.set(data.sumatoryTotalProperty().getValue());
            this.durationStringProperty
                    .set(data.totalWorkDurationConverter().toString(data.sumatoryTotalProperty().getValue()));

            this.state.bind(data.sumatoryState());
            this.sumatoryTotalProperty.bind(data.sumatoryTotalProperty());
        }
    }
}
