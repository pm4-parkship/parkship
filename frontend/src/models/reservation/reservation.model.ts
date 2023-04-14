import { ParkingLotModel } from '../parking-lot/parking-lot.model';
import { UserModel } from '../user/user.model';

export interface ReservationModel {
  id: number;
  parkingLot: ParkingLotModel;
  tenant: UserModel;
  reservationState: ReservationState;
  reservationFrom: Date;
  reservationTo: Date;
  paymentState: string;
  cancelDate: Date | null;
}

export enum ReservationState {
  OK = 'definitiv',
  XX = 'storniert'
}
