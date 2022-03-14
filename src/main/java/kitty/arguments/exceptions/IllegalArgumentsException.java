package kitty.arguments.exceptions;

import kitty.exceptions.UserInputException;

public class IllegalArgumentsException extends UserInputException {
    public IllegalArgumentsException() {
        super();
    }

    public IllegalArgumentsException(String s) {
        super(s);
    }

    public IllegalArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentsException(Throwable cause) {
        super(cause);
    }
}
