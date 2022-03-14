package kitty.exceptions;

public class UserInputException extends IllegalArgumentException {
    public UserInputException() {
        super();
    }

    public UserInputException(String s) {
        super(s);
    }

    public UserInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserInputException(Throwable cause) {
        super(cause);
    }
}
