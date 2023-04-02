import React, { useState } from 'react';
import { useRouter } from 'next/router';
import { makeStyles } from '@mui/styles';
import { ParkingLotModel } from '../../src/models';
import ParkingDetailModal from '../../src/components/parking-detail-modal/parking-detail-modal';

const Index = () => {
  const classes = useStyles();
  const router = useRouter();
  const [showModal, setShowModal] = useState(true);

  const parkingDummyData: ParkingLotModel = {
    address: 'This is my address',
    addressNr: '23a',
    description: 'This is my description',
    floor: 123,
    latitude: 0,
    longitude: 0,
    nr: 1,
    owner: 'My name',
    pictures: [
      'https://placehold.co/600x400.png',
      'https://placehold.co/600x400.png',
      'https://placehold.co/600x400.png',
      'https://placehold.co/600x400.png'
    ],
    price: 1200,
    state: 'busy',
    tags: ['nice view', 'nice view', 'nice view', 'nice view']
  };

  return (
    <div className={classes.superRoot}>
      <ParkingDetailModal
        showModal={showModal}
        setShowModal={setShowModal}
        parkingLotModel={parkingDummyData}
      />
    </div>
  );
};

const useStyles = makeStyles((theme) => ({
  superRoot: {
    maxWidth: '2000px',
    margin: '0 auto'
  }
}));

export default Index;
