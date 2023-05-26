import React, { useEffect, useState } from 'react';
import MyParkingLotList from '../../src/components/parking-list/my-parking-lot-list';
import { ParkingLotModel } from '../../src/models';
import { User } from '../api/user';
import { logger } from '../../src/logger';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import MyParkingLotReservationTable from '../../src/components/my-parking-lot-reservations/my-parking-lot-reservations';
import { ReservationModel } from '../../src/models/reservation/reservation.model';
import { makeStyles } from '@mui/styles';
import ParkingLotsFilter from '../../src/components/my-parking-lot-reservations/parking-lots-filter';

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
    fetchParkingLots(user).then((response) =>
      setParkingLots({ error: null, loading: false, result: response })
    );
    fetchReservations(user).then((result) => {
      if (result) {
        logger.log(result);
        setReservations(result);
      }
    });
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

      {parkingLots.result && parkingLots.result.length > 0 && (
        <MyParkingLotList parkings={parkingLots.result} />
      )}
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

const fetchReservations = async (user: User): Promise<ReservationModel[]> => {
  return await fetch('/backend/parking-lot/reservations', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      return data.current.concat(data.past);
    }
  });
};

const useStyles = makeStyles((theme) => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    gap: '3rem'
  }
}));
export default MyParkingLotPage;
