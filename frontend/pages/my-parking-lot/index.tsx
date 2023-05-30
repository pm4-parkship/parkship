import React, { useEffect, useState } from 'react';
import MyParkingLotList from '../../src/components/parking-list/my-parking-lot-list';
import { ParkingLotModel } from '../../src/models';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import MyParkingLotReservationTable from '../../src/components/my-parking-lot-reservations/my-parking-lot-reservations';
import { ReservationModel } from '../../src/models/reservation/reservation.model';
import { makeStyles } from '@mui/styles';
import ParkingLotsFilter from '../../src/components/my-parking-lot-reservations/parking-lots-filter';
import apiClient from '../api/api-client';
import { toast } from 'react-toastify';

const initState = {
  error: null,
  loading: false,
  result: Array<ParkingLotModel>()
};

export interface MyParkingLotsFilterData {
  names: Set<string>;
  searchTerm: string;
}

const initFilter: MyParkingLotsFilterData = {
  names: new Set(),
  searchTerm: ''
};

const MyParkingLotPage = ({ user }) => {
  const classes = useStyles();
  const [filter, setFilter] = useState<MyParkingLotsFilterData>(initFilter);
  const [parkingLots, setParkingLots] = useState(initState);
  const [reservations, setReservations] = useState<ReservationModel[]>([]);

  useEffect(() => {
    apiClient()
      .user.getMyParkingLots(user)
      .then((response) =>
        setParkingLots({ error: null, loading: false, result: response })
      )
      .catch(() =>
        toast.error(
          'Beim Laden Deiner Parkplätze ist ein Fehler aufgetreten. Versuchen Sie es später nochmal.'
        )
      );

    apiClient()
      .user.getReservationsFromMyParkingLots(user)
      .then((result) => setReservations(result.current.concat(result.past)))
      .catch(() =>
        toast.error(
          'Beim Laden der Reservierungen für Deine Parkplätze ist ein Fehler aufgetreten. Versuchen Sie es später nochmal.'
        )
      );
  }, []);
  const updateFilter = (filter: MyParkingLotsFilterData) => {
    setFilter(filter);
  };

  const filteredReservations = reservations.filter(
    (reservation) =>
      (!filter.names.size || filter.names.has(reservation.parkingLot.name)) &&
      (reservation.parkingLot.name.includes(filter.searchTerm) ||
        reservation.parkingLot.address.includes(filter.searchTerm) ||
        reservation.tenant.name.includes(filter.searchTerm) ||
        reservation.tenant.surname.includes(filter.searchTerm))
  );

  return (
    <div className={classes.root}>
      <Loading loading={parkingLots.loading} />

      {/*{parkingLots.result && parkingLots.result.length > 0 && (*/}
      {/*  <MyParkingLotList parkings={parkingLots.result} />*/}
      {/*)}*/}
      <MyParkingLotList parkings={parkingLots.result} />
      <ParkingLotsFilter
        parkingLots={parkingLots.result}
        updateFilter={updateFilter}
      />
      {reservations.length > 0 ? (
        <MyParkingLotReservationTable reservations={filteredReservations} />
      ) : null}
    </div>
  );
};

const useStyles = makeStyles(() => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    gap: '3rem'
  }
}));

export default MyParkingLotPage;
