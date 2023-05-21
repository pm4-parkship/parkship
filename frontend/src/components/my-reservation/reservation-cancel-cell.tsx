import { ReservationModel } from '../../models/reservation/reservation.model';
import { formatDate } from '../../date/date-formatter';
import { Link, Typography } from '@mui/material';
import React from 'react';

const minCancelDate = () => {
  const today = new Date();
  today.setDate(today.getDate() + 2);
  return today;
};

interface CancelCellProps {
  reservation: ReservationModel;
  onClick: () => void;
}

const CancelCell = ({
  reservation,
  onClick
}: CancelCellProps): string | JSX.Element => {
  if (reservation.cancelDate) {
    return `${formatDate(new Date(reservation.cancelDate))}`;
  } else if (new Date(reservation.from) <= minCancelDate()) {
    return <span></span>;
  }
  return (
    <Link
      href="#"
      onClick={(e) => {
        e.stopPropagation();
        onClick();
      }}
    >
      <Typography variant={'body2'}>{'stornieren'}</Typography>
    </Link>
  );
};
export default CancelCell;
