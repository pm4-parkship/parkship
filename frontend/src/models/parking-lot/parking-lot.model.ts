import { UserModel } from '../user/user.model';

export const DUMMY_TAGS: TagData[] = [
  { id: 1, name: 'überdacht' },
  { id: 2, name: 'im Schatten' },
  { id: 3, name: 'Ladestation' },
  { id: 4, name: 'barrierefrei' },
  { id: 5, name: 'Garage' },
  { id: 6, name: 'Überwacht' },
  { id: 7, name: 'Niedrige Einfahrtshöhe' },
  { id: 8, name: 'Zugangskontrolle' },
  { id: 9, name: 'Nahverkehrsanbindung' }
];
export interface TagData {
  id: number;
  name: string;
}

export interface ParkingLotModel {
  id: number;
  name: string;
  owner: UserModel;
  description: string;
  tags: TagData[];
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
export type UpdateParkingLotModel = Omit<
  ParkingLotModel,
  'id' | 'owner' | 'state' | 'floor' | 'nr' | 'pictures'
> & {
  offers: OfferModel[];
};
export type CreateParkingLotModel = Omit<
  ParkingLotModel,
  'id' | 'owner' | 'state' | 'floor' | 'nr' | 'pictures'
> & {
  offers: OfferModel[];
};
export const EMPTY_CREATE_PARKINGLOT: CreateParkingLotModel = {
  name: '',
  description: '',
  tags: [],
  longitude: 8.53285841502799, //zhaw coords
  latitude: 47.37773821639167,
  address: '',
  addressNr: '',
  offers: [],
  price: 0
};
export interface OfferModel {
  id?: number;
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

export type OfferCreateModel = Omit<OfferModel, ''> & {
  parkingLotId: number;
};

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
