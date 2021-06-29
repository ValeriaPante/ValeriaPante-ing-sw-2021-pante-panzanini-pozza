package it.polimi.ingsw.Exceptions;

/**
 *It is a runtime exception. It is used in the input manager of the CLI in order to notify to the cli itself
 * that the request from the player correspond to a request of printing something on terminal.
 * This exception contains inside the specific request from the player valued from the input manager itself
 */
public class PrintWithoutMessageCreationException extends RuntimeException{
    public PrintWithoutMessageCreationException(String message) {
        super(message);
    }
}
