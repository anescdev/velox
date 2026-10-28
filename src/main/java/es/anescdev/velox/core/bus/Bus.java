package es.anescdev.velox.core.bus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

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
    protected final Map<BusKey<?>, Object> busData;

    public Bus() {
        this(new HashMap<>());
    }

    protected Bus(Map<BusKey<?>, Object> busData) {
        this.busData = busData;
    }

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

    public <T> void removeFromBus(BusKey<T> key) {
        if(this.contains(key)) this.busData.remove(key);
    }

    public <T> boolean contains(BusKey<T> key) {
        return this.busData.containsKey(key);
    }

    public void clearBus() {
        this.busData.clear();
    }

    public void forEach(BiConsumer<? super BusKey<? super Object>, ? super Object> action) {
        this.busData.forEach((key, val) -> action.accept((BusKey<? super Object>)key, val));
    }
}
