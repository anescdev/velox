package es.anescdev.velox.core.command;

/**
 * @author AnesCDev
 */
public interface Command<R> {
    public R executeCommand();
}
