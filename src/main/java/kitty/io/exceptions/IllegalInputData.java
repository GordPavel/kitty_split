package kitty.io.exceptions;

import kitty.exceptions.UserInputException;

public class IllegalInputData extends UserInputException {
    public IllegalInputData() {
        super();
    }

    public IllegalInputData(String s) {
        super(s);
    }

    public IllegalInputData(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalInputData(Throwable cause) {
        super(cause);
    }
}
