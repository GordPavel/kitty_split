package kitty.split.exceptions;

import kitty.exceptions.UserInputException;

public class ErrorProcessingData extends UserInputException {
    public ErrorProcessingData() {
        super();
    }

    public ErrorProcessingData(String s) {
        super(s);
    }

    public ErrorProcessingData(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorProcessingData(Throwable cause) {
        super(cause);
    }
}
