import { Paper, Typography } from '@mui/material';
import { ParkingLotModel } from 'src/models';

interface Props {
  bcolor: string;
  parking: ParkingLotModel;
}

const ParkingListItem = (props: Props) => {
  const { parking } = props;
  return (
    <Paper
      elevation={1}
      style={{
        backgroundColor: props.bcolor,
        margin: 0,
        width: 150,
        height: 200
      }}
    >
      <Typography align={'center'}>{`${parking.name}`}</Typography>
      <br />
      <Typography>{`${parking.address} ${parking.addressNr}`}</Typography>
      <br />
      <Typography>CHF {parking.price} / Tag</Typography>
    </Paper>
  );
};

export default ParkingListItem;
