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

interface CreateReservationConfirmationModalData
  extends ReservationConfirmationModalData {
  parkingLotID: number;
}

interface CreateReservationProps {
  user: User;
  data: CreateReservationConfirmationModalData;
}

const CreateReservationModal = ({ user, data }: CreateReservationProps) => {
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
      .then((response) => toast.success('Buchung erfolgreich ' + response.id))
      .catch((reject) => toast.error(reject.message));
  };

  useEffect(() => {
    setOpenConfirmationModal(true);
  }, [data]);

  return (
    <ConfirmationModal
      showModal={openConfirmationModal}
      setShowModal={setOpenConfirmationModal}
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
      if (response.ok) {
        const data = await response.json();
        logger.log(data);
        return data;
      }
    },
    async (reject) => {
      const data = await reject.json();
      return data.message;
    }
  );
};

export default CreateReservationModal;
