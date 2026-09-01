package es.anescdev.velox.context.sumatory.view.commands;

import java.util.function.Consumer;

import javax.inject.Inject;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.sumatory.model.dto.CreateSumatory;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.context.sumatory.view.components.CreateSumatoryDialog;
import es.anescdev.velox.context.sumatory.view.utils.SumatoryUtils;
import es.anescdev.velox.context.sumatory.view.viewmodel.SumatoryListViewModel;
import es.anescdev.velox.core.bus.Bus;
import es.anescdev.velox.core.command.FeatherCommand;
import es.anescdev.velox.core.view.TabManager;
import es.anescdev.velox.core.view.controller.WorkspaceController;
import es.anescdev.velox.core.view.utils.NotificationsUtils;

/**
 * @author AnesCDev
 */

public class CreateSumatoryCommand extends FeatherCommand<Void> {

    @Inject
    private SumatoryListViewModel sumatoryListViewModel;
    @Inject
    private TabManager tabManagerService;
    @Inject
    private Bus globalBus;
    private final Consumer<Sumatory> onCreated;

    /**
     * @author AnesCDev
     */
    public CreateSumatoryCommand(Consumer<Sumatory> onCreated) {
        super();
        this.onCreated = onCreated;
    }

    public CreateSumatoryCommand() {
        super();
        this.onCreated = null;
    }

    @Override
    public Void executeCommand() {
        var employee = this.globalBus.getFromBus(WorkspaceController.EMPLOYEE_KEY);
        if (employee.isEmpty())
            return null;
        var createSumatoryData = new CreateSumatoryDialog().showAndWait();
        if (createSumatoryData.isEmpty())
            return null;
        sumatoryListViewModel
                .createEntity(createSumatoryData
                        .<CreateSumatory>map(missingEmployeeData -> new CreateSumatory(missingEmployeeData.month(),
                                missingEmployeeData.year(), employee.get()))
                        .get())
                .thenAccept(sumatory -> {
                    this.sumatoryListViewModel.entities.add(sumatory);
                    if (this.onCreated != null)
                        this.onCreated.accept(sumatory);
                    SumatoryUtils.openSumatory(tabManagerService, sumatory);
                })
                .exceptionally(
                        err -> {
                            NotificationsUtils.showError(
                                    App.instance().getMessage("notifications.sumatory.create.error"),
                                    App.instance().getMessage(err.getCause().getMessage()));
                            return null;
                        });
        return null;
    }

}
