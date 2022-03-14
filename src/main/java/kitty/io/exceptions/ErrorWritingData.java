package kitty.io.exceptions;

import kitty.exceptions.UserInputException;

public class ErrorWritingData extends UserInputException {
    public ErrorWritingData() {
        super();
    }

    public ErrorWritingData(String s) {
        super(s);
    }

    public ErrorWritingData(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorWritingData(Throwable cause) {
        super(cause);
    }
}
