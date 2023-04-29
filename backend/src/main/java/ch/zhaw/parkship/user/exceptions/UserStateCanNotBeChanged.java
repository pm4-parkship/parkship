package ch.zhaw.parkship.user.exceptions;

import ch.zhaw.parkship.errorhandling.BaseException;
import org.springframework.http.HttpStatus;

public class UserStateCanNotBeChanged extends BaseException {
    public UserStateCanNotBeChanged(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
