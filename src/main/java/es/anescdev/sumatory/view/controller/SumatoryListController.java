package es.anescdev.sumatory.view.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.inject.Inject;

import es.anescdev.core.command.CommandInvoker;
import es.anescdev.core.view.TabManager;
import es.anescdev.core.view.controller.BaseController;
import es.anescdev.sumatory.model.entities.Sumatory;
import es.anescdev.sumatory.view.cellvaluefactory.DurationValueFactory;
import es.anescdev.sumatory.view.commands.CreateSumatoryCommand;
import es.anescdev.sumatory.view.commands.DeleteSumatoryCommand;
import es.anescdev.sumatory.view.utils.TimeLogUtils;
import es.anescdev.sumatory.viewmodel.SumatoryListViewModel;
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
import javafx.scene.control.skin.TableColumnHeader;

public class SumatoryListController extends BaseController {

    private final SumatoryListViewModel viewModel;

    @FXML
    private TableView<Sumatory> sumatoryTable;

    @FXML
    private TableColumn<Sumatory, String> monthColumn;

    @FXML
    private TableColumn<Sumatory, Short> yearColumn;

    @FXML
    private TableColumn<Sumatory, String> employeeColumn;

    @FXML
    private TableColumn<Sumatory, String> totalColumn;

    @FXML
    private Button detailsBtn;

    @FXML
    private Button removeBtn;

    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
    private final CommandInvoker commandInvoker;
    private final TabManager tabManager;
    private ObservableList<Sumatory> selectedEntries;

    /**
     * @param viewModel
     */
    @Inject
    public SumatoryListController(SumatoryListViewModel viewModel, CommandInvoker commandInvoker, TabManager tabManager) {
        this.viewModel = viewModel;
        this.commandInvoker = commandInvoker;
        this.tabManager = tabManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.initializeColumns();
        this.setupSelectionModel();
        sumatoryTable.setRowFactory(view -> {
            var row = new TableRow<Sumatory>();
            row.setOnMouseClicked(event ->  {
                if (row.getItem() != null && event.getClickCount() == 2) {
                    TimeLogUtils.openSumatoryDetails(tabManager, row.getItem());
                }
            });
            return row;
        });
    }

    private void initializeColumns() {
        this.monthColumn.setCellValueFactory(monthCell -> {
            return new ReadOnlyStringWrapper(monthFormatter.format(
                    LocalDate.of(monthCell.getValue().getYear(), monthCell.getValue().getMonth(), 1)));
        });
        this.monthColumn.setSortable(true);
        this.yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        this.yearColumn.setSortable(true);
        this.employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
        this.employeeColumn.setSortable(true);
        this.totalColumn.setCellValueFactory(new DurationValueFactory<>(sumatory -> sumatory.getTotal()));
        this.totalColumn.setSortable(true);
    }

    private void setupSelectionModel() {
        this.selectedEntries = this.sumatoryTable.getSelectionModel().getSelectedItems();
        this.selectedEntries.addListener((ListChangeListener<Sumatory>) change -> {
            int selected = selectedEntries.size();
            detailsBtn.setDisable(selected != 1);
            removeBtn.setDisable(selected == 0);
        });
        this.sumatoryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.sumatoryTable.setItems(this.viewModel.sumatories);
        if (this.viewModel.sumatories.isEmpty())
            this.viewModel.searchSumatories();// TODO: Hacer que lo busque inicialmente ordenados por mes y año y
                                              // comprobar como ordenarlo por multiples columnas
    }

    @FXML
    private void createSumatory() {
        this.commandInvoker.executeCommand(new CreateSumatoryCommand());
    }

    @FXML
    private void deleteSelected() {
        this.commandInvoker.executeCommand(new DeleteSumatoryCommand(new ArrayList<>(this.selectedEntries)));
    }
    @FXML
    private void detailsButtonSumatory() {
        if (this.selectedEntries.size() != 1) return;
        TimeLogUtils.openSumatoryDetails(tabManager, this.selectedEntries.getFirst());
    }
}
