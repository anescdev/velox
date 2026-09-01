package es.anescdev.velox.core.services;

/**
 * Claves de los distintos mensajes de usuario que puede producir {@link AbstractService}
 * (creación correcta, error al crear, entidad ya existente...). Cada subclase de servicio
 * asocia estas claves a una clave de i18n concreta en {@code initializeMessages}.
 */
public enum AbstractServiceMessage {
    /**
     * Mensaje mostrado cuando la entidad es creada con éxito
     */
    CREATE_ENTITY,
    /**
     * Mensaje mostrado cuando la entidad no pudo ser creada
     */
    CREATE_ENTITY_ERROR, 
    /**
     * Mensaje mostrado cuando la entidad que se intenta añadir ya existe
     */
    ENTITY_EXISTING_ERROR;
}
