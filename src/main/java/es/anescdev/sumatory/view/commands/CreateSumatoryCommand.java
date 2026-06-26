package es.anescdev.sumatory.view.commands;

import java.util.Optional;

import javax.inject.Inject;

import es.anescdev.core.command.FeatherCommand;
import es.anescdev.sumatory.dto.CreateSumatory;
import es.anescdev.sumatory.view.components.CreateSumatoryDialog;
import es.anescdev.sumatory.viewmodel.SumatoryListViewModel;

/**
 * @author AnesCDev
 */

public class CreateSumatoryCommand extends FeatherCommand<Void> {

    @Inject
    private SumatoryListViewModel sumatoryListViewModel;

    /**
     * @author AnesCDev
     */
    public CreateSumatoryCommand() {
        super();
    }

    @Override
    public Void executeCommand() {
        Optional<CreateSumatory> data = new CreateSumatoryDialog().showAndWait();
        if (data.isEmpty())
            return null;
        sumatoryListViewModel.createSumatory(data.get());
        return null;
    }

}
