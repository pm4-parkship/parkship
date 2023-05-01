import {
  Button,
  Divider,
  FormControlLabel,
  Grid,
  Modal,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableRow,
  Typography
} from '@mui/material';
import React from 'react';
import { makeStyles } from '@mui/styles';
import { ParkingLotModel } from '../../models';
import { Icon } from '@iconify/react';
import ImageCustom from '../image/image-custom';
import { nanoid } from 'nanoid';
import Checkbox from '@mui/material/Checkbox';

const ParkingDetailModal = ({
  showModal = true,
  setShowModal,
  parkingLotModel,
  createReservation
}: {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
  parkingLotModel: ParkingLotModel;
  createReservation: () => void;
}) => {
  const classes = useStyles();

  return (
    <>
      <Modal
        disablePortal
        disableEnforceFocus
        disableAutoFocus
        open={showModal}
        onClose={() => setShowModal(false)}
      >
        <div className={classes.boxRoot}>
          <div className={classes.header}>
            <Typography component={'span'} variant="h3">
              Parkplatz {parkingLotModel.nr}
            </Typography>
            <div className={classes.closeIconOnHeader}>
              <Icon
                onClick={() => setShowModal(false)}
                className={classes.closeIcon}
                icon="ci:close-big"
              />
            </div>
          </div>
          <Divider variant="middle" />
          <Grid container>
            <Grid item xs={12} sm={12} md={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCellLabel}>
                      <Typography component={'span'} variant="body1">
                        Besitzer:
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        {`${parkingLotModel.owner.name} ${parkingLotModel.owner.surname}`}
                      </Typography>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Grid>
            <Grid item xs={12} sm={12} md={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCellLabel}>
                      <Typography component={'span'} variant="body1">
                        Frei:
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        <FormControlLabel
                          label="Mo"
                          control={<Checkbox checked />}
                        />
                        <FormControlLabel
                          label="Di"
                          control={<Checkbox checked />}
                        />
                        <FormControlLabel
                          label="Mi"
                          control={<Checkbox checked />}
                        />
                        <FormControlLabel
                          label="Do"
                          control={<Checkbox checked />}
                        />
                        <FormControlLabel
                          label="Fr"
                          control={<Checkbox checked />}
                        />
                        <FormControlLabel
                          label="Sa"
                          control={<Checkbox checked />}
                        />
                        <FormControlLabel
                          label="So"
                          control={<Checkbox checked />}
                        />
                      </Typography>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Grid>

            <Grid item xs={12} sm={12} md={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCellLabel}>
                      <Typography component={'span'} variant="body1">
                        Kontakt:
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        +41 79 123 45 67
                      </Typography>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Grid>

            <Grid item xs={12} sm={12} md={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCellLabel}>
                      <Typography component={'span'} variant="body1">
                        Kosten:
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        5.- / Tag <Icon icon="ph:money-fill" />{' '}
                        <Icon icon="material-symbols:phone-android-rounded" />{' '}
                        <Icon icon="majesticons:creditcard" />
                      </Typography>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Grid>

            <Grid item xs={12} sm={12} md={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCellLabel}>
                      <Typography component={'span'} variant="body1">
                        {/* placeholder */}
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        mail@email.com
                      </Typography>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Grid>

            <Grid item xs={0} sm={0} md={6}></Grid>

            <Grid item xs={12} sm={12} md={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCellLabel}>
                      <Typography component={'span'} variant="body1">
                        Wo:
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        {parkingLotModel.address} {parkingLotModel.addressNr}
                      </Typography>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Grid>
          </Grid>
          <Divider variant="middle" />

          <Paper elevation={0}>{parkingLotModel.description}</Paper>

          <Divider variant="middle" />
          <Stack direction="row" spacing={2}>
            {parkingLotModel.pictures &&
              parkingLotModel.pictures.map((picture) => (
                <div>
                  <ImageCustom alt="images" src={picture} />
                </div>
              ))}
          </Stack>
          <Button
            className={classes.button}
            variant="outlined"
            onClick={createReservation}
          >
            reservieren
          </Button>
        </div>
      </Modal>
    </>
  );
};

const useStyles = makeStyles((theme) => ({
  button: {
    float: 'right'
  },
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
    overflow: 'scroll',
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
    position: 'absolute',
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

export default ParkingDetailModal;
