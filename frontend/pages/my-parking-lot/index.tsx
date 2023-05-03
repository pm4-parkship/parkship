import React, { useEffect, useState } from 'react';
import MyParkingLotList from '../../src/components/parking-list/my-parking-lot-list';
import { ParkingLotModel } from '../../src/models';
import { User } from '../api/user';
import { logger } from '../../src/logger';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';

const initState = {
  error: null,
  loading: false,
  result: Array<ParkingLotModel>()
};

const MyParkingLotPage = ({ user }) => {
  const [parkingLots, setParkingLots] = useState(initState);

  useEffect(() => {
    fetchParkingLots(user).then((response) =>
      setParkingLots({ error: null, loading: false, result: response })
    );
  }, []);

  return (
    <>
      <Loading loading={parkingLots.loading} />

      {parkingLots.result && parkingLots.result.length > 0 && (
        <MyParkingLotList parkings={parkingLots.result} />
      )}
    </>
  );
};
const fetchParkingLots = async (user: User): Promise<ParkingLotModel[]> => {
  return await fetch('/backend/parking-lot/my-parkinglots', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      logger.log(data);
      return data;
    }
  });
};
export default MyParkingLotPage;
