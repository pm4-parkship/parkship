import { ParkingLotModel } from "src/models";

const ParkingListItem = ({parking} : {parking : ParkingLotModel}) => {
  return (
    <div>{parking.description}</div>
  );
};

export default ParkingListItem;