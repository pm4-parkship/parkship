import { User } from '../../../pages/api/user';
import ConfirmationModal, {
  ReservationAction,
  ReservationConfirmationModalData
} from './confirmation-modal';
import React, { useEffect, useState } from 'react';
import { format } from 'date-fns';
import { toast } from 'react-toastify';
import apiClient from '../../../pages/api/api-client';

export interface CreateReservationConfirmationModalData
  extends ReservationConfirmationModalData {
  parkingLotID: number;
}

interface CreateReservationProps {
  user: User;
  data: CreateReservationConfirmationModalData;
  onClose: () => void;
}

const CreateReservationModal = ({
  user,
  data,
  onClose
}: CreateReservationProps) => {
  const [openConfirmationModal, setOpenConfirmationModal] = useState(false);

  const onConfirm = (
    user: User,
    data: CreateReservationConfirmationModalData
  ) => {
    apiClient()
      .user.createReservation(
        {
          from: format(data.fromDate, 'yyyy-MM-dd'),
          to: format(data.toDate, 'yyyy-MM-dd'),
          parkingLotID: data.parkingLotID
        },
        user
      )
      .then((response) => {
        toast.success('Reservation erfolgreich ' + response.id);
        close();
      })
      .catch(() =>
        toast.error(
          `Reservation konnte nicht durchgeführt werden. Versuchen Sie es später nochmal`
        )
      );
  };

  useEffect(() => {
    setOpenConfirmationModal(true);
  }, [data]);

  const close = () => {
    onClose();
    setOpenConfirmationModal(false);
  };
  return (
    <ConfirmationModal
      showModal={openConfirmationModal}
      setShowModal={close}
      data={data}
      action={ReservationAction.CREATE}
      onConfirm={() => onConfirm(user, data)}
    />
  );
};

export default CreateReservationModal;
