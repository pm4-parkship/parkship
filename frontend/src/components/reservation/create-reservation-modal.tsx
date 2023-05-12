import {
  CreateReservationModel,
  ReservationModel
} from '../../models/reservation/reservation.model';
import { User } from '../../../pages/api/user';
import { logger } from '../../logger';
import ConfirmationModal, {
  ReservationAction,
  ReservationConfirmationModalData
} from './confirmation-modal';
import React, { useEffect, useState } from 'react';
import { format } from 'date-fns';
import { toast } from 'react-toastify';

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

  const createReservation = (
    user: User,
    data: CreateReservationConfirmationModalData
  ) => {
    createReservationCall(
      {
        from: format(data.fromDate, 'yyyy-MM-dd'),
        to: format(data.toDate, 'yyyy-MM-dd'),
        parkingLotID: data.parkingLotID
      },
      user
    )
      .then((response) => {
        toast.success('Buchung erfolgreich ' + response.id);
        close();
      })
      .catch((reject) => toast.error(reject.message));
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
      onConfirm={() => createReservation(user, data)}
    />
  );
};
const createReservationCall = async (
  body: CreateReservationModel,
  user: User
): Promise<ReservationModel> => {
  return fetch('/backend/reservations', {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${user.token}`,
      'content-type': 'application/json'
    },
    body: JSON.stringify(body)
  }).then(
    async (response) => {
      const data = await response.json();
      if (response.ok) {
        logger.log(data);
        return data;
      }
      return Promise.reject(new Error(data.message.match(/"(.*)"/)[1]));
    },
    async (reject) => {
      const data = await reject.json();
      return data.message;
    }
  );
};

export default CreateReservationModal;
