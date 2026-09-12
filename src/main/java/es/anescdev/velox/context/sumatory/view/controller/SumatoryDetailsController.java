package es.anescdev.velox.context.sumatory.view.controller;

import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.sumatory.SumatoryModule;
import es.anescdev.velox.context.sumatory.model.entity.SumatoryEntry;
import es.anescdev.velox.context.sumatory.view.commands.CreateSumatoryEntryCommand;
import es.anescdev.velox.context.sumatory.view.commands.print.GeneratePDFCommand;
import es.anescdev.velox.context.sumatory.view.components.dayselectorcell.DaySelectorCell;
import es.anescdev.velox.context.sumatory.view.utils.GroupedSumatoryEntry;
import es.anescdev.velox.context.sumatory.view.utils.SumatoryStatusBarData;
import es.anescdev.velox.context.sumatory.view.viewmodel.SumatoryDetailsViewModel;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.command.CommandInvoker;
import es.anescdev.velox.core.view.TabManager;
import es.anescdev.velox.core.view.components.EmptyTableNode;
import es.anescdev.velox.core.view.components.StyleableTableCell;
import es.anescdev.velox.core.view.components.statusbar.StateStatusBar;
import es.anescdev.velox.core.view.controller.StatusBarBaseViewTab;
import es.anescdev.velox.core.view.controller.WorkspaceController;
import es.anescdev.velox.core.view.utils.DescriptiveDurationConverter;
import es.anescdev.velox.core.view.utils.DurationValueFactory;

import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;

/**
 * Controlador FXML del dominio sumatory. Conecta los componentes definidos en
 * el FXML
 * asociado con el ViewModel y los comandos del dominio, siguiendo el patrón de
 * {@code BaseController}/{@code BaseViewTabController}.
 */
public class SumatoryDetailsController extends StatusBarBaseViewTab {
        @FXML
        private Button exportSumatoryButton;

        @FXML
        private Button saveSumatoryBtn;

        @FXML
        private Button addSumatoryEntryBtn;

        @FXML
        private Button removeSumatoryEntryBtn;

        @FXML
        private MenuItem saveSumatoryMenuItem;

        @FXML
        private MenuItem addSumatoryEntryMenuItem;

        @FXML
        private MenuItem removeSumatoryEntryMenuItem;

        @FXML
        private MenuItem exportSumatoryMenuItem;

        @FXML
        private Label monthTotalLabel;

        @FXML
        private TableView<GroupedSumatoryEntry> sumatoryEntryTable;

        @FXML
        private TableColumn<GroupedSumatoryEntry, String> codColumn;

        @FXML
        private TableColumn<GroupedSumatoryEntry, String> descriptionColumn;

        @FXML
        private TableColumn<GroupedSumatoryEntry, List<Byte>> dayColumn;

        @FXML
        private TableColumn<GroupedSumatoryEntry, String> totalColumn;

        @FXML
        private TableColumn<GroupedSumatoryEntry, String> timeWorkedPerDayColumn;

        private final SimpleIntegerProperty selectedEntries = new SimpleIntegerProperty(0);

        private final SumatoryDetailsViewModel sumatoryDetailsViewModel;
        private final CommandInvoker commandInvoker;
        private final StringConverter<Duration> durationConverter;
        private final Bus globalBus;

        public final SimpleListProperty<GroupedSumatoryEntry> groupedEntities = new SimpleListProperty<>(
                        FXCollections.observableArrayList());

        private long generatedId = 0;

        /**
         * @param sumatoryDetailsViewModel
         */
        @Inject
        public SumatoryDetailsController(SumatoryDetailsViewModel sumatoryDetailsViewModel,
                        TabManager tabManager, CommandInvoker commandInvoker, Bus globalBus) {
                super(tabManager);
                this.sumatoryDetailsViewModel = sumatoryDetailsViewModel;
                this.commandInvoker = commandInvoker;
                this.durationConverter = new DescriptiveDurationConverter();
                this.globalBus = globalBus;
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                super.initialize(location, resources);
                this.saveSumatoryBtn.disableProperty().bind(this.sumatoryDetailsViewModel.canExit);
                this.saveSumatoryMenuItem.disableProperty().bind(this.sumatoryDetailsViewModel.canExit);
                var disabledRemove = this.sumatoryDetailsViewModel.isSaving.or(this.selectedEntries.isEqualTo(0));
                this.removeSumatoryEntryBtn.disableProperty().bind(disabledRemove);
                this.removeSumatoryEntryMenuItem.disableProperty().bind(disabledRemove);
                var disabledExport = this.groupedEntities.sizeProperty().isEqualTo(0)
                                .or(this.sumatoryDetailsViewModel.changes.sizeProperty().greaterThan(0));
                this.exportSumatoryButton.disableProperty().bind(disabledExport);
                this.exportSumatoryMenuItem.disableProperty().bind(disabledExport);
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
                this.setupCodTable();
                this.bindData();
                this.sumatoryDetailsViewModel.searchAllCods();
        }

        @Override
        public boolean canExit() {
                return this.sumatoryDetailsViewModel.canExit.get();
        }

        private void setupCodTable() {
                this.sumatoryEntryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                this.sumatoryEntryTable.editableProperty()
                                .bind(this.sumatoryDetailsViewModel.isSaving.map(isSaving -> !isSaving));
                this.codColumn.setCellValueFactory(
                                cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCod().getCompleteCod()));
                this.codColumn.setCellFactory(StyleableTableCell.forTableColumn("cod-badge"));
                this.codColumn.setEditable(false);
                this.descriptionColumn.setCellValueFactory(
                                cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCod().getDescription()));
                this.descriptionColumn.setEditable(false);
                this.dayColumn.setCellValueFactory(new PropertyValueFactory<>("days"));
                var sumatory = this.sumatoryDetailsViewModel.sumatory.get();
                this.dayColumn.setCellFactory(DaySelectorCell.forTableColumn(
                                sumatory.getYear(),
                                sumatory.getMonth(),
                                this::onSelectedDay,
                                this::onUnselectedDay));
                this.totalColumn.setCellFactory(StyleableTableCell.forTableColumn("num-badge"));
                this.totalColumn.setCellValueFactory(
                                new DurationValueFactory<>((sumatoryEntry) -> sumatoryEntry.getTotalTimeWorked()));
                this.totalColumn.setEditable(false);
                this.timeWorkedPerDayColumn.setCellFactory(StyleableTableCell.forTableColumn("num-badge"));
                this.timeWorkedPerDayColumn.setCellValueFactory(
                                new DurationValueFactory<>((sumatoryEntry) -> sumatoryEntry.getCod().getTimeWorked()));
                this.timeWorkedPerDayColumn.setEditable(false);
                this.sumatoryEntryTable
                                .setPlaceholder(new EmptyTableNode(this.getMessage("sumatoryentry.table.empty")));
                this.sumatoryEntryTable.getSelectionModel().getSelectedItems()
                                .addListener((ListChangeListener<GroupedSumatoryEntry>) change -> {
                                        this.selectedEntries.set(this.sumatoryEntryTable.getSelectionModel()
                                                        .getSelectedItems().size());
                                });
        }

        private void bindData() {
                this.monthTotalLabel.textProperty()
                                .bind(this.sumatoryDetailsViewModel.total.map(this.durationConverter::toString));
                this.sumatoryDetailsViewModel.setOnSearchedEntries(entries -> {
                        var grouped = entries.stream()
                                        .collect(Collectors.groupingBy(entry -> entry.getCod().getInternalId()));
                        var mappedEntries = grouped.entrySet().stream().map(entrySet -> {
                                GroupedSumatoryEntry groupedSumatoryEntry = new GroupedSumatoryEntry();
                                var firstItem = entrySet.getValue().getFirst();
                                groupedSumatoryEntry.setCod(firstItem.getCod());
                                groupedSumatoryEntry.setTotalTimeWorked(firstItem.getCod().getTimeWorked()
                                                .multipliedBy(entrySet.getValue().size()));
                                entrySet.getValue().forEach(entry -> {
                                        groupedSumatoryEntry.getDaysEntry().put(entry.getDay(), entry);
                                        groupedSumatoryEntry.getDays().add(entry.getDay());
                                });
                                groupedSumatoryEntry.getDays().sort(Byte::compare);
                                return groupedSumatoryEntry;
                        }).toList();
                        this.groupedEntities.addAll(mappedEntries);
                });
                this.sumatoryEntryTable.setItems(this.groupedEntities);
        }

        @FXML
        private void saveSumatory(ActionEvent event) {
                this.sumatoryDetailsViewModel.saveCodsChanges();
        }

        @FXML
        private void createSumatoryEntry(ActionEvent event) {
                this.globalBus.getFromBus(WorkspaceController.EMPLOYEE_KEY)
                                .flatMap(employee -> this.commandInvoker
                                                .executeCommand(new CreateSumatoryEntryCommand(employee,
                                                                this.groupedEntities)))
                                .ifPresent(groupedEntry -> {
                                        this.groupedEntities.add(groupedEntry);
                                        this.sumatoryEntryTable.getSelectionModel().clearSelection();
                                        this.sumatoryEntryTable.getSelectionModel().select(groupedEntry);
                                        this.sumatoryEntryTable.scrollTo(groupedEntry);
                                });
        }

        @FXML
        private void deleteSelected(ActionEvent event) {
                var selectedSumatoryEntries = this.sumatoryEntryTable.getSelectionModel().getSelectedItems();
                if (selectedSumatoryEntries.size() < 1)
                        return;
                for (GroupedSumatoryEntry groupedSumatoryEntry : new ArrayList<>(selectedSumatoryEntries)) {
                        this.sumatoryDetailsViewModel.delete(groupedSumatoryEntry.getDaysEntry().values());
                        this.groupedEntities.remove(groupedSumatoryEntry);
                }
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
        protected void updateControlUserData(Consumer<Object> statusBarDataSetter) {
                statusBarDataSetter.accept(new SumatoryStatusBarData(
                                this.sumatoryDetailsViewModel.titleProperty,
                                new When(this.sumatoryDetailsViewModel.canExit)
                                                .then(StateStatusBar.SAVED)
                                                .otherwise(StateStatusBar.DRAFT),
                                this.sumatoryDetailsViewModel.total,
                                this.durationConverter));
        }

        private void onSelectedDay(Byte day, GroupedSumatoryEntry row) {
                var addedEntry = this.sumatoryDetailsViewModel.addSumatoryEntry(SumatoryEntry.draft(
                                (++generatedId) * -1,
                                row.getCod(),
                                this.sumatoryDetailsViewModel.sumatory.get(),
                                day));
                row.getDaysEntry().put(day, addedEntry);
                App.instance().getLogger().info("selected " + day);
                this.sumatoryDetailsViewModel.changes
                                .forEach((entry, change) -> App.instance().getLogger().info(entry + " - " + change));
        }

        private void onUnselectedDay(Byte day, GroupedSumatoryEntry row) {
                this.sumatoryDetailsViewModel.delete(row.getDaysEntry().get(day));
                row.getDaysEntry().remove(day);
                App.instance().getLogger().info("unselected " + day);
                this.sumatoryDetailsViewModel.changes
                                .forEach((entry, change) -> App.instance().getLogger().info(entry + " - " + change));
        }
}
