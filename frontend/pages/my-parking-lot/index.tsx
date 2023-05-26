import React, { useEffect, useState } from 'react';
import MyParkingLotList from '../../src/components/parking-list/my-parking-lot-list';
import {
  ParkingLotModel
} from '../../src/models';
import { User } from '../api/user';
import { logger } from '../../src/logger';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import MyParkingLotReservationTable from '../../src/components/my-parking-lot-reservations/my-parking-lot-reservations';
import { RowDataType } from '../../src/components/table/table-row';
import ReservationStateIcon from '../../src/components/my-reservation/reservation-state-icon';
import { formatDate } from '../../src/date/date-formatter';
import { ParkingLotReservationModel } from '../../src/models/parking-lot-reservations/parking-lot-reservations.model';
import {
  ReservationModel,
  ReservationState
} from '../../src/models/reservation/reservation.model';
import { makeStyles } from '@mui/styles';
import Link from 'src/components/link/link';

const initState = {
  error: null,
  loading: false,
  result: Array<ParkingLotModel>()
};

interface MyParkingLotsTableProps {
  id: number;
  parkingLotName: string;
  location: string;
  reservedBy: string;
  bookingTime: string;
  state: ReservationState;
  active: boolean;
}

const MyParkingLotPage = ({ user }) => {
  const classes = useStyles();
  const [parkingLots, setParkingLots] = useState(initState);
  const [reservations, setReservations] = useState<MyParkingLotsTableProps[]>(
    []
  );

  useEffect(() => {
    fetchParkingLots(user).then((response) =>
      setParkingLots({ error: null, loading: false, result: response })
    );
    fetchReservations(user).then((result: ParkingLotReservationModel) => {
      if (result) {
        setReservations(buildReservationList(result));
      }
    });
  }, []);

  const rowStyleMap = new Map<number, string>();
  reservations.map((value, index) => {
    rowStyleMap.set(index, !value.active ? classes.pastReservation : '');
  });

  const mappedReservations: Array<RowDataType> = reservations.map((item) => {
    return [
      `${item.id}`,
      ReservationStateIcon(item.state),
      `${item.parkingLotName}`,
      `${item.location}`,
      `${item.reservedBy}`,
      `${item.bookingTime}`
    ];
  });
  return (
    <div className={classes.root}>
      <Loading loading={parkingLots.loading} />

      {parkingLots.result && parkingLots.result.length > 0 && (
        <Link href="/create-parking-lot">
          <MyParkingLotList parkings={parkingLots.result} />
        </Link>
      )}

      <MyParkingLotReservationTable
        reservations={mappedReservations}
        styles={rowStyleMap}
      />
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

const fetchReservations = async (
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

const sortByDate = (a: ReservationModel, b: ReservationModel): number => {
  const dateA = new Date(a.from);
  const dateB = new Date(b.from);
  if (dateA > dateB) {
    return -1;
  } else if (dateA < dateB) {
    return 1;
  } else {
    return new Date(b.to).getTime() - new Date(a.to).getTime();
  }
};
const buildReservationList = (
  result: ParkingLotReservationModel
): MyParkingLotsTableProps[] => {
  const parkingSlotsDataPast = result.past
    .sort((a, b) => sortByDate(a, b))
    .map((item) => {
      return {
        id: item.id,
        parkingLotName: item.parkingLot.name,
        location: `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
        reservedBy: item.tenant.name,
        bookingTime: `${formatDate(new Date(item.from))} - ${formatDate(
          new Date(item.to)
        )}`,
        state: ReservationState[item.reservationState],
        active: false
      };
    });

  const parkingSlotsDataFuture = result.current
    .sort((a, b) => sortByDate(a, b))
    .map((item) => {
      return {
        id: item.id,
        parkingLotName: item.parkingLot.name,
        location: `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
        reservedBy: item.tenant.name,
        bookingTime: `${formatDate(new Date(item.from))} - ${formatDate(
          new Date(item.to)
        )}`,
        state: ReservationState[item.reservationState],
        active: true
      };
    });
  return parkingSlotsDataFuture.concat(parkingSlotsDataPast);
};

const useStyles = makeStyles((theme) => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    gap: '3rem'
  },
  pastReservation: {
    // backgroundColor:theme.palette.text,
    backgroundColor:
      theme.palette.mode == 'light'
        ? 'rgba(0, 0, 0, 0.04)'
        : 'rgba(255, 255, 255, 0.08)'
  }
}));
export default MyParkingLotPage;
