import ConfirmationModal, {
  ReservationAction,
  ReservationConfirmationModalData
} from './confirmation-modal';
import React, { useState } from 'react';
import {
  ModifyReservationModel,
  ReservationModel
} from '../../models/reservation/reservation.model';
import { User } from '../../../pages/api/user';
import { toast } from 'react-toastify';
import { logger } from '../../logger';

interface ModifyReservationProps {
  updateReservation: (newValue: ReservationModel) => void;
  user: User;
  reservation: ReservationModel;
  handleClose: () => void;
}

const ModifyReservationModal = ({
  updateReservation,
  user,
  reservation,
  handleClose
}: ModifyReservationProps) => {
  logger.log(reservation.id);
  const [open, setOpen] = useState(true);
  const close = () => {
    setOpen(false);
    handleClose();
  };

  const modifyReservation = (
    reservation: ReservationModel,
    modified: ReservationConfirmationModalData
  ) => {
    const body: ModifyReservationModel = {
      reservationID: reservation.id,
      parkingLotID: reservation.parkingLot.id,
      from: modified.fromDate,
      to: modified.toDate
    };
    modifyReservationCall(body, user)
      .then((result) => {
        result &&
          toast.success('Anpassung von ' + reservation.id + ' erfolgreich');
        reservation.from = result.from;
        reservation.to = result.to;
        updateReservation(reservation);
        close();
      })
      .catch((reject) => toast.error(reject.message));
  };

  const modalData: ReservationConfirmationModalData = {
    toDate: new Date(reservation.to),
    fromDate: new Date(reservation.from),
    id: reservation.id.toString()
  };

  return (
    <>
      <ConfirmationModal
        showModal={open}
        setShowModal={close}
        data={modalData}
        action={ReservationAction.MODIFY}
        onConfirm={(newValue) => modifyReservation(reservation, newValue)}
      />
    </>
  );
};
const modifyReservationCall = async (
  body: ModifyReservationModel,
  user: User
): Promise<ReservationModel> => {
  return await fetch(`/backend/reservations/${body.reservationID}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    },
    body: JSON.stringify(body)
  }).then(async (response) => {
    if (response.ok) {
      return await response.json();
    }
  });
};
export default ModifyReservationModal;
