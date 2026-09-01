package es.anescdev.velox.app;

import es.anescdev.velox.core.view.controller.BaseController;

/**
 * @author AnesCDev
 */
public record LoadFXMLResult<T, C extends BaseController>(T node, C controller) {
    
}
