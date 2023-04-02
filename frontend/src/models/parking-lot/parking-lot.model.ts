export interface ParkingLotModel {
  owner: string;
  description: string;
  tags: string[];
  longitude: number;
  latitude: number;
  address: string;
  addressNr: string;
  floor: number;
  nr: number;
  pictures: string[];
  price: number;
  state: string;
}
