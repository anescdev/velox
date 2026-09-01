package es.anescdev.velox.core.view.components;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsSolid;

import es.anescdev.velox.app.App;
import es.anescdev.velox.core.view.utils.DialogUtilities;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BaseDialog<R> extends Dialog<R> {

    private static final ButtonType DEFAULT_NO_BUTTON_TYPE = ButtonType.CLOSE;
    private static final String BUTTON_BAR_STYLE_CLASS = "dialog-custom-button-bar";

    private final HBox header = new HBox();
    private final Label titleLabel = new Label();
    private final Region separator = new Region();
    private final Button exitButton = new Button();
    private final StackPane content = new StackPane();

    private final SimpleBooleanProperty showExitbuttonProperty = new SimpleBooleanProperty(true);

    private double xOffset = 0;
    private double yOffset = 0;
    private final SimpleBooleanProperty noButtonType = new SimpleBooleanProperty(false);

    public BaseDialog(String title) {
        super();
        this.initOwner(App.instance().getMainStage());
        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.TRANSPARENT);
        this.setTitle(title);
        this.getDialogPane().getStyleClass().add("dialog-custom");

        this.header.getStyleClass().add("dialog-header");
        this.header.setAlignment(Pos.CENTER);
        this.header.setOnMousePressed(event -> {
            // Guarda las coordenadas relativas al interior de la ventana cuando haces clic
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        this.header.setOnMouseDragged(event -> {
            // Obtiene de forma segura el Stage que encapsula al Dialog de JavaFX
            Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
            if (stage != null) {
                // Mueve la ventana basándose en la posición del ratón en la pantalla menos el
                // offset inicial
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        this.header.setAlignment(Pos.CENTER);
        this.content.getStyleClass().add("dialog-content");
        this.content.setAlignment(Pos.CENTER);

        this.titleLabel.textProperty().set(title);
        this.titleLabel.getStyleClass().add("title");

        HBox.setHgrow(this.separator, Priority.ALWAYS);

        var exitButtonIcon = new FontIcon(UniconsSolid.MULTIPLY);
        exitButtonIcon.setIconSize(18);
        this.exitButton.setGraphic(exitButtonIcon);
        this.exitButton.setOnAction(event -> this.close());
        this.exitButton.visibleProperty().bind(showExitbuttonProperty);
        this.exitButton.managedProperty().bind(showExitbuttonProperty);

        var lineSeparator = new Separator();

        VBox.setVgrow(lineSeparator, Priority.NEVER);

        this.header.getChildren().addAll(this.titleLabel, this.separator, this.exitButton);
        this.getDialogPane().setContent(new VBox(this.header, lineSeparator, this.content));

        this.getDialogPane().getButtonTypes().add(DEFAULT_NO_BUTTON_TYPE);
        Node defaultButtonContainer = this.getDialogPane().lookup(".button-bar");
        if (defaultButtonContainer instanceof ButtonBar buttonBar) {
            this.noButtonType.addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    buttonBar.setVisible(false);
                    buttonBar.setManaged(false);
                    buttonBar.setMinHeight(0);
                    buttonBar.setPrefHeight(0);
                    buttonBar.setMaxHeight(0);
                    buttonBar.getStyleClass().remove(BUTTON_BAR_STYLE_CLASS); 
                } else {
                    buttonBar.setVisible(true);
                    buttonBar.setManaged(true);
                    buttonBar.setMinHeight(Region.USE_COMPUTED_SIZE);
                    buttonBar.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    buttonBar.setMaxHeight(Region.USE_COMPUTED_SIZE);
                    buttonBar.getStyleClass().add(BUTTON_BAR_STYLE_CLASS);
                }
            });
        }
        this.noButtonType.set(true);
        Node grid = this.getDialogPane().lookup(".grid");
        if (grid instanceof Region) {
            ((Region) grid).setStyle("-fx-background-color: transparent;");
        }

        DialogUtilities.centerDialog(this);
    }

    protected void setContent(Node node) {
        if (this.content.getChildren().size() == 0)
            this.content.getChildren().add(node);
        else
            this.content.getChildren().set(0, node);
    }

    // hideExitButton
    public SimpleBooleanProperty showExitbuttonProperty() {
        return this.showExitbuttonProperty;
    }

    public void addButtonType(ButtonType... buttonTypes) {
        if (this.noButtonType.get()) {
            this.getDialogPane().getButtonTypes().remove(0);
            this.noButtonType.set(false);
        }
        this.getDialogPane().getButtonTypes().addAll(buttonTypes);
    }

    
}
