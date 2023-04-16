package ch.zhaw.parkship.reservation;

public class ReservationNotFoundException extends Exception{
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
