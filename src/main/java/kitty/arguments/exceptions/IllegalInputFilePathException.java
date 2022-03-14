package kitty.arguments.exceptions;

import kitty.exceptions.UserInputException;

public class IllegalInputFilePathException extends UserInputException {
    public IllegalInputFilePathException() {
        super();
    }

    public IllegalInputFilePathException(String s) {
        super(s);
    }

    public IllegalInputFilePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalInputFilePathException(Throwable cause) {
        super(cause);
    }
}
