package es.anescdev.velox.context.employee.view.components;

import es.anescdev.velox.context.employee.model.entity.Employee;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Componente que muestra un {@link Employee}: un avatar circular con sus
 * iniciales junto al nombre. Se actualiza automáticamente al cambiar la
 * propiedad {@code employee} (o al llamar a {@link #setEmployee(Employee)}).
 *
 * <p>Ganchos de estilo (defínelos en tu hoja CSS, ajusta los nombres de
 * variable -fx-* a los que ya uses):
 * <pre>
 *   .employee-chip                          → contenedor (HBox)
 *   .employee-chip-avatar                    → círculo del avatar (StackPane)
 *   .employee-chip-avatar.chip-color-1..4    → 4 variantes de color del avatar
 *   .employee-chip-initials                  → iniciales dentro del avatar (Label)
 *   .employee-chip-name                       → nombre del empleado (Label)
 * </pre>
 *
 * Ajusta el paquete a la ubicación real de tus componentes de UI si es
 * distinta a {@code es.anescdev.velox.ui.component}.
 *
 * @author AnesCDev
 */
public class EmployeeChip extends HBox {

    private static final double AVATAR_SIZE = 32;
    private static final int COLOR_VARIANTS = 4;

    private final ObjectProperty<Employee> employee = new SimpleObjectProperty<>(this, "employee");

    private final StackPane avatar = new StackPane();
    private final Label initialsLabel = new Label();
    private final Label nameLabel = new Label();

    public EmployeeChip() {
        this(null);
    }

    public EmployeeChip(Employee initialEmployee) {
        getStyleClass().add("employee-chip");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(10);

        avatar.getStyleClass().add("employee-chip-avatar");
        avatar.setMinSize(AVATAR_SIZE, AVATAR_SIZE);
        avatar.setPrefSize(AVATAR_SIZE, AVATAR_SIZE);
        avatar.setMaxSize(AVATAR_SIZE, AVATAR_SIZE);
        avatar.getChildren().add(initialsLabel);
        initialsLabel.getStyleClass().add("employee-chip-initials");

        nameLabel.getStyleClass().add("employee-chip-name");
        
        getChildren().addAll(avatar, nameLabel);

        employee.addListener((obs, oldValue, newValue) -> refresh(newValue));

        setEmployee(initialEmployee);
    }

    private void refresh(Employee value) {
        avatar.getStyleClass().removeIf(styleClass -> styleClass.startsWith("chip-color-"));

        if (value == null || value.getName() == null || value.getName().isBlank()) {
            initialsLabel.setText("");
            nameLabel.setText("");
            return;
        }

        initialsLabel.setText(extractInitials(value.getName()));
        nameLabel.setText(value.getName());
        avatar.getStyleClass().add("chip-color-" + colorVariantFor(value));
    }

    private String extractInitials(String name) {
        String trimmed = name.trim();
        String[] parts = trimmed.split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }

        String first = parts[0].substring(0, 1);
        String second = parts[1].substring(0, 1);
        return (first + second).toUpperCase();
    }

    /** Variante de color estable para el mismo empleado (usa id si ya está persistido, si no el nombre). */
    private int colorVariantFor(Employee value) {
        Object key = value.getId() != null ? value.getId() : value.getName();
        int hash = Math.abs(key.hashCode());
        return (hash % COLOR_VARIANTS) + 1;
    }

    public ObjectProperty<Employee> employeeProperty() {
        return employee;
    }

    public Employee getEmployee() {
        return employee.get();
    }

    public void setEmployee(Employee value) {
        employee.set(value);
    }
}
