package es.anescdev.velox.context.sumatory.view.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.inject.Inject;

import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.context.sumatory.view.commands.CreateSumatoryCommand;
import es.anescdev.velox.context.sumatory.view.commands.DeleteSumatoryCommand;
import es.anescdev.velox.context.sumatory.view.utils.SumatoryUtils;
import es.anescdev.velox.context.sumatory.view.viewmodel.SumatoryListViewModel;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.command.CommandInvoker;
import es.anescdev.velox.core.view.TabManager;
import es.anescdev.velox.core.view.components.EmptyTableNode;
import es.anescdev.velox.core.view.components.StyleableTableCell;
import es.anescdev.velox.core.view.controller.BaseViewTabController;
import es.anescdev.velox.core.view.controller.WorkspaceController;
import es.anescdev.velox.core.view.utils.DurationValueFactory;
import es.anescdev.velox.core.view.utils.StringUtilities;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.converter.ShortStringConverter;

public class SumatoryListController extends BaseViewTabController {

    private final SumatoryListViewModel viewModel;

    @FXML
    private TableView<Sumatory> sumatoryTable;

    @FXML
    private TableColumn<Sumatory, String> monthColumn;

    @FXML
    private TableColumn<Sumatory, Short> yearColumn;

    @FXML
    private TableColumn<Sumatory, String> totalColumn;

    @FXML
    private Button detailsBtn;

    @FXML
    private Button removeBtn;

    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
    private final CommandInvoker commandInvoker;
    private final TabManager tabManager;
    private final Bus globalBus;
    private ObservableList<Sumatory> selectedEntries;

    /**
     * @param viewModel
     */
    @Inject
    public SumatoryListController(SumatoryListViewModel viewModel, CommandInvoker commandInvoker,
            TabManager tabManager, Bus globalBus) {
        super(tabManager);
        this.viewModel = viewModel;
        this.commandInvoker = commandInvoker;
        this.tabManager = tabManager;
        this.globalBus = globalBus;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.setupTable();
        this.setupSelectionModel();
        this.globalBus.getFromBus(WorkspaceController.EMPLOYEE_KEY).ifPresentOrElse(
                this.viewModel.employee::set,
                this::forceTabExit);
        this.viewModel.searchEntities();
    }

    private void setupTable() {
        this.monthColumn.setCellValueFactory(monthCell -> {
            return new ReadOnlyStringWrapper(StringUtilities.toFirstUpper(monthFormatter.format(
                    LocalDate.of(monthCell.getValue().getYear(), monthCell.getValue().getMonth(), 1))));
        });
        this.monthColumn.setSortable(true);
        this.yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        this.yearColumn.setCellFactory(
                StyleableTableCell.forTableColumn(new ShortStringConverter(), "num-badge"));
        this.yearColumn.setSortable(true);
        this.totalColumn.setCellValueFactory(new DurationValueFactory<>(sumatory -> sumatory.getTotal()));
        this.totalColumn.setSortable(true);
        this.totalColumn.setCellFactory(StyleableTableCell.forTableColumn("num-badge"));
        sumatoryTable.setRowFactory(view -> {
            var row = new TableRow<Sumatory>();
            row.setOnMouseClicked(event -> {
                if (row.getItem() != null && event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    SumatoryUtils.openSumatory(tabManager, row.getItem());
                    event.consume();
                }
            });
            return row;
        });
        this.sumatoryTable.setPlaceholder(new EmptyTableNode(this.getMessage("sumatory.table.empty")));
        this.sumatoryTable.setItems(this.viewModel.entities);
    }

    private void setupSelectionModel() {
        this.selectedEntries = this.sumatoryTable.getSelectionModel().getSelectedItems();
        this.selectedEntries.addListener((ListChangeListener<Sumatory>) change -> {
            int selected = selectedEntries.size();
            detailsBtn.setDisable(selected != 1);
            removeBtn.setDisable(selected == 0);
        });
        this.sumatoryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void createSumatory() {
        this.commandInvoker.executeCommand(new CreateSumatoryCommand(this.sumatoryTable::scrollTo));
    }

    @FXML
    private void deleteSelected() {
        this.commandInvoker.executeCommand(new DeleteSumatoryCommand(new ArrayList<>(this.selectedEntries)));
        this.sumatoryTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void detailsButtonSumatory() {
        if (this.selectedEntries.size() == 1)
            SumatoryUtils.openSumatory(tabManager, this.selectedEntries.getFirst());
    }
}
