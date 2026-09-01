package es.anescdev.velox.core.bus;

import java.util.HashMap;
import java.util.Optional;

import javax.inject.Singleton;

/**
 * @author AnesCDev
 */
@Singleton
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
