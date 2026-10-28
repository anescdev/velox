package es.anescdev.velox.context.customer.view.controller;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javax.inject.Inject;

import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.view.viewmodel.CodCreatorViewModel;
import es.anescdev.velox.context.customer.CustomerModule;
import es.anescdev.velox.context.customer.view.utils.CustomerDetailsStatusBarData;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.view.TabManager;
import es.anescdev.velox.core.view.components.EmptyTableNode;
import es.anescdev.velox.core.view.components.TextFieldActionTableCell;
import es.anescdev.velox.core.view.components.TitledPane;
import es.anescdev.velox.core.view.components.statusbar.StateStatusBar;
import es.anescdev.velox.core.view.controller.StatusBarBaseViewTab;
import es.anescdev.velox.core.view.controller.WorkspaceController;
import es.anescdev.velox.core.view.utils.DurationStringConverter;
import es.anescdev.velox.core.view.utils.DurationValueFactory;
import es.anescdev.velox.core.view.utils.TableCellCommitEvent;

import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * Controlador FXML del dominio customer. Conecta los componentes definidos en
 * el FXML
 * asociado con el ViewModel y los comandos del dominio, siguiendo el patrón de
 * {@code BaseController}/{@code BaseViewTabController}.
 */
public class CustomerDetailsController extends StatusBarBaseViewTab {
        @FXML
        private Button saveCustomer;

        @FXML
        private Button addCod;

        @FXML
        private Button removeCod;

        @FXML 
        private MenuItem deleteCodMenuItem;

        @FXML 
        private MenuItem saveCodsMenuItem;

        @FXML
        private Label customerName;

        @FXML
        private Label customerAbbreviation;

        @FXML
        private TableView<Cod> codTable;

        @FXML
        private TableColumn<Cod, String> codColumn;

        @FXML
        private TableColumn<Cod, String> descriptionColumn;

        @FXML
        private TableColumn<Cod, String> timeWorkedColumn;

        @FXML
        private TitledPane codPane;

        private final CodCreatorViewModel codCreatorViewModel;
        private final Bus bus;
        private final StringConverter<Duration> durationStringConverter;
        private final BooleanProperty disableDeleteProperty = new SimpleBooleanProperty(true);
        private long lastNewIndex = 0;

        /**
         * @param codCreatorViewModel
         */
        @Inject
        public CustomerDetailsController(CodCreatorViewModel codCreatorViewModel, Bus bus, TabManager tabManager,
                        DurationStringConverter converter) {
                super(tabManager);
                this.codCreatorViewModel = codCreatorViewModel;
                this.bus = bus;
                this.durationStringConverter = converter;
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                super.initialize(location, resources);
                this.codCreatorViewModel.isSaving.addListener((obs, oldIsSaving, newIsSaving) -> {
                        if (newIsSaving || this.codTable.getSelectionModel().isEmpty())
                                this.disableDeleteProperty.set(true);
                        else
                                this.disableDeleteProperty.set(false);
                });
                this.codTable.getSelectionModel().getSelectedIndices()
                                .addListener((ListChangeListener<Integer>) change -> {
                                        if (this.codCreatorViewModel.isSaving.get()
                                                        || this.codTable.getSelectionModel().isEmpty())
                                                this.disableDeleteProperty.set(true);
                                        else
                                                this.disableDeleteProperty.set(false);
                                });
                this.saveCustomer.disableProperty().bind(this.codCreatorViewModel.changes.sizeProperty().isEqualTo(0));
                this.saveCodsMenuItem.disableProperty().bind(this.codCreatorViewModel.changes.sizeProperty().isEqualTo(0));
                this.removeCod.disableProperty().bind(disableDeleteProperty);
                this.deleteCodMenuItem.disableProperty().bind(disableDeleteProperty);
                this.codTable.editableProperty().bind(this.codCreatorViewModel.isSaving.map(isSaving -> !isSaving));
        }

        @Override
        public void initData(Bus data) {
                super.initData(data);
                data.getFromBus(CustomerModule.CUSTOMER_KEY)
                                .ifPresentOrElse(
                                                this.codCreatorViewModel.customer::set,
                                                this::forceTabExit);

                this.bus.getFromBus(WorkspaceController.EMPLOYEE_KEY)
                                .ifPresentOrElse(
                                                this.codCreatorViewModel.employee::set,
                                                this::forceTabExit);
                this.setupCodTable();
                codPane.expandedProperty().addListener((obs, wasExpanded, isExpanded) -> {
                        if (isExpanded) {
                                codPane.setMaxHeight(Double.MAX_VALUE);
                                VBox.setVgrow(codPane, Priority.ALWAYS);
                        } else {
                                codPane.setMaxHeight(Region.USE_PREF_SIZE);
                                VBox.setVgrow(codPane, Priority.NEVER);
                        }
                });

                // estado inicial, por si arranca ya colapsado
                if (!codPane.isExpanded()) {
                        codPane.setMaxHeight(Region.USE_PREF_SIZE);
                        VBox.setVgrow(codPane, Priority.NEVER);
                }
                this.bindData();
                this.codCreatorViewModel.searchAllCods();
        }

        @Override
        public boolean canExit() {
                return this.codCreatorViewModel.canExit.get();
        }

        private void setupCodTable() {
                this.codColumn.setCellValueFactory(new PropertyValueFactory<>("cod"));
                this.codColumn.setCellFactory(TextFieldActionTableCell.noAction("cod-badge"));
                this.codColumn.setOnEditCommit(new TableCellCommitEvent<>((cod, newCodValue) -> cod.setCod(newCodValue),
                                this.codCreatorViewModel.changes, this.codCreatorViewModel.originalEntities));
                this.codColumn.setMaxWidth(350);
                this.codColumn.setMinWidth(200);
                this.descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
                this.descriptionColumn.setCellFactory(TextFieldActionTableCell.noAction());
                this.descriptionColumn.setMinWidth(200);
                this.descriptionColumn.setPrefWidth(360);
                this.descriptionColumn.setOnEditCommit(new TableCellCommitEvent<>(
                                (cod, newDescription) -> cod.setDescription(newDescription),
                                this.codCreatorViewModel.changes,
                                this.codCreatorViewModel.originalEntities));
                this.timeWorkedColumn.setCellValueFactory(new DurationValueFactory<Cod>(cod -> cod.getTimeWorked()));
                this.timeWorkedColumn.setCellFactory(TextFieldActionTableCell.noAction("num-badge"));
                this.timeWorkedColumn.setMaxWidth(350);
                this.timeWorkedColumn.setMinWidth(200);
                this.timeWorkedColumn.setOnEditCommit(new TableCellCommitEvent<>(
                                (cod, newDuration) -> cod
                                                .setTimeWorked(this.durationStringConverter.fromString(newDuration)),
                                this.codCreatorViewModel.changes,
                                this.codCreatorViewModel.originalEntities));
                                
                this.codTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                this.codTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
                this.codTable.setPlaceholder(new EmptyTableNode(this.getMessage("cod.table.empty")));
        }

        private void bindData() {
                this.customerName.textProperty()
                                .bind(this.codCreatorViewModel.customer
                                                .map(customer -> customer == null ? "ND" : customer.getName()));
                this.customerAbbreviation.textProperty()
                                .bind(this.codCreatorViewModel.customer
                                                .map(customer -> customer == null ? "ND" : customer.getAbbreviation()));
                this.codTable.setItems(this.codCreatorViewModel.entities);
        }

        @FXML
        private void saveCustomer(ActionEvent event) {
                this.codCreatorViewModel.saveCodsChanges();
        }

        @FXML
        private void addCod(ActionEvent event) {
                Cod cod = Cod.draftCod(
                                (++lastNewIndex) * (-1),
                                null,
                                codCreatorViewModel.customer.get(),
                                codCreatorViewModel.employee.get(),
                                null,
                                Duration.ZERO);
                this.codCreatorViewModel.addCod(cod);
                this.codTable.scrollTo(cod);
                this.codTable.getSelectionModel().clearSelection();
                this.codTable.getSelectionModel().select(cod);
        }

        @FXML
        private void deleteCod(ActionEvent event) {
                var selectedCods = this.codTable.getSelectionModel().getSelectedItems();
                this.codCreatorViewModel.delete(new ArrayList<>(selectedCods));
                this.codTable.getSelectionModel().clearSelection();
        }

        @Override
        protected void updateControlUserData(Consumer<Object> statusBarDataSetter) {
                statusBarDataSetter.accept(new CustomerDetailsStatusBarData(
                                new When(this.codCreatorViewModel.canExit)
                                                .then(StateStatusBar.SAVED)
                                                .otherwise(StateStatusBar.DRAFT)));
        }

}
