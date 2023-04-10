import { ParkingLotModel } from 'src/models';
import ParkingListItem from '../parking-list-item/parking-list-item';

const ParkingList = ({ parkings }: { parkings: ParkingLotModel[] }) => {
  return (
    <div>
      {parkings.map((parking) => {
        return <ParkingListItem parking={parking} />;
      })}
    </div>
  );
};

export default ParkingList;
