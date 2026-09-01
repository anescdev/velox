package es.anescdev.velox.core.view.utils;

import java.time.Duration;

import es.anescdev.velox.core.exceptions.InvalidFormatException;
import javafx.util.StringConverter;

/**
 * Utilidad de la capa de vista del dominio None (conversión de datos para la UI,
 * apertura de pantallas relacionadas, eventos de edición en tablas, etc.).
 */
public class DescriptiveDurationConverter extends StringConverter<Duration> {

    private final static String REGEXP_STRING = "^\\d+[hm]$";

    @Override
    public String toString(Duration object) {
        var hours = object.toHours();
        var mins = object.toMinutes() - hours * 60;
        return (hours < 10 ? "0" : "") + hours + "h " + (mins < 10 ? "0" : "") + mins + "s";
    }

    @Override
    public Duration fromString(String string) {
        var durationSplitted = string.split(" ");
        if (durationSplitted.length < 2
                || !durationSplitted[0].matches(REGEXP_STRING)
                || !durationSplitted[1].matches(REGEXP_STRING))
            throw new InvalidFormatException("notifications.sumatory.error.timeWorkedFormat");

        return Duration.parse(
            "PT" + durationSplitted[0].substring(0, durationSplitted[0].length()) + 
            "H" + durationSplitted[1].substring(durationSplitted[1].length()) + 
            "M");
    }

}
