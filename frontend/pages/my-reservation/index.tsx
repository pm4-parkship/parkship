import ReservationFilter from '../../src/components/my-reservation/reservation-filter';
import ReservationTable from '../../src/components/my-reservation/reservation-table';
import React, { useEffect, useState } from 'react';
import {
  ReservationModel,
  ReservationState
} from '../../src/models/reservation/reservation.model';
import { format } from 'date-fns';
import { de } from 'date-fns/locale';
import { dummy } from './dummy';
import { logger } from '../../src/logger';
import { Typography } from '@mui/material';

export interface ReservationFilterData {
  states: Set<ReservationState>;
}

const formatDate = (date: Date): string => {
  return format(date, 'P', { locale: de });
};
const MyReservationPage = () => {
  const initFilter: ReservationFilterData = { states: new Set() };

  const [filter, setFilter] = useState<ReservationFilterData>(initFilter);
  const [reservations, setReservations] = useState<Array<ReservationModel>>([]);

  const updateFilter = (newFilter: ReservationFilterData) => {
    setFilter(() => newFilter);
  };

  useEffect(() => {
    fetchReservations(false)
      .then((result) => {
        setReservations(result);
      })
      .catch();
  });

  const filterReservation = (reservation: ReservationModel): boolean => {
    logger.log(reservation);
    return (
      filter.states.has(reservation.reservationState) || !filter.states.size
    );
  };

  const filteredReservations: Array<string[]> = reservations
    .filter((item) => filterReservation(item))
    .map((item) => {
      return [
        `${item.id}`,
        `${item.parkingLot.id}`,
        `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
        `${item.parkingLot.owner}`,
        `${formatDate(item.reservationFrom)} - ${formatDate(
          item.reservationTo
        )}`,
        `${item.cancelDate ? formatDate(item.cancelDate) : 'stornieren'}`
      ];
    });

  return (
    <div>
      <ReservationFilter updateFilter={updateFilter}></ReservationFilter>
      <>
        {reservations.length > 0 ? (
          <ReservationTable
            reservations={filteredReservations}
          ></ReservationTable>
        ) : (
          noData
        )}
      </>
    </div>
  );
};

const noData = (
  <div>
    <Typography>Keine Reservationen gefunden :(</Typography>
  </div>
);

const fetchReservations = (useApi: boolean): Promise<ReservationModel[]> => {
  if (useApi) {
    return new Promise((resolve, reject) => {
      return;
    }); // todo
  } else {
    return new Promise((resolve, reject) => {
      resolve(dummy);
      reject('epic fail');
    });
  }
};
export default MyReservationPage;
