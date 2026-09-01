package es.anescdev.velox.context.sumatory.view.controller;

import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.sumatory.SumatoryModule;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.context.sumatory.view.commands.print.GeneratePDFCommand;
import es.anescdev.velox.context.sumatory.view.utils.CodEditCommitEvent;
import es.anescdev.velox.context.sumatory.view.utils.SumatoryStatusBarData;
import es.anescdev.velox.context.sumatory.view.viewmodel.SumatoryDetailsViewModel;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.command.CommandInvoker;
import es.anescdev.velox.core.view.TabManager;
import es.anescdev.velox.core.view.components.EmptyTableNode;
import es.anescdev.velox.core.view.components.StyleableTableCell;
import es.anescdev.velox.core.view.components.TextFieldActionTableCell;
import es.anescdev.velox.core.view.utils.TextFieldAction;
import es.anescdev.velox.core.view.components.statusbar.CustomStatusBar;
import es.anescdev.velox.core.view.components.statusbar.StateStatusBar;
import es.anescdev.velox.core.view.controller.StatusBarBaseViewTab;
import es.anescdev.velox.core.view.utils.DescriptiveDurationConverter;
import es.anescdev.velox.core.view.utils.DurationValueFactory;
import es.anescdev.velox.core.view.utils.NumberTableCellCommitEvent;

import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;
import javafx.util.converter.ByteStringConverter;

/**
 * Controlador FXML del dominio sumatory. Conecta los componentes definidos en el FXML
 * asociado con el ViewModel y los comandos del dominio, siguiendo el patrón de
 * {@code BaseController}/{@code BaseViewTabController}.
 */
public class SumatoryDetailsController extends StatusBarBaseViewTab {

        @FXML
        private Button saveSumatoryBtn;

        @FXML
        private Button addSumatoryEntryBtn;

        @FXML
        private Button removeSumatoryEntryBtn;

        @FXML
        private Label monthTotalLabel;

        @FXML
        private TableView<SumatoryEntry> sumatoryEntryTable;

        @FXML
        private TableColumn<SumatoryEntry, String> codColumn;

        @FXML
        private TableColumn<SumatoryEntry, String> descriptionColumn;

        @FXML
        private TableColumn<SumatoryEntry, Byte> dayColumn;

        @FXML
        private TableColumn<SumatoryEntry, String> totalColumn;

        private final SumatoryDetailsViewModel sumatoryDetailsViewModel;
        private final CommandInvoker commandInvoker;
        private final StringConverter<Duration> durationConverter;
        private long lastNewIndex = 0;

        /**
         * @param sumatoryDetailsViewModel
         */
        @Inject
        public SumatoryDetailsController(SumatoryDetailsViewModel sumatoryDetailsViewModel,
                        TabManager tabManager, CommandInvoker commandInvoker) {
                super(tabManager);
                this.sumatoryDetailsViewModel = sumatoryDetailsViewModel;
                this.commandInvoker = commandInvoker;
                this.durationConverter = new DescriptiveDurationConverter();
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                super.initialize(location, resources);
                this.saveSumatoryBtn.disableProperty().bind(this.sumatoryDetailsViewModel.canExit);
                this.removeSumatoryEntryBtn.disableProperty().bind(this.sumatoryDetailsViewModel.isSaving);
        }

        @Override
        public void initData(Bus data) {
                super.initData(data);
                data.getFromBus(SumatoryModule.SUMATORY_KEY)
                                .ifPresentOrElse(
                                                sum -> {
                                                        this.sumatoryDetailsViewModel.sumatory.set(sum);
                                                        this.sumatoryDetailsViewModel.total.set(sum.getTotal());
                                                },
                                                this::forceTabExit);
                data.getFromBus(TabManager.TITLE_KEY)
                                .ifPresentOrElse(this.sumatoryDetailsViewModel.titleProperty::set,
                                                this::forceTabExit);
                this.updateControlUserData();
                this.setupCodTable();
                this.bindData();
                this.sumatoryDetailsViewModel.searchAllCods();
        }

        @Override
        public boolean canExit() {
                return this.sumatoryDetailsViewModel.canExit.get();
        }

        private void setupCodTable() {
                MenuItem addMenuItem = new MenuItem(
                                this.getMessage("sumatoryentry.list.action.context.add"));
                addMenuItem.setGraphic(new FontIcon(UniconsLine.PLUS));
                addMenuItem.setOnAction(this::createSumatoryEntry);
                MenuItem deleteMenuItem = new MenuItem(
                                this.getMessage("sumatoryentry.list.action.context.remove"));
                deleteMenuItem.setGraphic(new FontIcon(UniconsLine.TRASH));
                deleteMenuItem.setOnAction(this::deleteSelected);
                this.sumatoryEntryTable.setContextMenu(new ContextMenu(addMenuItem, deleteMenuItem));
                this.sumatoryEntryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                this.sumatoryEntryTable.editableProperty()
                                .bind(this.sumatoryDetailsViewModel.isSaving.map(isSaving -> !isSaving));
                this.codColumn.setCellValueFactory(
                                cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCod().getCompleteCod()));
                this.codColumn.setCellFactory(
                                TextFieldActionTableCell
                                                .withAction(TextFieldAction.onlyIcon(FontIcon.of(UniconsLine.SEARCH),
                                                                null), "cod-badge"));
                this.codColumn.setOnEditCommit(new CodEditCommitEvent(
                                this.sumatoryDetailsViewModel.codService,
                                this.sumatoryDetailsViewModel.changes,
                                this.sumatoryDetailsViewModel.originalEntities,
                                this.sumatoryDetailsViewModel.sumatory.get().getEmployee()));
                this.descriptionColumn.setCellValueFactory(
                                cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCod().getDescription()));
                this.dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
                this.dayColumn.setCellFactory(
                                TextFieldActionTableCell.noAction(new ByteStringConverter(), "num-badge"));
                this.dayColumn.setOnEditCommit(new NumberTableCellCommitEvent<SumatoryEntry, Byte>(
                                (entry, day) -> entry.setDay(day),
                                this.sumatoryDetailsViewModel.changes,
                                this.sumatoryDetailsViewModel.originalEntities,
                                (byte) 1, (byte) 31));
                this.totalColumn.setCellFactory(StyleableTableCell.forTableColumn("num-badge"));
                this.totalColumn.setCellValueFactory(
                                new DurationValueFactory<>((sumatoryEntry) -> sumatoryEntry.getCod().getTimeWorked()));
                this.sumatoryEntryTable
                                .setPlaceholder(new EmptyTableNode(this.getMessage("sumatoryentry.table.empty")));
        }

        private void bindData() {
                this.monthTotalLabel.textProperty()
                                .bind(this.sumatoryDetailsViewModel.total.map(this.durationConverter::toString));
                this.sumatoryEntryTable.setItems(this.sumatoryDetailsViewModel.entities);
        }

        @FXML
        private void saveSumatory(ActionEvent event) {
                this.sumatoryDetailsViewModel.saveCodsChanges();
        }

        @FXML
        private void createSumatoryEntry(ActionEvent event) {
                SumatoryEntry entry = SumatoryEntry.draft(
                                (++this.lastNewIndex) * -1,
                                SumatoryDetailsViewModel.EMPTY_COD,
                                this.sumatoryDetailsViewModel.sumatory.get(),
                                (byte) 1);
                this.sumatoryDetailsViewModel.addSumatoryEntry(entry);
                this.sumatoryEntryTable.scrollTo(entry);
        }

        @FXML
        private void deleteSelected(ActionEvent event) {
                var selectedSumatoryEntries = this.sumatoryEntryTable.getSelectionModel().getSelectedItems();
                this.sumatoryDetailsViewModel.delete(new ArrayList<>(selectedSumatoryEntries));
                this.sumatoryEntryTable.getSelectionModel().clearSelection();
        }

        @FXML
        private void printSumatory(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new ExtensionFilter(
                                App.instance().getMessage("filechooser.extensionfilter.pdf"), "*.pdf", "*.PDF"));
                fileChooser.setInitialFileName(
                                "sum-" + this.sumatoryDetailsViewModel.sumatory.get().getEmployee().getName() + "-"
                                                + this.sumatoryDetailsViewModel.sumatory.get().getFullDate()
                                                                .format(DateTimeFormatter.ofPattern("MM-yyyy"))
                                                + ".pdf");
                this.commandInvoker
                                .executeCommand(new GeneratePDFCommand(
                                                this.sumatoryDetailsViewModel.sumatory.get(),
                                                fileChooser.showSaveDialog(App.instance().getMainStage())));

        }

        @Override
        protected void updateControlUserData() {
                CustomStatusBar.mainStatusBar().setUserData(new SumatoryStatusBarData(
                                this.sumatoryDetailsViewModel.titleProperty,
                                new When(this.sumatoryDetailsViewModel.canExit)
                                                .then(StateStatusBar.SAVED)
                                                .otherwise(StateStatusBar.DRAFT),
                                this.sumatoryDetailsViewModel.total,
                                this.durationConverter));
        }
}
