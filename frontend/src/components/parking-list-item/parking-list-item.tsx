import { Color, Paper, Typography } from '@mui/material';
import { DefaultTheme, makeStyles } from '@mui/styles';
import { ParkingLotModel } from 'src/models';

interface Props {
  bcolor: string,
  parking: ParkingLotModel
}

const ParkingListItem = (props : Props) => {
    const {parking} = props;
    const { listItem } = useStyles(props)();
 
    return (
    <Paper elevation={1} className={listItem}>
      <Typography>Parkplatz {parking.nr}</Typography>
      <br />
      <Typography>{parking.floor}</Typography>
      <Typography>{parking.address}</Typography>
      <Typography>{parking.addressNr}</Typography>
      <Typography>CHF {parking.price} / Tag</Typography>
    </Paper>
  );
};


const useStyles = (props : Props) => makeStyles( theme => ({
  listItem: {
    backgroundColor: props.bcolor,
    margin: 10
  }
}));


export default ParkingListItem;
