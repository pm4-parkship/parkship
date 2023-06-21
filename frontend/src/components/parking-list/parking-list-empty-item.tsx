import { Card, CardActionArea, CardContent } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import React from 'react';
import { useRouter } from 'next/router';

const ParkingListEmptyItem = () => {
  const router = useRouter();
  return (
    <Card style={{ margin: 0, padding: 0 }} elevation={4}>
      <CardActionArea
        style={{
          backgroundColor: 'lightgray',
          margin: 0,
          width: 160,
          height: 230,
          justifyContent: 'center',
          alignItems: 'center',
          textAlign: 'center',
          verticalAlign: 'middle'
        }}
        onClick={() => router.push({ pathname: '/create-parking-lot' })}
      >
        <CardContent>
          <AddIcon fontSize={'large'} />
        </CardContent>
      </CardActionArea>
    </Card>
  );
};

export default ParkingListEmptyItem;
