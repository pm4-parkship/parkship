import { Color, Paper, Typography } from '@mui/material';
import { lightBlue } from '@mui/material/colors';
import { DefaultTheme, makeStyles } from '@mui/styles';
import { ParkingLotModel } from 'src/models';

const ParkingListItem = ({ bcolor, parking}: {bcolor: string, parking: ParkingLotModel }) => {
    const classes = useStyles();
 
    return (
    <Paper elevation={1} className={classes.listItem} 
    // sx={{
    //     bgcolor: "red",
    //     m: 2
    //   }}
      >
      <Typography>Parkplatz {parking.nr}</Typography>
      <br />
      <Typography>{parking.floor}</Typography>
      <Typography>{parking.address}</Typography>
      <Typography>{parking.addressNr}</Typography>
      <Typography>CHF {parking.price} / Tag</Typography>
    </Paper>
  );
};

const useStyles = makeStyles((theme : DefaultTheme) => ({
  listItem: {
    // todo add styling here.

    backgroundColor: theme.palette.primary.light,
    margin: 10
  }
}));

export default ParkingListItem;
