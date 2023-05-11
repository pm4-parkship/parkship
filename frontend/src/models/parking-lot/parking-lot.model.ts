import { UserModel } from '../user/user.model';

export interface ParkingLotModel {
  id: number;
  name: string;
  owner: UserModel;
  description: string;
  tags: string[];
  longitude: number;
  latitude: number;
  address: string;
  addressNr: string;
  floor: number;
  nr: string;
  pictures: string[];
  price: number;
  state: ParkingLotState;
}

export enum ParkingLotState {
  active = 'ACTIVE',
  locked = 'LOCKED',
  inactive = 'INACTIVE'
}

export const ParkingLotStateLabel = new Map<ParkingLotState, string>([
  [ParkingLotState.active, 'aktiv'],
  [ParkingLotState.inactive, 'inaktiv'],
  [ParkingLotState.locked, 'gesperrt']
]);
