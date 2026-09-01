package es.anescdev.velox.context.sumatory.view.viewmodel;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.app.App;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.context.sumatory.model.dto.CreateSumatory;
import es.anescdev.velox.context.sumatory.model.entity.Sumatory;
import es.anescdev.velox.context.sumatory.model.service.SumatoryService;
import es.anescdev.velox.core.view.viewmodel.AbstractViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;


@Singleton
public class SumatoryListViewModel extends AbstractViewModel<Sumatory, Long, SumatoryService, CreateSumatory>{
    
    public final SimpleObjectProperty<Employee> employee = new SimpleObjectProperty<>();

    /**
     * @param service
     */
    @Inject
    public SumatoryListViewModel(SumatoryService service) {
        super(service);
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public Long initialLastId() {
        return -1L;
    }

    @Override
    public void searchEntities() {
        Thread.startVirtualThread(() -> {
            var result = this.service.searchByEmployee(this.employee.getValue());
            Platform.runLater(() -> {
                if(!result.success()) {
                    App.instance().getLogger().severe(result.message());
                    return;
                }
                if (!this.entities.isEmpty()) this.entities.clear();
                this.entities.addAll(result.entities());
                this.count.set(result.totalEntities());
            });
        });
    }

    
}