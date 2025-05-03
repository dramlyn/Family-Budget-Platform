package fbp.app.exception;

import org.springframework.http.HttpStatus;

public class UserServiceException extends RuntimeException {
    private final HttpStatus httpStatus;

    public UserServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
