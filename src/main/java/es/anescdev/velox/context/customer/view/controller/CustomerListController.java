package es.anescdev.velox.context.customer.view.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

import es.anescdev.velox.context.customer.model.entities.Customer;
import es.anescdev.velox.context.customer.view.commands.CreateCustomerCommand;
import es.anescdev.velox.context.customer.view.commands.DeleteCustomerCommand;
import es.anescdev.velox.context.customer.view.utils.CustomerUtils;
import es.anescdev.velox.context.customer.view.viewmodel.CustomerViewModel;
import es.anescdev.velox.core.command.CommandInvoker;
import es.anescdev.velox.core.view.TabManager;
import es.anescdev.velox.core.view.components.EmptyTableNode;
import es.anescdev.velox.core.view.components.StyleableTableCell;
import es.anescdev.velox.core.view.controller.BaseController;

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

/**
 * Controlador FXML del dominio customer. Conecta los componentes definidos en el FXML
 * asociado con el ViewModel y los comandos del dominio, siguiendo el patrón de
 * {@code BaseController}/{@code BaseViewTabController}.
 */
public class CustomerListController extends BaseController {

    @FXML
    private Button detailsBtn;
    @FXML
    private Button removeBtn;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> abreviationColumn;

    private final CustomerViewModel viewModel;
    private final CommandInvoker commandInvoker;
    private final TabManager tabManager;

    private ObservableList<Customer> selectedEntries;

    /**
     * @param viewModel
     */
    @Inject
    public CustomerListController(CustomerViewModel viewModel, CommandInvoker commandInvoker, TabManager tabManager) {
        this.viewModel = viewModel;
        this.commandInvoker = commandInvoker;
        this.tabManager = tabManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.setupTable();
        this.setupSelectionModel();
        this.viewModel.searchEntities();
    }

    private void setupTable() {
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.abreviationColumn.setCellValueFactory(new PropertyValueFactory<>("abbreviation"));
        this.abreviationColumn.setCellFactory(StyleableTableCell.forTableColumn("num-badge"));
        this.customerTable.setItems(this.viewModel.entities);
        this.selectedEntries = this.customerTable.getSelectionModel().getSelectedItems();
        this.customerTable.setRowFactory(view -> {
            var row = new TableRow<Customer>();
            row.setOnMouseClicked(event -> {
                if (row.getItem() != null && event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    CustomerUtils.openCustomerDetails(tabManager, row.getItem());
                    event.consume();
                }

            });
            return row;
        });
        this.customerTable.setPlaceholder(new EmptyTableNode(this.getMessage("customer.table.empty")));
    }

    private void setupSelectionModel() {
        this.selectedEntries.addListener((ListChangeListener<Customer>) change -> {
            int selected = selectedEntries.size();
            detailsBtn.setDisable(selected != 1);
            removeBtn.setDisable(selected == 0);
        });
        this.customerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.customerTable.setItems(this.viewModel.entities);
    }

    @FXML
    private void detailsButton() {
        CustomerUtils.openCustomerDetails(tabManager, this.selectedEntries.getFirst());
    }

    @FXML
    private void createButton() {
        this.commandInvoker.executeCommand(new CreateCustomerCommand());
    }

    @FXML
    private void deleteSelected() {
        this.commandInvoker.executeCommand(new DeleteCustomerCommand(List.copyOf(selectedEntries)));
        this.customerTable.getSelectionModel().clearSelection();
    }
}
