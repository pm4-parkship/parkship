import {
  ReservationState,
  ReservationStateToString
} from '../../models/reservation/reservation.model';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import { Tooltip } from '@mui/material';

const ReservationStateIcon = (state: ReservationState) => {
  return (
    <Tooltip title={ReservationStateToString(state)}>
      {ReservationState[state] == ReservationState.ACTIVE ? (
        <CheckCircleIcon color={'success'} />
      ) : (
        <CancelIcon color={'error'} />
      )}
    </Tooltip>
  );
};
export default ReservationStateIcon;
