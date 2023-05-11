import React, { useEffect, useState } from 'react';
import MyParkingLotList from '../../src/components/parking-list/my-parking-lot-list';
import { ParkingLotModel } from '../../src/models';
import { User } from '../api/user';
import { logger } from '../../src/logger';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import { formatDate } from 'src/date/date-formatter';
import { ParkingLotReservationModel } from 'src/models/parking-lot-reservations/parking-lot-reservations.model';
import MyParkinLotReservationTable from 'src/components/my-parking-lot-reservations/my-parking-lot-reservations';


const initState = {
  error: null,
  loading: false,
  result: Array<ParkingLotModel>()
};

const fetchMyParkingLotReservations = async (
  user: User
): Promise<ParkingLotReservationModel> => {
  return await fetch('/backend/parking-lot/reservations', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      return data;
    }
  });
};

const headerNames = [
  'ID',
  'Parkplatzname',
  'Ort',
  'reserviert von',
  'gebucht von - bis',
  'Status'
];

export interface MyParkingLotsTableProps {
  id: number;
  parkingLotName: string;
  location: string;
  reservedBy: string;
  bookingTime: string;
  status: string;
  active: boolean;
}

const MyParkingLotPage = ({ user }) => {
  const [parkingLots, setParkingLots] = useState(initState);
  const [myParkingLotsReservations, setMyParkingLotsReservations] = useState<
    MyParkingLotsTableProps[]
  >([]);

  useEffect(() => {
    fetchParkingLots(user).then((response) =>
      setParkingLots({ error: null, loading: false, result: response })
    );

    fetchMyParkingLotReservations(user)
      .then((result: ParkingLotReservationModel) => {

        logger.log(result);
        const parkingSlotsDataPast = result.past.map((item) => {
          return {
            id: item.id,
            parkingLotName: item.parkingLot.name,
            location: `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
            reservedBy: item.tenant.name,
            bookingTime: `${formatDate(new Date(item.from))} - ${formatDate(
              new Date(item.to)
            )}`,
            status: item.reservationState,
            active: false
          };
        });

        const parkingSlotsDataFuture = result.current.map((item) => {
          return {
            id: item.id,
            parkingLotName: item.parkingLot.name,
            location: `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
            reservedBy: item.tenant.name,
            bookingTime: `${formatDate(new Date(item.from))} - ${formatDate(
              new Date(item.to)
            )}`,
            status: item.reservationState,
            active: true
          };
        });

        setMyParkingLotsReservations(
          parkingSlotsDataPast.concat(parkingSlotsDataFuture)
        );
      })
      .catch((error) => logger.log(error));
  }, []);

  return (
    <>
      <Loading loading={parkingLots.loading} />

      {parkingLots.result && parkingLots.result.length > 0 && (
        <MyParkingLotList parkings={parkingLots.result} />
      )}

      <MyParkinLotReservationTable headerNames={headerNames} reservations={myParkingLotsReservations}/>    
    </>
  );
};
const fetchParkingLots = async (user: User): Promise<ParkingLotModel[]> => {
  return await fetch('/backend/parking-lot/my-parkinglots', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      logger.log(data);
      return data;
    }
  });
};
export default MyParkingLotPage;
