import { OfferModel } from "../offer/offer.model";

export interface CreateParkingLotModel {
    id?: string;
    owner: string;
    description?: string;
    tags?: string[];
    longitude?: number;
    latitude?: number;
    address: string;
    addressNr: string;
    nr: string;
    pictures?: string[];
    price: number;
    offer: OfferModel[];
    //state: ParkingLotState;
}