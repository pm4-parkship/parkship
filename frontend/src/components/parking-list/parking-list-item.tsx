import {
  Card,
  CardActionArea,
  CardContent,
  Divider,
  Typography
} from '@mui/material';
import { ParkingLotModel } from 'src/models';
import React from 'react';
import Link from 'next/link';

interface Props {
  bcolor: string;
  parking: ParkingLotModel;
}

const ParkingListItem = (props: Props) => {
  const { parking } = props;

  return (
    <Card style={{ margin: 0, padding: 0 }} elevation={4}>
      <Link
        href={{
          pathname: `/my-parking-lot/[id]`,
          query: { id: `${parking.id}` }
        }}
      >
        <CardActionArea
          style={{
            backgroundColor: props.bcolor,
            margin: 0,
            width: 160,
            height: 230
          }}
          itemID={parking.id.toString()}
          // onClick={() => {
          //   logger.log('press');
          //   router.push(`/my-parking-lot/${parking.id}`);
          // }}
        >
          <CardContent>
            <Typography align={'center'}>{`${parking.name}`}</Typography>
            <Divider />
            <Typography fontSize={'14px'} align={'center'}>
              CHF {parking.price} / Tag
            </Typography>
          </CardContent>
        </CardActionArea>
      </Link>
    </Card>
  );
};

export default ParkingListItem;
