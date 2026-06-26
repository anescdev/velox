package es.anescdev.sumatory.view.controller;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.inject.Inject;

import es.anescdev.core.command.CommandInvoker;
import es.anescdev.core.view.controller.BaseController;
import es.anescdev.sumatory.model.Sumatory;
import es.anescdev.sumatory.utils.SumatoryList;
import es.anescdev.sumatory.view.commands.CreateSumatoryCommand;
import es.anescdev.sumatory.view.commands.DeleteSumatoryCommand;
import es.anescdev.sumatory.viewmodel.SumatoryListViewModel;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private ObservableList<Sumatory> selectedEntries;

    /**
     * @param viewModel
     */
    @Inject
    public SumatoryListController(SumatoryListViewModel viewModel, CommandInvoker commandInvoker) {
        this.viewModel = viewModel;
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.initializeColumns();
        this.setupSelectionModel();
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
        this.totalColumn.setCellValueFactory(totalCell -> {
            Duration dur = totalCell.getValue().getTotal();
            var hours = dur.toHours();
            var secs = dur.getSeconds();
            return new ReadOnlyStringWrapper(
                    (hours < 10 ? "0" : "") + hours + ":" + (secs < 10 ? "0" : "") + dur.getSeconds());
        });
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
        this.commandInvoker.executeCommand(new DeleteSumatoryCommand(new SumatoryList(this.selectedEntries)));
    }
}
