package es.anescdev.velox.core.model;

/**
 * @author AnesCDev
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
