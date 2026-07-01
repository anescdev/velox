package es.anescdev;

import es.anescdev.core.view.controller.BaseController;

/**
 * @author AnesCDev
 */
public record LoadFXMLResult<T, C extends BaseController>(T node, C controller) {
    
}
