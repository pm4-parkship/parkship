import ReservationFilter from '../../src/components/my-reservation/reservation-filter';
import ReservationTable from '../../src/components/my-reservation/reservation-table';
import React, { useEffect, useState } from 'react';
import {
  ReservationModel,
  ReservationState
} from '../../src/models/reservation/reservation.model';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import { RowDataType } from '../../src/components/table/table-row';
import CancelReservationModal from '../../src/components/reservation/cancel-reservation-modal';
import ModifyReservationModal from '../../src/components/reservation/modify-reservation-modal';
import NoData from '../../src/components/loading-buffer/no-data';
import { toast } from 'react-toastify';
import apiClient from '../api/api-client';

export interface ReservationFilterData {
  states: Set<ReservationState>;
}

const initFilter: ReservationFilterData = { states: new Set() };
const initState = {
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
    setReservations({ loading: true, result: [] });
    apiClient()
      .user.getMyReservations(user)
      .then((result) => {
        setReservations({ loading: false, result: result });
      })
      .catch(() =>
        toast.error(
          'Beim Laden Deiner Reservationen ist ein Fehler aufgetreten. Versuchen Sie es später nochmal.'
        )
      );
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

export default MyReservationPage;
