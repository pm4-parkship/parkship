package ch.zhaw.parkship.user.exceptions;

import ch.zhaw.parkship.errorhandling.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}