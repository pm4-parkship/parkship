
import React, { useState } from 'react';
import { Grid, Link, Typography } from '@mui/material';
import { CreateParkingModal } from 'src/components/create-parking-modal/create-parking-modal';
import { logger } from 'src/logger';
import {
  CreateParkingLotModel,
  OfferCreateModel,
  OfferModel,
  ParkingLotModel
} from '../../src/models';
import { User } from 'pages/api/user';
import { toast } from 'react-toastify';
import { useRouter } from 'next/router';

const CreatePage = ({ user }) => {
  const router = useRouter();

  const addParkingLot = (
    newParkingLot: CreateParkingLotModel,
    offers: OfferModel[]
  ) => {
     let parkingLotId = 0;
    createParkingLotCall(user, newParkingLot).then((response) => {
      if (response) {
        parkingLotId = response.id;

        const offersToCreate: OfferCreateModel[] = offers.map((offer) => {
          return {
            parkingLotId: parkingLotId,
            ...offer
          };
        });

        createParkingLotOfferCall(user, offersToCreate).then((response) => {
          if (response) {
            logger.log("offer created");
            logger.log(response);
            toast.success(`Parkplatz erstellt!`);
            router.push("/my-parking-lot");
          }
        });

      }
    }).catch(e => {
      toast.error(`Parkplatz konnte nicht erstellt werden :(`);
    });
  };


  return (
    <Grid padding={2}>
        <CreateParkingModal addParkingLot={addParkingLot} owner={user.username}/>
    </Grid>
  );
};

const createParkingLotCall = async (
  user: User,
  body: CreateParkingLotModel
): Promise<ParkingLotModel> => {
  logger.log("sende: ");
  logger.log(body);

  return await fetch('/backend/parking-lot', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    },
    body: JSON.stringify(body)
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      logger.log(data);
      return data;
    }
  });
};

const createParkingLotOfferCall = async (
  user: User,
  body: OfferCreateModel[]
): Promise<any> => {
  return await fetch('/backend/offer', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    },
    body: JSON.stringify(body)
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      logger.log(data);
      return data;
    }
  });
};

export default CreatePage;
