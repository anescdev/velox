package es.anescdev.sumatory.view.commands;

import javax.inject.Inject;

import es.anescdev.App;
import es.anescdev.core.command.FeatherCommand;
import es.anescdev.sumatory.utils.SumatoryList;
import es.anescdev.sumatory.viewmodel.SumatoryListViewModel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author AnesCDev
 */
public class DeleteSumatoryCommand extends FeatherCommand<Void> {

    @Inject
    private SumatoryListViewModel viewModel;

    private final SumatoryList toRemove;

    /**
     * @param viewModel
     */
    public DeleteSumatoryCommand(SumatoryList toRemove) {
        super();
        this.toRemove = toRemove;
    }

    @Override
    /**
     * Los elementos a borrar deberán de ser incluidos en una lista, guardados en la
     * clave "toDelete"
     */
    public Void executeCommand() {
        if (toRemove.size() == 0)
            throw new IllegalArgumentException("The SumatoryList cannot be empty");
        var alert = new Alert(
                AlertType.CONFIRMATION,
                App.getResourceBundle().getString(
                        toRemove.size() > 1 ? "sumatory.dialog.remove.text.multiple" : "sumatory.dialog.remove.text"),
                ButtonType.NO,
                ButtonType.OK);
        alert.showAndWait().ifPresent(button -> {
            if (ButtonType.OK == button) {
                this.viewModel.deleteSumatories(toRemove);
            }
        });
        return null;
    }

}
