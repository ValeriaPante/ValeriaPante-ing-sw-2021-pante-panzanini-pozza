package it.polimi.ingsw.Exceptions;

public class PrintWithoutMessageCreationException extends RuntimeException{
    public PrintWithoutMessageCreationException(String message) {
        super(message);
    }
}
