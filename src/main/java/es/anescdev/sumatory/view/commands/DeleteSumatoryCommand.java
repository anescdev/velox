package es.anescdev.sumatory.view.commands;

import java.util.List;

import javax.inject.Inject;

import es.anescdev.App;
import es.anescdev.core.command.FeatherCommand;
import es.anescdev.sumatory.model.entities.Sumatory;
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

    private final List<Sumatory> toRemove;

    /**
     * @param viewModel
     */
    public DeleteSumatoryCommand(List<Sumatory> toRemove) {
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
                ButtonType.YES);
        alert.showAndWait().ifPresent(button -> {
            if (ButtonType.YES == button) {
                this.viewModel.deleteSumatories(toRemove);
            }
        });
        return null;
    }

}
