import ConfirmationModal, {
  ReservationAction,
  ReservationConfirmationModalData
} from './confirmation-modal';
import React, { useState } from 'react';
import { ReservationModel } from '../../models/reservation/reservation.model';
import { toast } from 'react-toastify';
import { User } from '../../../pages/api/user';
import apiClient from '../../../pages/api/api-client';

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

  const onConfirm = (reservation: ReservationModel) => {
    apiClient()
      .user.cancelReservation(reservation.id, user)
      .then((result) => {
        result &&
          toast.success('Stornierung von ' + reservation.id + ' erfolgreich');

        reservation.reservationState = result.reservationState;
        reservation.cancelDate = result.cancelDate;
        updateReservation(reservation);
      })
      .catch(() =>
        toast.error(
          `Stornierung der Reservation ${reservation.id} konnte nicht durchgeführt werden. Versuchen Sie es später nochmal`
        )
      );
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
      onConfirm={() => onConfirm(reservation)}
    />
  );
};

export default CancelReservationModal;
