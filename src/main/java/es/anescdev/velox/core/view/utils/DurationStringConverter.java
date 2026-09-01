package es.anescdev.velox.core.view.utils;

import java.time.Duration;

import javax.inject.Singleton;

import es.anescdev.velox.core.exceptions.InvalidFormatException;
import javafx.util.StringConverter;

/**
 * @author AnesCDev
 */
@Singleton
public class DurationStringConverter extends StringConverter<Duration> {

    private final static String REGEXP_STRING = "^\\d{1,2}$";

    @Override   
    public String toString(Duration object) {
        var hours = object.toHours();
        var mins = object.toMinutes() - hours * 60;
        return (hours < 10 ? "0" : "") + hours + ":" + (mins < 10 ? "0" : "") + mins;
    }

    @Override
    public Duration fromString(String string) {
        var durationSplitted = string.split(":");
        if (durationSplitted.length < 2
                || !durationSplitted[0].matches(REGEXP_STRING)
                || !durationSplitted[1].matches(REGEXP_STRING))
            throw new InvalidFormatException("notifications.sumatory.error.timeWorkedFormat");

        return Duration.parse("PT" + durationSplitted[0] + "H" + durationSplitted[1] + "M");
    }

}
