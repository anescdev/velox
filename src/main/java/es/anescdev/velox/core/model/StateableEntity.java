package es.anescdev.velox.core.model;

/**
 * Clase base para entidades que necesitan distinguir entre estado "borrador" y "guardado"
 * en la interfaz (ver {@link EntityState}). Aporta el campo de estado común para no
 * repetirlo en cada entidad que lo necesite.
 */
public abstract class StateableEntity {
    private EntityState state;

    protected StateableEntity(EntityState state) {
        this.state = state;
    }

    public boolean isDraft() {
        return this.state == EntityState.DRAFT;
    }

    public void setState(EntityState state) {
        if (state == null)
            return;
        this.state = state;
    }
}
