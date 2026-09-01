package es.anescdev.velox.core.view.utils;

/**
 * @author AnesCDev
 */
public class StringUtilities {
    public static String toFirstUpper(String string) {
        if(string.length() < 1) return "";
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}
