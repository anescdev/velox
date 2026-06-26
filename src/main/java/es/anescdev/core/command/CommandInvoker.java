package es.anescdev.core.command;

import javax.inject.Singleton;

/**
 * @author AnesCDev
 */
@Singleton
public class CommandInvoker {
    public <R> R executeCommand(Command<R> command) {
        return command.executeCommand();
    }
}
