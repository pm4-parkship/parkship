import { Paper, Typography } from "@mui/material";
import { ParkingLotModel } from "src/models";

const ParkingListItem = ({parking} : {parking : ParkingLotModel}) => {
  return (
    <Paper elevation={1}>
        <Typography>Parkplatz {parking.nr}</Typography>
        <br/>
        <Typography>{parking.floor}</Typography>
        <Typography>{parking.address}</Typography>
        <Typography>{parking.addressNr}</Typography>
        <Typography>CHF {parking.price} / Tag</Typography>
    </Paper>
  );
};

export default ParkingListItem;