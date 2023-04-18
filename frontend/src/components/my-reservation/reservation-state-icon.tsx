import { ReservationState } from '../../models/reservation/reservation.model';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import { Tooltip } from '@mui/material';

const ReservationStateIcon = (state: ReservationState) => {
  return (
    <Tooltip title={state.toString()}>
      {state == ReservationState.OK ? (
        <CheckCircleIcon color={'success'} />
      ) : (
        <CancelIcon color={'error'} />
      )}
    </Tooltip>
  );
};
export default ReservationStateIcon;
