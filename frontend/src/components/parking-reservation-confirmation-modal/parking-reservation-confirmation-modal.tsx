import {
  Box,
  Link,
  Button,
  Modal,
  Typography,
  Grid,
  TextField,
  Stack
} from '@mui/material';
import React from 'react';
import { makeStyles } from '@mui/styles';
import { DatePicker } from '@mui/x-date-pickers';
import { Icon } from '@iconify/react';

export const enum ParkplatzAction {
  RESERVIEREN = 'reservieren',
  STORNIEREN = 'stornieren'
}

const ParkingReservationConfirmationModal = ({
  bezeichnung,
  requestType,
  von,
  bis,
  makeReservation
}: {
  bezeichnung: string;
  requestType: ParkplatzAction;
  von: Date;
  bis: Date;
  makeReservation: (parkplatzAction: ParkplatzAction) => void;
}) => {
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const classes = useStyles();

  return (
    <div>
      <Link href="#" onClick={handleOpen}>
        Reservieren
      </Link>
      <Modal open={open} onClose={handleClose}>
        <Box sx={style}>
          <div className={classes.closeIconOnHeader}>
            <Icon
              onClick={() => setOpen(false)}
              className={classes.closeIcon}
              icon="ci:close-big"
            />
          </div>
          <Stack spacing={2}>
            <Typography align="center" variant="h4">
              {bezeichnung}
            </Typography>
            {/* <Typography align="center" variant="h5">
            {requestType}
          </Typography> */}

            <Grid container columnSpacing={2}>
              <Grid item xs={6}>
                <DatePicker
                  label="von"
                  onChange={() => null}
                  value={von}
                  disabled
                  renderInput={(props) => (
                    <TextField {...props} required={true} />
                  )}
                />
              </Grid>

              <Grid item xs={6}>
                <DatePicker
                  label="bis"
                  onChange={() => null}
                  value={bis}
                  disabled
                  renderInput={(props) => (
                    <TextField {...props} required={true} />
                  )}
                />
              </Grid>
            </Grid>

            <Box textAlign="center">
              <Button
                style={buttonStyle}
                onClick={() =>
                  makeReservation(
                    requestType === ParkplatzAction.RESERVIEREN
                      ? ParkplatzAction.RESERVIEREN
                      : ParkplatzAction.STORNIEREN
                  )
                }
              >
                jetzt {requestType}
              </Button>
            </Box>
          </Stack>
        </Box>
      </Modal>
    </div>
  );
};

const useStyles = makeStyles((theme) => ({
  tableRoot: {
    overflow: 'scroll'
  },
  tableBody: {
    display: 'inline-table',
    width: '100%',
    [theme.breakpoints.down('md')]: {
      display: 'grid-inline'
    }
  },
  tableRow: {
    [theme.breakpoints.down('md')]: {
      borderBottom: '0pt solid black'
    },
    [theme.breakpoints.down('sm')]: {
      display: 'grid',
      gap: '0',
      marginBottom: '5px'
    },
    gap: '20px'
  },
  tableCell: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    minWidth: '250px',
    maxWidth: '250px',
    [theme.breakpoints.down('sm')]: {
      minWidth: '300px',
      maxWidth: '300px'
    }
  },
  tableCellLabel: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    minWidth: '70px',
    maxWidth: '70px',
    [theme.breakpoints.down('sm')]: {
      minWidth: '100px',
      maxWidth: '100px'
    }
  },
  boxRoot: {
    position: 'absolute',
    display: 'block',
    borderRadius: '0%',
    textAlign: 'left',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    padding: '20px',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '80%',
    maxHeight: '90%',
    [theme.breakpoints.down('sm')]: {
      width: '90%',
      borderRadius: '0%'
    },
    backgroundColor: theme.palette.background.paper,
    border: '2px solid #000',
    boxShadow: '24',
    pt: '2',
    px: '4',
    pb: '3'
  },
  header: {
    display: 'flex',
    marginBottom: '20px',
    alignItems: 'center'
  },
  closeIconOnHeader: {
    right: '2%',
    float: 'right'
  },
  closeIcon: {
    height: '20px',
    width: '20px',
    '&:hover': {
      opacity: '50%'
    }
  }
}));

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4
};

const buttonStyle = {
  margin: '0px'
};

export default ParkingReservationConfirmationModal;
