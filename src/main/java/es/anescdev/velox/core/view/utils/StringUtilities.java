package es.anescdev.velox.core.view.utils;

/**
 * Utilidad de la capa de vista del dominio None (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public class StringUtilities {
    public static String toFirstUpper(String string) {
        if(string.length() < 1) return "";
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}
