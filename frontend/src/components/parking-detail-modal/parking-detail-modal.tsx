import {
  Divider,
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
import { logger } from '../../logger';
import { Icon } from '@iconify/react';
import ImageCustom from '../image/image-custom';
import { nanoid } from 'nanoid';

const ParkingDetailModal = ({
  showModal = true,
  setShowModal,
  parkingLotModel
}: {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
  parkingLotModel: ParkingLotModel;
}) => {
  const classes = useStyles();

  logger.error(parkingLotModel, 'parkingLotModel');

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
            <Grid item xs={12} sm={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        Besitzer:
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        {parkingLotModel.owner}
                      </Typography>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        Frei:
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        {parkingLotModel.description}
                      </Typography>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Grid>

            <Grid item xs={12} sm={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCell}>
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

            <Grid item xs={12} sm={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCell}>
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

            <Grid item xs={12} sm={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        {/* placeholder */}
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        example@email.com
                      </Typography>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </Grid>

            <Grid item xs={0} sm={6}></Grid>

            <Grid item xs={12} sm={6}>
              <Table className={classes.tableRoot}>
                <TableBody className={classes.tableBody}>
                  <TableRow className={classes.tableRow} key={nanoid()}>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        Wo:
                      </Typography>
                    </TableCell>
                    <TableCell className={classes.tableCell}>
                      <Typography component={'span'} variant="body1">
                        {parkingLotModel.address}
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
            {parkingLotModel.pictures.map((picture) => (
              <div>
                <ImageCustom alt="images" src={picture} />
              </div>
            ))}
          </Stack>
        </div>
      </Modal>
    </>
  );
};

const useStyles = makeStyles((theme) => ({
  tableRoot: {
    display: 'block',
    overflow: 'scroll',
    maxWidth: '100%'
  },
  tableBody: {
    display: 'inline-table',
    width: '100%',
    [theme.breakpoints.down('md')]: {
      display: 'inline-grid'
    }
  },
  tableRow: {
    [theme.breakpoints.down('md')]: {
      borderBottom: '0pt solid black'
    },
    alignItems: 'left',
    justifyContent: 'space-evenly',
    display: 'flex'
  },
  tableCell: {
    align: 'left'
  },
  typoText: {
    align: 'left'
  },
  boxRoot: {
    position: 'absolute',
    display: 'block',
    borderRadius: '5%',
    textAlign: 'left',
    overflow: 'scroll',
    padding: '20px',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '650px',
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
    justifyContent: 'space-around',
    alignItems: 'center'
  },
  closeIconOnHeader: {
    position: 'absolute',
    right: '5%',
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
