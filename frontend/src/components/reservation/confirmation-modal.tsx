import {
  Box,
  Button,
  Grid,
  Modal,
  Stack,
  TextField,
  Typography
} from '@mui/material';
import React, { useState } from 'react';
import { makeStyles } from '@mui/styles';
import { Icon } from '@iconify/react';
import { formatDate } from '../../date/date-formatter';
import { DatePicker } from '@mui/x-date-pickers';

export const enum ReservationAction {
  CREATE = 'reservieren',
  CANCEL = 'stornieren',
  MODIFY = 'anpassen'
}

const ButtonText = new Map<ReservationAction, string>([
  [ReservationAction.CREATE, 'jetzt reservieren'],
  [ReservationAction.CANCEL, 'jetzt stornieren'],
  [ReservationAction.MODIFY, 'jetzt anpassen']
]);

const HeaderText = new Map<ReservationAction, string>([
  [ReservationAction.CREATE, 'Reservation bestätigen:'],
  [ReservationAction.CANCEL, 'Stornierung bestätigen'],
  [ReservationAction.MODIFY, 'Anpassung bestätigen']
]);

export interface ReservationConfirmationModalData {
  fromDate: Date;
  toDate: Date;
  id: string;
}

interface ParkingReservationConfirmationModalProps {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
  action: ReservationAction;
  data: ReservationConfirmationModalData;
  onConfirm: (data: ReservationConfirmationModalData) => void;
}

const ConfirmationModal = ({
  showModal = true,
  setShowModal,
  data,
  action,
  onConfirm
}: ParkingReservationConfirmationModalProps) => {
  const classes = useStyles();
  const [fromDate, setFromDate] = useState<Date>(data.fromDate);
  const [toDate, setToDate] = useState<Date>(data.toDate);

  const validateInput = (): boolean => {
    return fromDate <= toDate && fromDate >= new Date() && toDate >= new Date();
  };
  const DateRange = (date: Date, label: string, setter, minDate) => (
    <Grid item xs={12}>
      <Grid container flex={1} justifyContent="center" alignItems="center">
        <Grid item xs={3}>
          <Typography variant="body2" display="inline">
            {label}
          </Typography>
        </Grid>
        <Grid item>
          {action == ReservationAction.MODIFY ? (
            <DatePicker
              label={label}
              onChange={(newValue) => newValue && setter(newValue)}
              value={date}
              disablePast
              minDate={minDate}
              renderInput={(props) => <TextField {...props} required={true} />}
            />
          ) : (
            <Typography variant="body2" display="inline">
              {formatDate(date)}
            </Typography>
          )}
        </Grid>
      </Grid>
    </Grid>
  );

  return (
    <div>
      <Modal
        open={showModal}
        onClose={() => setShowModal(false)}
        disablePortal
        disableEnforceFocus
        disableAutoFocus
      >
        <Box className={classes.boxRoot}>
          <div className={classes.closeIconOnHeader}>
            <Icon
              onClick={() => setShowModal(false)}
              className={classes.closeIcon}
              icon="ci:close-big"
            />
          </div>
          <Stack spacing={3}>
            <Typography align="center" variant="h4">
              {data.id}
            </Typography>
            <Typography align="center">{HeaderText.get(action)}</Typography>
            <Grid container rowSpacing={4} flex={1} xs={12}>
              {DateRange(fromDate, 'von:', setFromDate, new Date())}
              {DateRange(toDate, 'bis:', setToDate, fromDate)}
            </Grid>

            <Box textAlign="center">
              <Button
                onClick={() =>
                  validateInput() &&
                  onConfirm({ toDate: toDate, fromDate: fromDate, id: data.id })
                }
                variant="contained"
              >
                {ButtonText.get(action)}
              </Button>
            </Box>
          </Stack>
        </Box>
      </Modal>
    </div>
  );
};
const useStyles = makeStyles((theme) => ({
  closeIconOnHeader: {
    display: 'flex',
    justifyContent: 'right'
  },
  closeIcon: {
    height: '20px',
    width: '20px',
    '&:hover': {
      opacity: '50%',
      cursor: 'pointer'
    }
  },
  boxRoot: {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    backgroundColor: theme.palette.background.default,
    border: '0px solid #000',
    borderRadius: '4px',
    padding: 16,
    display: 'flex',
    flexDirection: 'column',
    minWidth: '400px'
  }
}));

export default ConfirmationModal;
