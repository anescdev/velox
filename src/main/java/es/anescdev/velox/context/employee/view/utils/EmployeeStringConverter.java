package es.anescdev.velox.context.employee.view.utils;

import es.anescdev.velox.context.employee.model.entity.Employee;
import javafx.util.StringConverter;

/**
 * @author AnesCDev
 */
public class EmployeeStringConverter extends StringConverter<Employee>{

    @Override
    public String toString(Employee object) {
        return object == null ? "" : object.getName();
    }

    @Override
    public Employee fromString(String string) {
        return null;
    }
    
}
