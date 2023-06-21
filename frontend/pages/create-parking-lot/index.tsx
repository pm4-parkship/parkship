import React from 'react';
import { Grid } from '@mui/material';
import {
  CreateParkingLotModel,
  EMPTY_CREATE_PARKINGLOT,
  OfferCreateModel,
  OfferModel
} from '../../src/models';
import { toast } from 'react-toastify';
import { useRouter } from 'next/router';
import apiClient from 'pages/api/api-client';
import { CreateParkingStepper } from '../../src/components/create-parking/create-parking-lot-stepper';

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
      <CreateParkingStepper
        addParkingLot={addParkingLot}
        owner={user.name}
        parkingLotData={EMPTY_CREATE_PARKINGLOT}
      />
    </Grid>
  );
};

export default CreatePage;
