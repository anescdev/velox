package es.anescdev.shared.domain.exceptions;

public class CreateEntityException extends RuntimeException {

    /**
     * @param message
     */
    public CreateEntityException(String message) {
        super(message);
    }
    
}