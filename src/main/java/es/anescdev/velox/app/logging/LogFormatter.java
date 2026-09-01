package es.anescdev.velox.app.logging;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author AnesCDev
 */
public class LogFormatter extends Formatter {
    private final static DateTimeFormatter INSTANT_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    

    @Override
    public String format(LogRecord record) {
        return "[" + record.getLoggerName() + "] [" + INSTANT_FORMATTER.format(record.getInstant()) + "] "
                + record.getLevel() + ": " + record.getMessage()+"\n";
    }

}
