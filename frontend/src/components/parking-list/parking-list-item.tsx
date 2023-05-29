import { Divider, Paper, Typography } from '@mui/material';
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
        padding: "1em",
        width: 150,
        height: 250,
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        fontSize: "0.9em"
      }}
    >
      <Typography align={'center'}>{`${parking.name}`}</Typography>
      <Divider/>
      <Typography fontSize={"14px"} align={'center'}>CHF {parking.price} / Tag</Typography>
    </Paper>
  );
};

export default ParkingListItem;
