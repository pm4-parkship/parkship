import { ParkingLotModel, ParkingLotState } from '../models';

export const parkingLotDummyData: ParkingLotModel[] = [
  {
    id: 'Parkplatz A1',
    address: 'Dorfstrasse',
    addressNr: '8',
    description: 'Der Parkplatz ist direkt neben der Säule. Also aufpassen!',
    floor: 123,
    latitude: 0,
    longitude: 0,
    nr: 'A1',
    owner: 'benjamin Blümchen',
    pictures: [
      'https://placehold.co/600x600.png',
      'https://placehold.co/600x600.png'
    ],
    price: 1200,
    state: ParkingLotState.released,
    tags: ['nice view', 'nice view', 'nice view', 'nice view']
  },
  {
    id: 'Parkplatz A2',
    address: 'Dorfstrasse',
    addressNr: '8',
    description: 'Der Parkplatz ist direkt neben der Säule. Also aufpassen!',
    floor: 123,
    latitude: 0,
    longitude: 0,
    nr: 'A1',
    owner: 'benjamin Blümchen',
    pictures: [
      'https://placehold.co/600x600.png',
      'https://placehold.co/600x600.png'
    ],
    price: 1200,
    state: ParkingLotState.locked,
    tags: ['nice view', 'nice view', 'nice view', 'nice view']
  }
];
