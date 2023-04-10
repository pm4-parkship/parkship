import { ParkingLotModel } from 'src/models';
import { makeStyles } from '@mui/styles';
import ParkingListItem from '../parking-list-item/parking-list-item';

const ParkingList = ({ parkings }: { parkings: ParkingLotModel[] }) => {
    const classes = useStyles();

    return (
    <div className={classes.parkingList}>
      {parkings.map((parking) => {
        return <ParkingListItem color={"red"} parking={parking} />;
      })}
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
