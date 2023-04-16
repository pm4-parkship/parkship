import { ParkingLotModel } from 'src/models';
import { makeStyles } from '@mui/styles';
import ParkingListItem from '../parking-list-item/parking-list-item';
import ParkingListEmptyItem from '../parking-list-empty-item/parking-list-empty-item';

const ParkingList = ({ parkings }: { parkings: ParkingLotModel[] }) => {
    const classes = useStyles();

    const randomColors : string[] = ["red", "blue", "yellow", "green", "purple"];
    const randomColor = (index : number) : string => {
      // TODO : update with random color
      return randomColors[index];
    };

    return (
    <div className={classes.parkingList}>
      {parkings.map((parking, index) => {
        return <ParkingListItem bcolor={randomColor(index)} parking={parking} />;
      })}
      {(parkings?.length == 0) ? <ParkingListEmptyItem /> : <></>}
    </div>
  );
};

const useStyles = makeStyles((theme) => ({
    parkingList: {
        display: "flex",
        flexDirection: "row",
        overflow: "scroll"   
       }
}));

export default ParkingList;
