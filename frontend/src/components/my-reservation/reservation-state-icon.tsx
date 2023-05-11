import {
  ReservationState,
  ReservationStateLabel
} from '../../models/reservation/reservation.model';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import { Tooltip } from '@mui/material';

const ReservationStateIcon = (state: ReservationState) => {
  return (
    <Tooltip title={ReservationStateLabel.get(state)}>
      {ReservationState[state] == ReservationState.ACTIVE ? (
        <CheckCircleIcon color={'success'} />
      ) : (
        <CancelIcon color={'error'} />
      )}
    </Tooltip>
  );
};
export default ReservationStateIcon;
