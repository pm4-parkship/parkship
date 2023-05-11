import { User } from 'pages/api/user';
import React, { useEffect, useState } from 'react';
import { logger } from 'src/logger';
import { formatDate } from 'src/date/date-formatter';
import { ParkingLotReservationModel } from 'src/models/parking-lot-reservations/parking-lot-reservations.model';
import MyParkinLotReservationTable from 'src/components/my-parking-lot-reservations/my-parking-lot-reservations';
import MyParkingLotPage from 'pages/my-parking-lot';

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

const MyParkingLotsPage = ({ user }) => {
  const [myParkingLotsReservations, setMyParkingLotsReservations] = useState<
    MyParkingLotsTableProps[]
  >([]);

  useEffect(() => {
    fetchMyParkingLotReservations(user)
      .then((result: ParkingLotReservationModel) => {
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
      <MyParkingLotPage user={user}/>
      <MyParkinLotReservationTable headerNames={headerNames} reservations={myParkingLotsReservations}/>      
    </>
  );
};

export default MyParkingLotsPage;
