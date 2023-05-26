import ReservationFilter from '../../src/components/my-reservation/reservation-filter';
import ReservationTable from '../../src/components/my-reservation/reservation-table';
import React, { useEffect, useState } from 'react';
import { ReservationModel, ReservationState } from '../../src/models/reservation/reservation.model';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import { RowDataType } from '../../src/components/table/table-row';
import { User } from '../api/user';
import { logger } from '../../src/logger';
import CancelReservationModal from '../../src/components/reservation/cancel-reservation-modal';
import ModifyReservationModal from '../../src/components/reservation/modify-reservation-modal';
import NoData from '../../src/components/loading-buffer/no-data';

export interface ReservationFilterData {
  states: Set<ReservationState>;
}

const initFilter: ReservationFilterData = { states: new Set() };
const initState = {
  error: null,
  loading: false,
  result: Array<ReservationModel>()
};

const MyReservationPage = ({ user }) => {
  const [filter, setFilter] = useState<ReservationFilterData>(initFilter);
  const [reservations, setReservations] = useState(initState);
  const [selectedCancelReservation, setSelectedCancelReservation] =
    useState<ReservationModel | null>(null);
  const [selectedModifyReservation, setSelectedModifyReservation] =
    useState<ReservationModel | null>(null);
  const [, setOpen] = useState(false);
  const handleClose = () => {
    setSelectedModifyReservation(null);
    setSelectedCancelReservation(null);
    setOpen(false);
  };
  const updateReservation = (newValue: ReservationModel) => {
    reservations.result.map((obj) => (obj.id == newValue.id ? newValue : obj));
    setReservations({
      error: null,
      loading: false,
      result: reservations.result
    });
    setSelectedCancelReservation(null);
  };

  const openModifyModal = (row: RowDataType) => {
    const id = Number(row[0]);
    const reservation = reservations.result.filter(
      (value) => value.id == id
    )[0];
    if (
      reservation.reservationState != ReservationState.ACTIVE ||
      new Date(reservation.from) <= new Date()
    )
      return;
    setSelectedModifyReservation(reservation);
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

  const filteredReservations: ReservationModel[] = reservations.result.filter(
    (item) => filter.states.has(item.reservationState) || !filter.states.size
  );

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
            openModifyModal={openModifyModal}
            onCancel={setSelectedCancelReservation}
          ></ReservationTable>
        ) : (
          <NoData
            resultSize={reservations.result.length}
            text={'Keine Reservationen gefunden :('}
          />
        )}
      </>
      {selectedCancelReservation && (
        <CancelReservationModal
          updateReservation={updateReservation}
          reservation={selectedCancelReservation}
          user={user}
          handleClose={handleClose}
        />
      )}
      {selectedModifyReservation && (
        <ModifyReservationModal
          updateReservation={updateReservation}
          reservation={selectedModifyReservation}
          user={user}
          handleClose={handleClose}
        />
      )}
    </div>
  );
};

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

export default MyReservationPage;
