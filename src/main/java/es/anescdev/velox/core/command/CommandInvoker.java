package es.anescdev.velox.core.command;

import java.util.Optional;

import javax.inject.Singleton;

@Singleton
/**
 * Punto único por el que los controladores ejecutan un {@link Command}, envolviendo el
 * resultado en un {@link java.util.Optional} (útil cuando el comando puede no producir nada,
 * p. ej. si el usuario cancela un diálogo). Mantiene desacoplado "quién ejecuta" de "qué se ejecuta".
 */
public class CommandInvoker {
    public <R> Optional<R> executeCommand(Command<R> command) {
        return Optional.ofNullable(command.executeCommand());
    }
}
