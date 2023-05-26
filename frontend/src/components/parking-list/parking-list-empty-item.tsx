import { makeStyles } from '@mui/styles';
import { Paper } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';

const ParkingListEmptyItem = () => {
  const { listItem } = useStyles();

  return (
    <Paper elevation={1} className={listItem} >
      <AddIcon fontSize={'large'} />
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
    backgroundColor: 'lightgray',
    width: 150,
    height: 200,
    margin: 0,
    '&:hover': {
      opacity: '75%',
      cursor: 'pointer'
    }
  }
}));

export default ParkingListEmptyItem;
