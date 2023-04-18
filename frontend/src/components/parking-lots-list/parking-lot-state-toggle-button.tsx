import { ParkingLotModel, ParkingLotState } from '../../models';
import { ToggleButton, ToggleButtonGroup, Typography } from '@mui/material';
import React from 'react';

interface parkingLotStateToggleButtonProps {
  parkingLot: ParkingLotModel;
  changeParkingLotState: (item: ParkingLotModel) => void;
}

const ParkingLotStateToggleButton = ({
  parkingLot,
  changeParkingLotState
}: parkingLotStateToggleButtonProps): JSX.Element => {
  return (
    <ToggleButtonGroup
      value={parkingLot.state}
      exclusive
      onChange={() => changeParkingLotState(parkingLot)}
    >
      <ToggleButton
        value={ParkingLotState.released}
        color={'success'}
        disabled={parkingLot.state == ParkingLotState.released}
        style={{ minWidth: 150 }}
      >
        {releasedText(parkingLot.state)}
      </ToggleButton>
      <ToggleButton
        value={ParkingLotState.locked}
        color={'error'}
        disabled={parkingLot.state == ParkingLotState.locked}
        style={{ minWidth: 150 }}
      >
        {lockedText(parkingLot.state)}
      </ToggleButton>
    </ToggleButtonGroup>
  );
};

const lockedText = (state: ParkingLotState) =>
  state === ParkingLotState.locked ? (
    <Typography>gesperrt</Typography>
  ) : (
    <Typography>sperren</Typography>
  );
const releasedText = (state: ParkingLotState) =>
  state === ParkingLotState.released ? (
    <Typography>freigegeben</Typography>
  ) : (
    <Typography>freigeben</Typography>
  );
export default ParkingLotStateToggleButton;
