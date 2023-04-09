import { ParkingLotModel } from '../../src/models';

export const searchDummy = [
  [
    'Parkplatz A1',
    'in der Ecke',
    'Toni Balloni',
    '01.02.2023 - 01.02.2023',
    'reservieren'
  ],
  [
    'Parkplatz A2',
    'im Schnee',
    'Eisbär Knut',
    '01.02.2023 - 01.02.2023',
    'reservieren'
  ],
  [
    'Parkplatz A3',
    'im Büro',
    'Stanley Parabel',
    '01.02.2023 - 01.02.2023',
    'reservieren'
  ]
];
export const parkingDummyData: ParkingLotModel[] = [
  {
    id: 'Parkplatz A1',
    address: 'Dorfstrasse',
    addressNr: '8',
    description: 'Der Parkplatz ist direkt neben der Säule. Also aufpassen!',
    floor: 123,
    latitude: 0,
    longitude: 0,
    nr: 'A1',
    owner: 'Benjamin Blümchen',
    pictures: [
      'https://placehold.co/600x600.png',
      'https://placehold.co/600x600.png'
    ],
    price: 1200,
    state: 'busy',
    tags: ['nice view', 'nice view', 'nice view', 'nice view']
  }
];
