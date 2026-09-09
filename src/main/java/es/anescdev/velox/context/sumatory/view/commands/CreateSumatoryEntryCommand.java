package es.anescdev.velox.context.sumatory.view.commands;

import java.util.Optional;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.cod.model.entities.Cod;
import es.anescdev.velox.context.cod.view.components.CodSearcherDialog;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.sumatory.view.utils.GroupedSumatoryEntry;
import es.anescdev.velox.core.command.Command;
import es.anescdev.velox.core.view.utils.NotificationsUtils;
import javafx.collections.ObservableList;

/**
 * @author AnesCDev
 */
public class CreateSumatoryEntryCommand implements Command<GroupedSumatoryEntry> {

    private final ObservableList<GroupedSumatoryEntry> actualEntries;

    private final CodSearcherDialog codSearchDialog;

    public CreateSumatoryEntryCommand(Employee employee, ObservableList<GroupedSumatoryEntry> actualEntries) {
        super();
        this.actualEntries = actualEntries;
        this.codSearchDialog = new CodSearcherDialog(employee);
    }

    @Override
    public GroupedSumatoryEntry executeCommand() {
        Cod cod = null;
        do {
            Optional<Cod> selectedCod = this.codSearchDialog.showAndWait();
            if (selectedCod.isEmpty())
                return null;
            if (!this.actualEntries.stream().anyMatch(entry -> entry.getCod().equals(selectedCod.get()))) {
                cod = selectedCod.get();
            } else
                NotificationsUtils.showError(App.instance().getMessage("cod.dialog.searcher.error.added.title"),
                        App.instance().getMessage(
                                selectedCod.get().getCompleteCod() + " %cod.dialog.searcher.error.added.description"));
        } while (cod == null);
        var groupedEntry = new GroupedSumatoryEntry();
        groupedEntry.setCod(cod);
        return groupedEntry;
    }

}
