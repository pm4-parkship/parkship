import { ReservationModel } from '../../models/reservation/reservation.model';
import { formatDate } from '../../date/date-formatter';
import { Link, Typography } from '@mui/material';
import React from 'react';
import { makeStyles } from "@mui/styles";

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
  const classes = useStyles();

  if (reservation.cancelDate) {
    return `${formatDate(new Date(reservation.cancelDate))}`;
  } else if (new Date(reservation.from) <= minCancelDate()) {
    return <></>;
  }
  return (
    <div className={classes.bookLink}
      onClick={(e) => {
        e.stopPropagation();
        onClick();
      }}
    >
      <Typography variant={'body2'}>{'stornieren'}</Typography>
    </div>
  );
};
const useStyles = makeStyles((theme) => ({
  bookLink: {
    color: theme.palette.primary.main,
    textDecorationStyle:'solid',
    '&:hover': {
      cursor: 'pointer',
      textDecorationLine:'underline',
    }
  }
}))
export default CancelCell;
