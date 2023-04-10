import { Color, Paper, Typography } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { ParkingLotModel } from 'src/models';

const ParkingListItem = ({ color, parking }: {color: string, parking: ParkingLotModel }) => {
    const classes = useStyles();
 
    return (
    <Paper elevation={1} className={classes.listItem} sx={{
        bgcolor: {color}
      }}>
      <Typography>Parkplatz {parking.nr}</Typography>
      <br />
      <Typography>{parking.floor}</Typography>
      <Typography>{parking.address}</Typography>
      <Typography>{parking.addressNr}</Typography>
      <Typography>CHF {parking.price} / Tag</Typography>
    </Paper>
  );
};

const useStyles = makeStyles((theme) => ({
  listItem: {
    // todo add styling here.

  }
}));

export default ParkingListItem;
