package kitty.arguments.exceptions;

import kitty.exceptions.UserInputException;

public class IllegalOutputFilePathException extends UserInputException {
    public IllegalOutputFilePathException() {
        super();
    }

    public IllegalOutputFilePathException(String s) {
        super(s);
    }

    public IllegalOutputFilePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalOutputFilePathException(Throwable cause) {
        super(cause);
    }
}
