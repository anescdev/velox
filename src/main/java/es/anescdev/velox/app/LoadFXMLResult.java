package es.anescdev.velox.app;

import es.anescdev.velox.core.view.controller.BaseController;

/**
 * Envoltorio simple que agrupa el nodo raíz cargado de un FXML junto con su controlador,
 * para no tener que hacer dos llamadas separadas al {@link javafx.fxml.FXMLLoader}.
 *
 * @param <T> tipo del nodo raíz de la escena
 * @param <C> tipo del controlador asociado (debe extender {@link es.anescdev.velox.core.view.controller.BaseController})
 */
public record LoadFXMLResult<T, C extends BaseController>(T node, C controller) {
    
}
