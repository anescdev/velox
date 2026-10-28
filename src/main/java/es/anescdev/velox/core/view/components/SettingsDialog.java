package es.anescdev.velox.core.view.components;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.bus.ObservableBus;
import es.anescdev.velox.core.providers.SettingsProvider;
import es.anescdev.velox.core.settings.SettingsKey;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class SettingsDialog extends BaseDialog<ObservableBus> {

    private final ValidationSupport validationSupport;

    private final SettingsProvider settings;
    private final ObservableBus changes = new ObservableBus();

    private final SimpleStringProperty sumatoryFilePattern;
    private final SimpleBooleanProperty compactTabName;

    public SettingsDialog(SettingsProvider settingsConfigurationProvider) {
        super(App.instance().getMessage("settings.title"));
        this.validationSupport = new ValidationSupport();

        this.setWidth(650);
        this.settings = settingsConfigurationProvider;
        this.addButtonType(ButtonType.CLOSE, ButtonType.APPLY);
        this.showExitbuttonProperty().set(false);

        Button apply = ((Button) this.getDialogPane().lookupButton(ButtonType.APPLY));
        apply.setDefaultButton(true);
        apply.disableProperty()
                .bind(this.changes.sizeProperty().isEqualTo(0).or(this.validationSupport.invalidProperty()));

        // Textfield nombre sumatorio exportado
        this.sumatoryFilePattern = new SimpleStringProperty(this.settings.get(SettingsKey.SUMATORY_NAME_PATTERN));
        this.sumatoryFilePattern.addListener((ChangeListener<String>) (obs, oldValue, newValue) -> {
            if (!this.settings.get(SettingsKey.SUMATORY_NAME_PATTERN).equals(newValue)) {
                this.changes.setData(SettingsKey.SUMATORY_NAME_PATTERN, newValue);
            } else
                this.changes.removeFromBus(SettingsKey.SUMATORY_NAME_PATTERN);
        });

        // Checkbox Nombre de pestañas compactos
        this.compactTabName = new SimpleBooleanProperty(this.settings.get(SettingsKey.COMPACT_TAB_NAME).get());
        this.compactTabName.addListener((ChangeListener<Boolean>) (obs, oldValue, newValue) -> {
            if (!this.settings.get(SettingsKey.COMPACT_TAB_NAME).getValue().equals(newValue)) {
                this.changes.setData(SettingsKey.COMPACT_TAB_NAME, newValue);
            } else
                this.changes.removeFromBus(SettingsKey.COMPACT_TAB_NAME);
        });

        this.buildOptionPane();
        this.setResultConverter(
                button -> ButtonType.APPLY.equals(button)
                        && this.changes.sizeProperty().get() > 0
                                ? this.changes
                                : null);
    }

    private void buildOptionPane() {
        // ----------------------- UI OPTIONS
        GridPane uiOptionsWrapper = new GridPane();
        uiOptionsWrapper.setAlignment(Pos.CENTER_LEFT);

        CheckBox collapsedTabNameCheckBox = new CheckBox(
                App.instance().getMessage("settings.options.ui.compactTabName"));
        collapsedTabNameCheckBox.setSelected(this.compactTabName.get());
        this.compactTabName.bind(collapsedTabNameCheckBox.selectedProperty());
        uiOptionsWrapper.add(collapsedTabNameCheckBox, 0, 0);

        // ----------------------- SUMATORY OPTIONS
        GridPane sumatoryOptionsWrapper = new GridPane();

        TextField exportFilePatternTextField = new TextField(this.sumatoryFilePattern.get());
        validationSupport.registerValidator(
                exportFilePatternTextField,
                Validator.createRegexValidator(
                        App.instance().getMessage(""),
                        SettingsKey.SUMATORY_NAME_REGEXP,
                        Severity.ERROR));
        this.sumatoryFilePattern.bind(exportFilePatternTextField.textProperty());

        Label exportFilePatternLabel = new Label(
                App.instance().getMessage("settings.options.sumatory.exportFilePattern"));
        exportFilePatternLabel.setLabelFor(exportFilePatternTextField);

        sumatoryOptionsWrapper.add(this.createOptionBox(exportFilePatternLabel, exportFilePatternTextField), 0, 0);

        NavigationPane navigationPane = new NavigationPane();
        navigationPane.addPage(App.instance().getMessage(SettingsKey.UI_GROUP),
                uiOptionsWrapper);
        navigationPane.addPage(App.instance().getMessage(SettingsKey.SUMATORY_GROUP), sumatoryOptionsWrapper);

        this.setOnHidden(event -> navigationPane.dispose());

        this.setContent(navigationPane);
    }

    private HBox createOptionBox(Node... children) {
        HBox optionBox = new HBox(children);
        for (Node node : children) {
            node.getStyleClass().add("setting");
        }
        optionBox.setAlignment(Pos.CENTER);
        return optionBox;
    }

}