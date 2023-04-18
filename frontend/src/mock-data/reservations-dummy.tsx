import {
  ReservationModel,
  ReservationState
} from '../models/reservation/reservation.model';
import { ParkingLotState, UserRole } from '../models';

export const dummy: Array<ReservationModel> = [
  {
    id: 1,
    parkingLot: {
      id: 'parking lot 3',
      owner: 'John Smith',
      description: 'Parking lot description',
      tags: ['tag1', 'tag2'],
      longitude: -122.084,
      latitude: 37.422,
      address: '123 Main St',
      addressNr: '12',
      floor: 1,
      nr: 'A',
      pictures: ['url1', 'url2'],
      price: 10,
      state: ParkingLotState.released
    },
    tenant: {
      id: '1',
      name: 'Jane',
      surname: 'Doe',
      role: UserRole.user,
      email: 'jane.doe@example.com'
    },
    reservationState: ReservationState.OK,
    from: new Date(),
    to: new Date(),
    paymentState: 'paid',
    cancelDate: null
  },
  {
    id: 2,
    parkingLot: {
      id: 'parking lot 1',
      owner: 'John Smith',
      description: 'Parking lot description',
      tags: ['tag1', 'tag2'],
      longitude: -122.084,
      latitude: 37.422,
      address: '123 Main St',
      addressNr: '12',
      floor: 1,
      nr: 'A',
      pictures: ['url1', 'url2'],
      price: 10,
      state: ParkingLotState.released
    },
    tenant: {
      id: '1',
      name: 'Jane',
      surname: 'Doe',
      role: UserRole.admin,
      email: 'jane.doe@example.com'
    },
    reservationState: ReservationState.XX,
    from: new Date(),
    to: new Date(),
    paymentState: 'paid',
    cancelDate: new Date()
  }
];
