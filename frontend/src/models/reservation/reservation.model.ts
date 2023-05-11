import { ParkingLotModel } from '../parking-lot/parking-lot.model';
import { UserModel } from '../user/user.model';

export interface ReservationModel {
  id: number;
  parkingLot: ParkingLotModel;
  tenant: UserModel;
  reservationState: ReservationState;
  from: Date;
  to: Date;
  paymentState: string;
  cancelDate: Date | null;
}

export enum ReservationState {
  ACTIVE = 'ACTIVE',
  CANCELED = 'CANCELED'
}

export const ReservationStateLabel = new Map<ReservationState, string>([
  [ReservationState.ACTIVE, 'definitive Reservation'],
  [ReservationState.CANCELED, 'stornierte Reservation']
]);

export const ReservationStateToString = (state: ReservationState) =>
  state == ReservationState.ACTIVE ? 'definitiv' : 'storniert';

export interface CreateReservationModel {
  parkingLotID: number;
  from: string;
  to: string;
}

export interface ModifyReservationModel {
  parkingLotID: number;
  reservationID: number;
  from: Date;
  to: Date;
}
