package es.anescdev.velox.app.loader.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.app.loader.LoadException;
import es.anescdev.velox.app.loader.Loader;
import es.anescdev.velox.app.loader.events.LoadEvent;
import es.anescdev.velox.app.loader.loaders.DataBaseLoader;
import es.anescdev.velox.app.loader.loaders.PdfGeneratorLoader;
import es.anescdev.velox.app.logging.LoggingLoader;

import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Window;

public class LoadController implements Initializable {
    @FXML
    private Label loadingMessageText;

    @FXML
    private Label loadingCountText;

    @FXML
    private ProgressBar loadingProgress;

    private final SimpleIntegerProperty loadCount = new SimpleIntegerProperty(0);

    private final Loader[] loaders;

    private final SimpleObjectProperty<Window> loadStage = new SimpleObjectProperty<>();

    @Inject
    public LoadController(DataBaseLoader dataBaseLoader, LoggingLoader loggingLoader,
            PdfGeneratorLoader pdfGeneratorLoader) {
        this.loaders = new Loader[] { loggingLoader, dataBaseLoader, pdfGeneratorLoader };
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadingCountText.textProperty()
                .bind(new When(loadCount.greaterThanOrEqualTo(this.loaders.length))
                        .then(App.instance().getMessage("load.complete"))
                        .otherwise("(" + loadCount.get() + "/" + this.loaders.length + ")"));
        loadingProgress.progressProperty()
                .bind(loadCount.map(loadCount -> loadCount.doubleValue() / this.loaders.length));
        this.loadStage.addListener(this::startLoaders);
    }

    public void setLoadStage(Window primaryStage) {
        this.loadStage.set(primaryStage);
    }

    private void startLoaders(ObservableValue<? extends Window> observable, Window oldValue, Window newValue) {
        if (!(oldValue == null && newValue != null))
            return;
        Thread.startVirtualThread(() -> {
            boolean noError = true;
            for (Loader loader : this.loaders) {
                Platform.runLater(() -> {
                    loadingMessageText.setText(App.instance().getMessage(loader.getLoadingMessage()));
                });
                try {
                    loader.load();
                } catch (LoadException e) {
                    this.handleException(e);
                    noError = false;
                    break;
                }
                Platform.runLater(() -> {
                    loadCount.set(loadCount.get() + 1);
                });
                LoadEvent.fireEvent(this.loadStage.getValue(),
                        new LoadEvent(loader.eventType()));
            }
            if (noError)
                LoadEvent.fireEvent(this.loadStage.getValue(), new LoadEvent(LoadEvent.COMPLETED_EVENT));
            this.loadStage.removeListener(this::startLoaders);
        });
    }

    private void handleException(LoadException exception) {
        App.instance().getLogger().severe(exception.getMessage());
        Platform.runLater(() -> {
            new Alert(AlertType.ERROR, exception.getErrorKey(), ButtonType.CLOSE).showAndWait();
            Platform.exit();
        });
    }
}