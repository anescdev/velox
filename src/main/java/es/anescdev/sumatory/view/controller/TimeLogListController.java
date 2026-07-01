package es.anescdev.sumatory.view.controller;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.ResourceBundle;

import es.anescdev.core.view.TabManager;
import es.anescdev.core.view.controller.BaseController;
import es.anescdev.sumatory.model.entities.Sumatory;
import es.anescdev.sumatory.model.enums.SumatoryState;
import es.anescdev.sumatory.view.components.SumatoryStateWidget;
import es.anescdev.sumatory.view.utils.SumatoryUtils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * @author AnesCDev
 */
public class TimeLogListController extends BaseController {

    @FXML
    private VBox listRootNode;

    @FXML
    private Text employeStatusLabel;

    @FXML
    private Text dateStatusLabel;

    @FXML
    private Text totalStatusLabel;

    @FXML
    private SumatoryStateWidget sumatoryStateWidget;

    private Sumatory sumatory;

    private final SimpleObjectProperty<Duration> duration = new SimpleObjectProperty<>(Duration.ZERO);

    private final SimpleObjectProperty<SumatoryState> state = new SimpleObjectProperty<>(SumatoryState.SAVED);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.duration.addListener(
                (o, oldTotal, newTotal) -> totalStatusLabel.setText(SumatoryUtils.parseDuration(newTotal)));
        this.sumatoryStateWidget.sumatoryState.bind(state);
    }

    @Override
    public void initData(Map<String, Object> data) {
        this.sumatory = (Sumatory) data.get(TabManager.USER_DATA_KEY);
        this.employeStatusLabel.setText(this.sumatory.getEmployee());
        this.dateStatusLabel.setText(this.sumatory.getMonth() + "/" + this.sumatory.getYear());
        this.totalStatusLabel.setText(SumatoryUtils.parseDuration(this.sumatory.getTotal()));
    }
    

}
