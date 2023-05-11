import { User } from 'pages/api/user';
import React, { useEffect, useState } from 'react';
import { logger } from 'src/logger';
import { RowDataType } from 'src/components/table/table-row';
import { formatDate } from 'src/date/date-formatter';
import { ParkingLotReservationModel } from 'src/models/parking-lot-reservations/parking-lot-reservations.model';
import TableContainer from '@mui/material/TableContainer';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableHeader from '../../../src/components/table/table-header';
import TableBody from '@mui/material/TableBody';
import { TableCell, TableRow } from '@mui/material';
import { makeStyles } from '@mui/styles';

const fetchMyParkingLotReservations = async (
  user: User
): Promise<ParkingLotReservationModel> => {
  return await fetch('/backend/parking-lot/reservations', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      return data;
    }
  });
};

const headerNames = [
  'ID',
  'Parkplatzname',
  'Ort',
  'reserviert von',
  'gebucht von - bis',
  'Status'
];

interface myParkingSlotsTableProps {
  id: number;
  parkingLotName: string;
  location: string;
  reservedBy: string;
  bookingTime: string;
  status: string;
  active: boolean;
}

const MyParkingLotsPage = ({ user }) => {
  const classes = useStyles();
  const [parkingSlotsDataPast, setParkingSlotsDataPast] = useState<
    myParkingSlotsTableProps[]
  >([]);

  useEffect(() => {
    fetchMyParkingLotReservations(user)
      .then((result: ParkingLotReservationModel) => {
        logger.log('result: ', result);

        const parkingSlotsDataPast = result.past.map((item) => {
          return {
            id: item.id,
            parkingLotName: item.parkingLot.name,
            location: `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
            reservedBy: item.tenant.name,
            bookingTime: `${formatDate(new Date(item.from))} - ${formatDate(
              new Date(item.to)
            )}`,
            status: item.reservationState,
            active: false
          };
        });

        const parkingSlotsDataFuture = result.current.map((item) => {
          return {
            id: item.id,
            parkingLotName: item.parkingLot.name,
            location: `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
            reservedBy: item.tenant.name,
            bookingTime: `${formatDate(new Date(item.from))} - ${formatDate(
              new Date(item.to)
            )}`,
            status: item.reservationState,
            active: true
          };
        });

        setParkingSlotsDataPast(
          parkingSlotsDataPast.concat(parkingSlotsDataFuture)
        );

        logger.log(parkingSlotsDataPast);
      })
      .catch((error) => logger.log(error));
  }, []);

  return (
    <>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }}>
          <TableHeader headerNames={headerNames} />

          <TableBody>
            {parkingSlotsDataPast.map((row, index) => {
              return (
                <TableRow
                  key={row.id}
                  className={!row.active ? classes.baseRow : classes.activeRow}
                >
                  <TableCell
                    align={'left'}
                    variant={'body'}
                    key={index}
                  >
                    {row.id}
                  </TableCell>

                  <TableCell
                    align={'right'}
                    variant={'body'}
                    key={index}
                  >
                    {row.parkingLotName}
                  </TableCell>

                  <TableCell
                    align={'right'}
                    variant={'body'}
                    key={index}
                  >
                    {row.location}
                  </TableCell>

                  <TableCell
                    align={'right'}
                    variant={'body'}
                    key={index}
                  >
                    {row.reservedBy}
                  </TableCell>

                  <TableCell
                    align={'right'}
                    variant={'body'}
                    key={index}
                  >
                    {row.bookingTime}
                  </TableCell>

                  <TableCell
                    align={'right'}
                    variant={'body'}
                    key={index}
                  >
                    {row.status}
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

const useStyles = makeStyles((theme) => ({
  baseRow: {
    width: '100%'
  },
  activeRow: {
    backgroundColor: theme.palette.primary.main,
    '&:hover': {
      backgroundColor: theme.palette.primary.main
    }
  }
}));

export default MyParkingLotsPage;
