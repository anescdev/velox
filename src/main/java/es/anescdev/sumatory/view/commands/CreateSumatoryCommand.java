package es.anescdev.sumatory.view.commands;

import java.util.Optional;

import javax.inject.Inject;

import es.anescdev.core.command.FeatherCommand;
import es.anescdev.core.view.TabManager;
import es.anescdev.sumatory.model.dto.CreateSumatory;
import es.anescdev.sumatory.view.components.CreateSumatoryDialog;
import es.anescdev.sumatory.view.utils.TimeLogUtils;
import es.anescdev.sumatory.viewmodel.SumatoryListViewModel;

/**
 * @author AnesCDev
 */

public class CreateSumatoryCommand extends FeatherCommand<Void> {

    @Inject
    private SumatoryListViewModel sumatoryListViewModel;
    @Inject
    private TabManager tabManagerService;

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
        sumatoryListViewModel.createSumatory(data.get())
                .thenAccept(sumatory -> {
                    TimeLogUtils.openSumatoryDetails(tabManagerService, sumatory);
                });
        return null;
    }

}
