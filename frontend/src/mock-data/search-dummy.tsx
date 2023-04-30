import { SearchResultModel } from '../models/search/search-result.model';
import { ParkingLotState, UserRole } from '../models';

export const searchDummyData: SearchResultModel[] = [
  {
    id: 'Parkplatz A1',
    address: 'Dorfstrasse',
    addressNr: '8',
    description: 'Der Parkplatz ist direkt neben der Säule. Also aufpassen!',
    floor: 123,
    latitude: 0,
    longitude: 0,
    nr: 'A1',
    owner: {
      id: '1',
      email: 'mail',
      surname: 'benjamin',
      name: 'Blümchen',
      role: UserRole.user
    },
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
    owner: {
      id: '1',
      email: 'mail',
      surname: 'benjamin',
      name: 'Blümchen',
      role: UserRole.user
    },
    pictures: [
      'https://placehold.co/600x600.png',
      'https://placehold.co/600x600.png'
    ],
    price: 1200,
    state: ParkingLotState.locked,
    tags: ['nice view', 'nice view', 'nice view', 'nice view']
  }
];
