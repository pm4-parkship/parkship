import { makeStyles } from '@mui/styles';
import { Paper, Typography } from '@mui/material';

const ParkingListEmptyItem = () => {
  const { listItem } = useStyles();

  return (
    <Paper elevation={1} className={listItem}>
      <Typography>+</Typography>
    </Paper>
  );
};

const useStyles = makeStyles(() => ({
  listItem: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    textAlign: 'center',
    verticalAlign: 'middle',
    backgroundColor: 'grey',
    margin: 10,
    width: 130,
    height: 165
  }
}));

export default ParkingListEmptyItem;
