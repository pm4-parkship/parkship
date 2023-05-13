import { ParkingLotModel } from 'src/models';
import { makeStyles } from '@mui/styles';
import ParkingListItem from './parking-list-item';
import ParkingListEmptyItem from './parking-list-empty-item';
import Paper from '@mui/material/Paper';

const MyParkingLotList = ({ parkings , createNewParking}: { parkings: ParkingLotModel[] , createNewParking : () => void }) => {
  const classes = useStyles();

  const stringToAscii = (input: string): number => {
    let result = 0;
    for (let i = 0; i < input.length; i++) {
      result += input.codePointAt(i) || 0;
    }
    return Number(`0.${result}`);
  };

  const randomColor = (name: string): string => {
    const ascii = stringToAscii(name);
    const randomColor = Math.floor(ascii * 16777215).toString(16);
    return '#' + randomColor + 'b3';
  };

  return (
    <Paper className={classes.root}>
      <div className={classes.parkingList}>
        {parkings.map((parking) => {
          return (
            <ParkingListItem
              key={parking.id}
              bcolor={randomColor(parking.name)}
              parking={parking}
            />
          );
        })}
        <ParkingListEmptyItem add={createNewParking}/>
      </div>
    </Paper>
  );
};

const useStyles = makeStyles(() => ({
  root: {
    margin: '0'
  },
  parkingList: {
    display: 'flex',
    flexDirection: 'row',
    overflow: 'scroll',
    justifyContent: 'flex-start',
    columnGap: '2rem'
  }
}));

export default MyParkingLotList;
