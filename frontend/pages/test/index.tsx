import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { makeStyles } from '@mui/styles';
import { ParkingLotModel } from '../../src/models';
import ParkingDetailModal from '../../src/components/parking-detail-modal/parking-detail-modal';
import ParkingList from 'src/components/parking-list/parking-list';
import { Alert, CircularProgress } from '@mui/material';

const Index = () => {
  const classes = useStyles();
  const router = useRouter();

  const [showModal, setShowModal] = useState(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isError, setIsError] = useState<boolean>(true);

  useEffect(() => {
    // here to show progress circle and error message
    setTimeout(() => {
      setIsLoading(false);
      setIsError(false);
    }, 2000);
  });

  const parkingDummyData: ParkingLotModel[] = [
    {
      address: 'Dorfstrasse',
      addressNr: '8',
      description:
        'Prakplatz ist ein kleiner Parkplatz, nicht für alle Autos geignet.',
      floor: 123,
      latitude: 0,
      longitude: 0,
      nr: 'A2',
      owner: 'Benjamin Blümchen',
      pictures: [
        'https://placehold.co/600x600.png',
        'https://placehold.co/600x600.png'
      ],
      price: 1200,
      state: 'busy',
      tags: ['nice view', 'nice view', 'nice view', 'nice view']
    },
    {
      address: 'Dorfstrasse',
      addressNr: '8',
      description:
        'Prakplatz ist ein kleiner Parkplatz, nicht für alle Autos geignet.',
      floor: 123,
      latitude: 0,
      longitude: 0,
      nr: 'A2',
      owner: 'Benjamin Blümchen',
      pictures: [
        'https://placehold.co/600x600.png',
        'https://placehold.co/600x600.png'
      ],
      price: 1200,
      state: 'busy',
      tags: ['nice view', 'nice view', 'nice view', 'nice view']
    },
    {
      address: 'Dorfstrasse',
      addressNr: '8',
      description:
        'Prakplatz ist ein kleiner Parkplatz, nicht für alle Autos geignet.',
      floor: 123,
      latitude: 0,
      longitude: 0,
      nr: 'A2',
      owner: 'Benjamin Blümchen',
      pictures: [
        'https://placehold.co/600x600.png',
        'https://placehold.co/600x600.png'
      ],
      price: 1200,
      state: 'busy',
      tags: ['nice view', 'nice view', 'nice view', 'nice view']
    },
    {
      address: 'Dorfstrasse',
      addressNr: '8',
      description:
        'Prakplatz ist ein kleiner Parkplatz, nicht für alle Autos geignet.',
      floor: 123,
      latitude: 0,
      longitude: 0,
      nr: 'A2',
      owner: 'Benjamin Blümchen',
      pictures: [
        'https://placehold.co/600x600.png',
        'https://placehold.co/600x600.png'
      ],
      price: 1200,
      state: 'busy',
      tags: ['nice view', 'nice view', 'nice view', 'nice view']
    },
    {
      address: 'Dorfstrasse',
      addressNr: '8',
      description:
        'Prakplatz ist ein kleiner Parkplatz, nicht für alle Autos geignet.',
      floor: 123,
      latitude: 0,
      longitude: 0,
      nr: 'A2',
      owner: 'Benjamin Blümchen',
      pictures: [
        'https://placehold.co/600x600.png',
        'https://placehold.co/600x600.png'
      ],
      price: 1200,
      state: 'busy',
      tags: ['nice view', 'nice view', 'nice view', 'nice view']
    }
  ];


  return (
    <div className={classes.superRoot}>
      {isLoading && (
        <div className={classes.progressContainer}>
          <CircularProgress color="primary" />{' '}
        </div>
      )}

      {isError && <Alert variant="filled" severity="error">This is a demo error message</Alert>}
      {!isLoading && !isError && <ParkingList parkings={parkingDummyData} />}

      <ParkingDetailModal
        showModal={showModal}
        setShowModal={setShowModal}
        parkingLotModel={parkingDummyData[0]}
      />
    </div>
  );
};

const useStyles = makeStyles(() => ({
  superRoot: {
    maxWidth: '2000px',
    margin: '0 auto'
  },
  progressContainer: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    textAlign: 'center',
    verticalAlign: 'middle',
    height: 150
  }
}));

export default Index;
