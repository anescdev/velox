package es.anescdev.velox.core.command;

import java.util.Optional;

import javax.inject.Singleton;

/**
 * @author AnesCDev
 */
@Singleton
public class CommandInvoker {
    public <R> Optional<R> executeCommand(Command<R> command) {
        return Optional.ofNullable(command.executeCommand());
    }
}
