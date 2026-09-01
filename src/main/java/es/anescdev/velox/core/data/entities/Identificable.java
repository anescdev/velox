package es.anescdev.velox.core.data.entities;

/**
 * Contrato mínimo que debe cumplir toda entidad persistible: exponer su identificador
 * (simple o, a través de {@link CompositeKey}, compuesto). Lo usan de forma genérica
 * {@code Repository}, {@code AbstractService} y {@code AbstractViewModel}.
 *
 * @param <ID> tipo del identificador
 */
public interface Identificable<ID> {
    ID getId();
}
