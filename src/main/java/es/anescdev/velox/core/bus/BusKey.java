package es.anescdev.velox.core.bus;

/**
 * Clave compuesta lógica de la entidad None asociada. Ver
 * {@link es.anescdev.velox.core.data.entities.CompositeKey} para el contrato que implementa.
 */
public class BusKey<T> {
    private final String key;
    private final Class<T> keyClass;

    public BusKey(String key, Class<T> keyClass) {
        this.key = key;
        this.keyClass = keyClass;
    }

    protected String key() {
        return this.key;
    }

    protected Class<T> keyClass() {
        return this.keyClass;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        return obj instanceof BusKey oKey && this.key.equals(oKey.key);
    }

}
