export interface ParkingLotModel {
  id?: string;
  owner: string;
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
  released = 'freigegegben',
  locked = 'gesperrt'
}
