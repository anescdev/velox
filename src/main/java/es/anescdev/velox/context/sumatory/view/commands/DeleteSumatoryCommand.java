package es.anescdev.velox.context.sumatory.view.commands;

import java.util.List;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.context.sumatory.view.viewmodel.SumatoryListViewModel;
import es.anescdev.velox.core.command.FeatherCommand;
import es.anescdev.velox.core.view.components.ConfirmationDialog;
import javafx.scene.control.ButtonType;

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
        var alert = new ConfirmationDialog(
            App.instance().getMessage("sumatory.dialog.remove.title"), 
            App.instance().getMessage(
                        toRemove.size() > 1 ? "sumatory.dialog.remove.text.multiple" : "sumatory.dialog.remove.text"));
        alert.showAndWait().ifPresent(button -> {
            if (ButtonType.YES == button) {
                this.viewModel.deleteEntities(toRemove);
            }
        });
        return null;
    }

}
