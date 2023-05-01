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
import ReservationStateIcon from '../../src/components/my-reservation/reservation-state-icon';

export interface ReservationFilterData {
  states: Set<ReservationState>;
}

const initFilter: ReservationFilterData = { states: new Set() };
const initState = {
  error: null,
  loading: false,
  result: Array<ReservationModel>()
};
const minCancelDate = () => {
  const today = new Date();
  today.setDate(today.getDate() + 2);
  return today;
};

const MyReservationPage = () => {
  const { user } = useUser();

  const [filter, setFilter] = useState<ReservationFilterData>(initFilter);
  const [reservations, setReservations] = useState(initState);

  const openReservationModal = (reservation: ReservationModel) => {
    cancelReservation(reservation.id, user)
      .then((result) => {
        result &&
          toast.success('Stornierung von ' + reservation.id + ' erfolgreich');

        reservation.reservationState = result.reservationState;
        reservation.cancelDate = result.cancelDate;

        reservations.result.map((obj) =>
          obj.id == reservation.id ? reservation : obj
        );
        setReservations({
          error: null,
          loading: false,
          result: reservations.result
        });
      })
      .catch((reject) => toast.error(reject.message));
  };

  const makeCancelCell = (item: ReservationModel): string | JSX.Element => {
    if (new Date(item.from) <= minCancelDate()) {
      return <span></span>;
    } else if (item.cancelDate) {
      return `${formatDate(new Date(item.cancelDate))}`;
    }
    return (
      <Link href="#" onClick={() => openReservationModal(item)}>
        <Typography variant={'body2'}>{'stornieren'}</Typography>
      </Link>
    );
  };

  useEffect(() => {
    setReservations({ error: null, loading: true, result: [] });
    fetchReservations(user)
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
        ReservationStateIcon(item.reservationState),
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

const fetchReservations = async (user: User): Promise<ReservationModel[]> => {
  return await fetch('/backend/reservations', {
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

const cancelReservation = async (
  reservationID: number,
  user: User
): Promise<ReservationModel> => {
  return await fetch(`/backend/reservations/${reservationID}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      return await response.json();
    }
  });
};
export default MyReservationPage;
