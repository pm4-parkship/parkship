import { ParkingLotModel } from 'src/models';
import { makeStyles } from '@mui/styles';
import ParkingListItem from './parking-list-item';
import ParkingListEmptyItem from './parking-list-empty-item';

const MyParkingLotList = ({ parkings }: { parkings: ParkingLotModel[] }) => {
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
      {parkings?.length == 0 ? <ParkingListEmptyItem /> : <></>}
    </div>
  );
};

const useStyles = makeStyles(() => ({
  parkingList: {
    display: 'flex',
    flexDirection: 'row',
    overflow: 'scroll',
    justifyContent: 'flex-start',
    columnGap: '2rem'
  }
}));

export default MyParkingLotList;