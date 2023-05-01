import { makeStyles } from '@mui/styles';
import { Paper, Typography } from '@mui/material';

const ParkingListEmptyItem = ({add} : {add : () => void}) => {
  const { listItem } = useStyles();

  return (
    <Paper elevation={1} className={listItem} onClick={() => add}>
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
    width: 150,
    height: 200
  }
}));

export default ParkingListEmptyItem;
