import { Card, CardActionArea, CardContent, Typography } from '@mui/material';
import { ParkingLotModel } from 'src/models';
import React from 'react';

interface Props {
  bcolor: string;
  parking: ParkingLotModel;
}

const ParkingListItem = (props: Props) => {
  const { parking } = props;
  return (
    <Card style={{ margin: 0, padding: 0 }} elevation={4}>
      <CardActionArea
        style={{
          backgroundColor: props.bcolor,
          margin: 0,
          width: 150,
          height: 200
        }}
      >
        <CardContent>
          <Typography>Parkplatz {parking.nr}</Typography>
          <br />
          <Typography>{parking.floor}</Typography>
          <Typography>{parking.address}</Typography>
          <Typography>{parking.addressNr}</Typography>
          <Typography>CHF {parking.price} / Tag</Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
};

export default ParkingListItem;
