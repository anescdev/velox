package es.anescdev.velox.core.bus;

import java.util.HashMap;
import java.util.Optional;

import javax.inject.Singleton;

@Singleton
/**
 * "Buzón" de datos tipado para pasar información entre pantallas/controladores sin
 * acoplarlos directamente entre sí (por ejemplo, el {@code Employee} seleccionado se
 * mete en el {@code Bus} y el {@code WorkspaceController} lo recoge en {@code initData}).
 * Cada dato se identifica por una {@link es.anescdev.velox.core.bus.BusKey}, que además
 * de la clave lleva el tipo esperado, así que {@link #getFromBus(BusKey)} es seguro en
 * cuanto a tipos sin necesidad de casts manuales por parte de quien lo usa.
 * Es {@code @Singleton}: hay una única instancia compartida por toda la sesión de un empleado,
 * y se vacía ({@link #clearBus()}) al volver al selector de empleado.
 */
public class Bus {
    private final HashMap<BusKey<?>, Object> busData = new HashMap<>();

    public <T> void setData(BusKey<T> key, T data) {
        if (!key.keyClass().isAssignableFrom(data.getClass()))
            throw new IllegalArgumentException(
                    String.format("A [%s] object cannot be assigned to a [%s] key", data.getClass().getName(),
                            key.keyClass().getName()));
        this.busData.put(key, data);
    }

    public <T> Optional<T> getFromBus(BusKey<T> key) {
        Object data = this.busData.get(key);
        if (data == null)
            return Optional.empty();
        return Optional.of((T) data);
    }

    public <T> boolean contains(BusKey<T> key) {
        return this.busData.containsKey(key);
    }

    public void clearBus() {
        this.busData.clear();
    }
}
