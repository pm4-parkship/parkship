import { UserModel } from '../user/user.model';
import { ParkingLotModel } from '../parking-lot/parking-lot.model';

export interface SearchResultModel {
  id?: string;
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
  state: string;
}

export const mapSearchResultToParkingLot = (
  search: SearchResultModel
): ParkingLotModel => {
  return {
    id: search.id,
    owner: search.owner.name,
    description: search.description,
    tags: search.tags,
    longitude: search.longitude,
    latitude: search.latitude,
    address: search.address,
    addressNr: search.addressNr,
    floor: search.floor,
    nr: search.nr,
    pictures: search.pictures,
    price: search.price,
    state: search.state
  };
};
