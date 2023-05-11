
import TableContainer from '@mui/material/TableContainer';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableHeader from '../../../src/components/table/table-header';
import TableBody from '@mui/material/TableBody';
import { makeStyles } from '@mui/styles';
import { TableCell, TableRow } from '@mui/material';
import { nanoid } from 'nanoid';
import { red } from '@mui/material/colors';
import { MyParkingLotsTableProps } from 'pages/my-parking-lot';

interface MyParkinLotReservationTableProps {
  headerNames: string[];
  reservations: MyParkingLotsTableProps[]
}

const MyParkinLotReservationTable = ({headerNames, reservations} : MyParkinLotReservationTableProps) => {
  const classes = useStyles();

  return (
    <>
      <TableContainer component={Paper} className={classes.baseTableContainer}>
        <Table sx={{ minWidth: 650 }}>
          <TableHeader headerNames={headerNames} />

          <TableBody>
            {reservations.map((row, index) => {
              return (
                <TableRow
                  key={row.id}
                  className={row.active ? classes.baseRow : classes.oldRow}
                >
                  <TableCell align={'left'} variant={'body'} key={nanoid()}>
                    {row.id}
                  </TableCell>

                  <TableCell align={'right'} variant={'body'} key={nanoid()}>
                    {row.parkingLotName}
                  </TableCell>

                  <TableCell align={'right'} variant={'body'} key={nanoid()}>
                    {row.location}
                  </TableCell>

                  <TableCell align={'right'} variant={'body'} key={nanoid()}>
                    {row.reservedBy}
                  </TableCell>

                  <TableCell align={'right'} variant={'body'} key={nanoid()}>
                    {row.bookingTime}
                  </TableCell>

                  <TableCell align={'right'} variant={'body'} key={nanoid()}>
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
  baseTableContainer: {
    marginTop: 10,
  },
  baseRow: {
    width: '100%'
  },
  oldRow: {
    backgroundColor: theme.palette.primary.main,
    '&:hover': {
      backgroundColor: theme.palette.primary.main
    }
  }
}));

export default MyParkinLotReservationTable;
