package ch.zhaw.parkship.reservation;

public class ReservationCanNotBeCanceledException extends Exception {
    public ReservationCanNotBeCanceledException(String message) {
        super(message);
    }
}
