package ch.zhaw.parkship.reservation.exceptions;

import ch.zhaw.parkship.errorhandling.BaseException;
import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends BaseException {
    public ReservationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
