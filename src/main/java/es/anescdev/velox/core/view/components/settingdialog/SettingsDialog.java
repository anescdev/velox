package es.anescdev.velox.core.view.components.settingdialog;

import java.util.List;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.bus.ObservableBus;
import es.anescdev.velox.core.providers.SettingsProvider;
import es.anescdev.velox.core.settings.SettingsKey;
import es.anescdev.velox.core.view.components.BaseDialog;
import es.anescdev.velox.core.view.components.NavigationPane;

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
import javafx.scene.layout.VBox;

public class SettingsDialog extends BaseDialog<ObservableBus> {

    private final ValidationSupport validationSupport;

    private final SettingsProvider settings;
    private final ObservableBus changes = new ObservableBus();

    private final List<String> suggests;

    public SettingsDialog(SettingsProvider settingsConfigurationProvider) {
        super(App.instance().getMessage("settings.title"));
        this.validationSupport = new ValidationSupport();

        this.setWidth(650);
        this.setHeight(280);
        this.settings = settingsConfigurationProvider;
        this.addButtonType(ButtonType.CLOSE, ButtonType.APPLY);
        this.showExitbuttonProperty().set(false);

        Button apply = ((Button) this.getDialogPane().lookupButton(ButtonType.APPLY));
        apply.setDefaultButton(true);
        apply.disableProperty()
                .bind(this.changes.sizeProperty().isEqualTo(0).or(this.validationSupport.invalidProperty()));
        this.suggests = List.of("%d", "%n")
                .stream()
                .map(var -> var + " - "
                        + App.instance().getMessage("settings.options.sumatory.exportFilePattern.suggest." + var))
                .toList();

        this.buildOptionPane();
        this.setResultConverter(
                button -> ButtonType.APPLY.equals(button)
                        && this.changes.sizeProperty().get() > 0
                                ? this.changes
                                : null);
    }

    private void buildOptionPane() {
        // ----------------------- UI OPTIONS
        CheckBox collapsedTabNameCheckBox = new CheckBox(
                App.instance().getMessage("settings.options.ui.compactTabName"));
        collapsedTabNameCheckBox.setSelected(this.settings.get(SettingsKey.COMPACT_TAB_NAME).get());
        collapsedTabNameCheckBox.selectedProperty().addListener((ChangeListener<Boolean>) (obs, oldValue, newValue) -> {
            if (!this.settings.get(SettingsKey.COMPACT_TAB_NAME).getValue().equals(newValue)) {
                this.changes.setData(SettingsKey.COMPACT_TAB_NAME, newValue);
            } else
                this.changes.removeFromBus(SettingsKey.COMPACT_TAB_NAME);
        });

        GridPane uiOptionsWrapper = this.createOptionWrapper();
        uiOptionsWrapper.addRow(0, this.createOptionBox(collapsedTabNameCheckBox));
        // ----------------------- SUMATORY OPTIONS

        TextField exportFilePatternTextField = new TextField(this.settings.get(SettingsKey.SUMATORY_NAME_PATTERN));
        exportFilePatternTextField.textProperty().addListener((ChangeListener<String>) (obs, oldValue, newValue) -> {
            if (!this.settings.get(SettingsKey.SUMATORY_NAME_PATTERN).equals(newValue)) {
                this.changes.setData(SettingsKey.SUMATORY_NAME_PATTERN, newValue);
            } else
                this.changes.removeFromBus(SettingsKey.SUMATORY_NAME_PATTERN);
        });

        validationSupport.registerValidator(
                exportFilePatternTextField,
                Validator.createRegexValidator(
                        App.instance().getMessage(""),
                        SettingsKey.SUMATORY_NAME_REGEXP,
                        Severity.ERROR));

        var exportFileAutocompletion = new AppendedAutoCompletionTextFieldBinding<>(
                exportFilePatternTextField, autoCompletionRequest -> {
                    var lastInput = autoCompletionRequest.getUserText()
                            .substring(autoCompletionRequest.getUserText().length() - 2);
                    if (autoCompletionRequest.isCancelled() || lastInput.equals("%%") || !lastInput.endsWith("%"))
                        return List.of();
                    return this.suggests;
                },
                new FileExportPatternStringConverter());

        Label exportFilePatternLabel = new Label(
                App.instance().getMessage("settings.options.sumatory.exportFilePattern"));
        exportFilePatternLabel.setLabelFor(exportFilePatternTextField);

        GridPane sumatoryOptionPane = this.createOptionWrapper();
        sumatoryOptionPane.addRow(0, this.createOptionBox(
                App.instance().getMessage("settings.options.sumatory.exportFilePattern.help"),
                exportFilePatternLabel, exportFilePatternTextField));

        NavigationPane navigationPane = new NavigationPane();
        navigationPane.setPrefHeight(Double.MAX_VALUE);
        navigationPane.addPage(App.instance().getMessage(SettingsKey.UI_GROUP),
                uiOptionsWrapper);
        navigationPane.addPage(
                App.instance().getMessage(SettingsKey.SUMATORY_GROUP),
                sumatoryOptionPane);

        this.setOnHidden(event -> {
            navigationPane.dispose();
            exportFileAutocompletion.dispose();
        });

        this.setContent(navigationPane);
    }

    private VBox createOptionBox(String tooltip, Node... children) {
        HBox optionBox = new HBox(children);
        for (Node node : children) {
            node.getStyleClass().add("setting");
        }
        optionBox.setAlignment(Pos.CENTER_LEFT);
        VBox group = new VBox(optionBox);
        group.setAlignment(Pos.CENTER_LEFT);
        if (tooltip != null && !tooltip.isBlank()) {
            Label helpLabel = new Label(tooltip);
            helpLabel.getStyleClass().addAll("setting", "help");
            group.getChildren().add(helpLabel);
        }
        return group;
    }

    private VBox createOptionBox(Node... children) {
        return this.createOptionBox(null, children);
    }

    private GridPane createOptionWrapper() {
        GridPane optionWrapper = new GridPane();
        optionWrapper.setAlignment(Pos.CENTER_LEFT);
        return optionWrapper;
    }

}