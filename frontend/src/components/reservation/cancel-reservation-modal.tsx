import ConfirmationModal, {
  ReservationAction,
  ReservationConfirmationModalData
} from './confirmation-modal';
import React, { useState } from 'react';
import { ReservationModel } from '../../models/reservation/reservation.model';
import { toast } from 'react-toastify';
import { User } from '../../../pages/api/user';

interface CancelReservationProps {
  updateReservation: (newValue: ReservationModel) => void;
  user: User;
  reservation: ReservationModel;
  handleClose: () => void;
}

const CancelReservationModal = ({
  updateReservation,
  user,
  reservation,
  handleClose
}: CancelReservationProps) => {
  const [open, setOpen] = useState(true);
  const close = () => {
    setOpen(false);
    handleClose();
  };

  const cancelReservation = (reservation: ReservationModel) => {
    cancelReservationCall(reservation.id, user)
      .then((result) => {
        result &&
          toast.success('Stornierung von ' + reservation.id + ' erfolgreich');

        reservation.reservationState = result.reservationState;
        reservation.cancelDate = result.cancelDate;
        updateReservation(reservation);
      })
      .catch((reject) => toast.error(reject.message));
    close();
  };
  const modalData: ReservationConfirmationModalData = {
    toDate: new Date(reservation.to),
    fromDate: new Date(reservation.from),
    id: reservation.id.toString()
  };
  return (
    <ConfirmationModal
      showModal={open}
      setShowModal={close}
      data={modalData}
      action={ReservationAction.CANCEL}
      onConfirm={() => cancelReservation(reservation)}
    />
  );
};

const cancelReservationCall = async (
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
export default CancelReservationModal;
