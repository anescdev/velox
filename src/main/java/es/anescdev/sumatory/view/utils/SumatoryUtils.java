package es.anescdev.sumatory.view.utils;

import java.time.Duration;

import lombok.experimental.UtilityClass;

/**
 * @author AnesCDev
 */
@UtilityClass
public class SumatoryUtils {
    public String parseDuration(Duration duration) {
        var hours = duration.toHours();
        var secs = duration.getSeconds();
        return (hours < 10 ? "0" : "") + hours + ":" + (secs < 10 ? "0" : "") + secs;
    }
}
