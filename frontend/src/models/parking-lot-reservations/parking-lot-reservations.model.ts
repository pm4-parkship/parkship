import { ReservationModel } from '../reservation/reservation.model';

export interface ParkingLotReservationModel {
  current: ReservationModel[];
  past: ReservationModel[];
}
