
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


const CreatePage = ({ user }) => {

  const addParkingLot = (
    newParkingLot: CreateParkingLotModel,
    offers: OfferModel[]
  ) => {
    logger.log("hello world");
    let parkingLotId = 0;
    createParkingLotCall(user, newParkingLot).then((response) => {
      if (response) {
        parkingLotId = response.id;
        // parkingLots.result.push(response);
        // setParkingLots(parkingLots);
      }
    });

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
      }
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
