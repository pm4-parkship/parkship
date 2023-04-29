import { Box, Button, Grid, Modal, Stack, Typography } from '@mui/material';
import React from 'react';
import { makeStyles } from '@mui/styles';
import { Icon } from '@iconify/react';
import { ParkingLotModel } from '../../models';
import { logger } from '../../logger';
import useUser from '../../auth/use-user';
import { toast } from 'react-toastify';
import { ReservationModel } from 'src/models/reservation/reservation.model';

export const enum ParkingLotAction {
  RESERVIEREN = 'reservieren',
  STORNIEREN = 'stornieren'
}

const ParkingReservationConfirmationModal = ({
  showModal = true,
  setShowModal,
  parkingLot,
  requestType,
  from,
  to
}: {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
  parkingLot: ParkingLotModel;
  requestType: ParkingLotAction;
  from: string;
  to: string;
}) => {
  const { user } = useUser();
  const classes = useStyles();

  const executeReservation = (parkingLot: ParkingLotModel) => {
    const reservationData: ReservationModel = {
      parkingLot,
      tenant: {
        username: user?.username ?? '',
        email: 'test@test'
      },
      from: new Date(from),
      to: new Date(to)
    };

    if (user) {
      fetch('/backend/reservation', {
        method: 'POST',
        body: JSON.stringify(reservationData),
        headers: {
          Authorization: `Bearer ${user.token}`,
          'Content-Type': 'application/json'
        }
      })
        .then(async (response) => {
          const data = await response.json();
          logger.log(data);
          return data;
        })
        .catch((error) => logger.log(error));
    } else {
      logger.log('User not logged in');
      toast.error('User not logged in');
    }
  };

  const executeCancellation = (data: ParkingLotModel) => {
    if (user) {
      fetch('/backend/reservation' + data.id, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${user.token}`,
          'Content-Type': 'application/json'
        }
      }).then(async (response) => {
        const data = await response.json();
        logger.log(data);
        return data;
      });
    } else {
      logger.log('User not logged in');
      toast.error('User not logged in');
    }
  };

  return (
    <div>
      <Modal
        style={{ zIndex: 10000 }}
        open={showModal}
        onClose={() => setShowModal(false)}
      >
        <Box sx={style}>
          <div className={classes.closeIconOnHeader}>
            <Icon
              onClick={() => setShowModal(false)}
              className={classes.closeIcon}
              icon="ci:close-big"
            />
          </div>
          <Stack spacing={2}>
            <Typography align="center" variant="h4">
              {parkingLot.id}
            </Typography>
            {/* <Typography align="center" variant="h5">
            {requestType}
          </Typography> */}

            <Grid container columnSpacing={1}>
              <Grid item xs={2}>
                <Typography variant="subtitle2">von:</Typography>
              </Grid>

              <Grid item xs={4}>
                <Typography variant="highlighted">{from}</Typography>
              </Grid>

              <Grid item xs={2}>
                <Typography variant="subtitle2">bis:</Typography>
              </Grid>
              <Grid item xs={4}>
                <Typography variant="highlighted">{to}</Typography>
              </Grid>
            </Grid>

            <Box textAlign="center">
              <Button
                style={buttonStyle}
                onClick={() => {
                  if (requestType === ParkingLotAction.RESERVIEREN) {
                    executeReservation(parkingLot);
                  } else {
                    executeCancellation(parkingLot);
                  }
                }}
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
