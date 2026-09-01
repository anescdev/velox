package es.anescdev.velox.context.employee.model.service;

import java.util.function.BiConsumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.anescdev.velox.context.employee.data.repository.EmployeeRepository;
import es.anescdev.velox.context.employee.model.dto.CreateEmployee;
import es.anescdev.velox.context.employee.model.entity.Employee;
import es.anescdev.velox.core.services.AbstractService;
import es.anescdev.velox.core.services.AbstractServiceMessage;

/**
 * @author AnesCDev
 */
@Singleton
public class EmployeeService extends AbstractService<Employee, Long, CreateEmployee> {
    /**
     * @param repo
     */
    @Inject
    public EmployeeService(EmployeeRepository repo) {
        super(repo);
    }

    @Override
    protected void initializeMessages(BiConsumer<AbstractServiceMessage, String> includeMessage) {
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY, "notifications.employee.create");
        includeMessage.accept(AbstractServiceMessage.CREATE_ENTITY_ERROR, "notifications.employee.create.error");
        includeMessage.accept(AbstractServiceMessage.ENTITY_EXISTING_ERROR, "notifications.employee.existing.error");
    }
    
}
