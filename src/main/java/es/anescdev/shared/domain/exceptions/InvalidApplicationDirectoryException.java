package es.anescdev.shared.domain.exceptions;

public class InvalidApplicationDirectoryException extends RuntimeException {
    public InvalidApplicationDirectoryException(String message) {
        super(message);
    }

}