import React from 'react';
import { Grid } from '@mui/material';
import { CreateParkingModal } from 'src/components/create-parking-modal/create-parking-modal';
import { logger } from 'src/logger';
import {
  CreateParkingLotModel,
  OfferCreateModel,
  OfferModel
} from '../../src/models';
import { toast } from 'react-toastify';
import { useRouter } from 'next/router';
import apiClient from 'pages/api/api-client';

const CreatePage = ({ user }) => {
  const router = useRouter();

  const addParkingLot = (
    newParkingLot: CreateParkingLotModel,
    offers: OfferModel[]
  ) => {
    let parkingLotId = 0;
    apiClient()
      .user.createParkingLot(user, newParkingLot)
      .then((response) => {
        parkingLotId = response.id;

        const offersToCreate: OfferCreateModel[] = offers.map((offer) => {
          return {
            parkingLotId: parkingLotId,
            ...offer
          };
        });

        apiClient()
          .user.createParkingLotOffer(user, offersToCreate)
          .then((response) => {
            logger.log('offer created');
            logger.log(response);
            toast.success(
              `Parkplatz ${newParkingLot.name} wurde erfolgreich erstellt!`
            );
            router.push('/my-parking-lot');
          })
          .catch((e) => new Error(e)); // propagate error
      })
      .catch((e) => {
        toast.error(
          `Parkplatz konnte nicht erstellt werden. Versuchen Sie es später nochmal`
        );
      });
  };

  return (
    <Grid padding={2}>
      <CreateParkingModal addParkingLot={addParkingLot} owner={user.name} />
    </Grid>
  );
};

export default CreatePage;
