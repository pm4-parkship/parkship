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

export type CreateParkingLotModel = Omit<
  ParkingLotModel,
  'id' | 'owner' | 'state' | 'longitude' | 'latitude' |'floor' | 'nr' | 'pictures'
> & {
  ownerId: number;
};

export interface OfferModel {
  from: Date;
  to: Date;
  monday: boolean;
  tuesday: boolean;
  wednesday: boolean;
  thursday: boolean;
  friday: boolean;
  saturday: boolean;
  sunday: boolean;
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
