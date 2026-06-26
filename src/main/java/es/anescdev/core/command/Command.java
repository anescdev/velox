package es.anescdev.core.command;

/**
 * @author AnesCDev
 */
public interface Command<R> {
    public R executeCommand();
}
