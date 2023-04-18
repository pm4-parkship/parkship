import ReservationFilter from '../../src/components/my-reservation/reservation-filter';
import ReservationTable from '../../src/components/my-reservation/reservation-table';
import React, { useEffect, useState } from 'react';
import {
  ReservationModel,
  ReservationState
} from '../../src/models/reservation/reservation.model';
import { Link, Typography } from '@mui/material';
import { formatDate } from '../../src/date/date-formatter';
import useUser from '../../src/auth/use-user';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import { RowDataType } from '../../src/components/table/table-row';
import { toast } from 'react-toastify';
import { User } from '../api/user';
import { logger } from '../../src/logger';
import { dummy } from '../../src/data/reservations';

export interface ReservationFilterData {
  states: Set<ReservationState>;
}
const initFilter: ReservationFilterData = { states: new Set() };
const initState = {
  error: null,
  loading: false,
  result: Array<ReservationModel>()
};

const MyReservationPage = () => {
  const { user } = useUser();

  const [filter, setFilter] = useState<ReservationFilterData>(initFilter);
  const [reservations, setReservations] = useState(initState);

  const cancelReservation = (e: ReservationModel) => {
    toast.success('🦄 Wow so easy! ID: ' + e.id);
  };
  const makeCancelCell = (item: ReservationModel): string | JSX.Element => {
    return item.cancelDate ? (
      `${formatDate(new Date(item.cancelDate))}`
    ) : (
      <Link href="#" onClick={() => cancelReservation(item)}>
        <Typography variant={'body2'}>{'stornieren'}</Typography>
      </Link>
    );
  };

  useEffect(() => {
    if (!user) return;
    setReservations({ error: null, loading: true, result: [] });
    fetchReservations(false, user)
      .then((result) => {
        if (result) {
          setReservations({ error: null, loading: false, result: result });
        }
      })
      .catch();
  }, []);

  const filterReservation = (reservation: ReservationModel): boolean => {
    return (
      filter.states.has(reservation.reservationState) || !filter.states.size
    );
  };

  const filteredReservations: Array<RowDataType> = reservations.result
    .filter((item) => filterReservation(item))
    .map((item) => {
      return [
        `${item.id}`,
        `${item.parkingLot.id}`,
        `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
        `${item.tenant.name} ${item.tenant.surname}`,
        `${formatDate(new Date(item.from))} - ${formatDate(new Date(item.to))}`,
        makeCancelCell(item)
      ];
    });

  return (
    <div>
      <ReservationFilter
        updateFilter={(newFilter) => setFilter(() => newFilter)}
      ></ReservationFilter>
      <>
        <Loading loading={reservations.loading} />

        {reservations.result.length > 0 ? (
          <ReservationTable
            reservations={filteredReservations}
          ></ReservationTable>
        ) : (
          <NoData size={reservations.result.length} />
        )}
      </>
    </div>
  );
};

const NoData = ({ size }) =>
  size > 0 ? <Typography>Keine Reservationen gefunden :(</Typography> : null;

const fetchReservations = async (
  useApi: boolean,
  user: User
): Promise<ReservationModel[]> => {
  if (useApi) {
    return await fetch('/backend/reservation', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${user.token}`
      }
    }).then(async (response) => {
      const data = await response.json();
      logger.log(data);
      return data;
    });
  } else {
    return new Promise((resolve) => {
      setTimeout(() => resolve(dummy), 2000);
    });
  }
};
export default MyReservationPage;
