import React, { useEffect, useState } from 'react';
import { Grid } from '@mui/material';
import { logger } from 'src/logger';
import {
  CreateParkingLotModel,
  EMPTY_CREATE_PARKINGLOT,
  OfferModel,
  UpdateParkingLotModel
} from '../../../src/models';
import { useRouter } from 'next/router';
import apiClient from 'pages/api/api-client';
import { CreateParkingStepper } from '../../../src/components/create-parking/create-parking-lot-stepper';
import { toast } from 'react-toastify';

const ModifyPage = ({ user }) => {
  const router = useRouter();
  const id = router.query.id as unknown as number;

  const [parkingLot, setParkingLot] = useState<CreateParkingLotModel>(
    EMPTY_CREATE_PARKINGLOT
  );
  // const dateA = new Date(a.from);
  // const dateB = new Date(b.from);
  // if (dateA > dateB) {
  //   return 1 * sortOrder;
  // } else if (dateA < dateB) {
  //   return -1 * sortOrder;
  // } else {
  //   return (dateA.getTime() - dateB.getTime()) * sortOrder;
  // }

  useEffect(() => {
    apiClient()
      .user.getParkingLot(id, user)
      .then((resolve) => {
        apiClient()
          .user.getParkingLotOffer(user, id)
          .then((offers) => {
            const data: CreateParkingLotModel = {
              name: resolve.name,
              address: resolve.address,
              addressNr: resolve.addressNr,
              latitude: resolve.latitude,
              longitude: resolve.longitude,
              description: resolve.description,
              tags: resolve.tags,
              price: resolve.price,
              offers:
                offers.sort((a, b) =>
                  new Date(a.from) > new Date(b.from) ? 1 : -1
                ) || []
            };
            setParkingLot(() => data);
          });
      });
    logger.log(parkingLot);
  }, []);

  const updateParkingLot = (
    newParkingLot: CreateParkingLotModel,
    offers: OfferModel[]
  ) => {
    // const parkingLotId = id;
    const payload: UpdateParkingLotModel = {
      ...newParkingLot,
      offers: offers
    };
    logger.log(newParkingLot);
    apiClient()
      .user.updateParkingLot(user, payload, id)
      .then((response) => {
        toast.success(
          `Parkplatz ${newParkingLot.name} wurde erfolgreich aktualisiert!`
        );
        router.push('/my-parking-lot');
      });
  };

  return (
    <Grid padding={2}>
      <CreateParkingStepper
        addParkingLot={updateParkingLot}
        owner={user.name}
        parkingLotData={parkingLot}
      />
    </Grid>
  );
};

export default ModifyPage;
