package ch.zhaw.parkship.reservation.exceptions;

import ch.zhaw.parkship.errorhandling.BaseException;
import org.springframework.http.HttpStatus;

public class ReservationCanNotBeCanceledException extends BaseException {
    public ReservationCanNotBeCanceledException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
