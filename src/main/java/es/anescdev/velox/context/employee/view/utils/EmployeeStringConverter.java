package es.anescdev.velox.context.employee.view.utils;

import es.anescdev.velox.context.employee.model.entity.Employee;
import javafx.util.StringConverter;

/**
 * Utilidad de la capa de vista del dominio employee (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
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
