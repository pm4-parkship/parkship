import React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { format } from 'date-fns';
import Link from '@mui/material/Link';
import { styled } from '@mui/styles';

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.primary.main,
    color: theme.palette.common.white
  },
  [`&.${tableCellClasses.body}`]: {
     fontSize: 14
  }
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover
  },
  // hide last border
  '&:last-child td, &:last-child th': {
    border: 0
  }
}));

// TODO: dummy input data, will be replaced by the incoming data from backend
function createData(
  bezeichnung: string,
  ort: string,
  besitzer_in: string,
  startDatum: Date,
  endDatum: Date
) {
  return { bezeichnung, ort, besitzer_in, startDatum, endDatum };
}

// TODO: dummy input data, to be replaced by the incoming data from backend
const rows = [
  createData(
    'Parkplatz A1',
    'in der Ecke',
    'Toni Balloni',
    new Date(),
    new Date()
  ),
  createData(
    'Parkplatz A2',
    'im Schnee',
    'Eisbär Knut',
    new Date(),
    new Date()
  ),
  createData(
    'Parkplatz A3',
    'im Büro',
    'Stanley Parabel',
    new Date(),
    new Date()
  )
];

function getFormattedAvailabilitySpan(startDatum: Date, endDatum: Date) {
  return (
    format(startDatum, 'dd.MM.yyyy') + ' - ' + format(endDatum, 'dd.MM.yyyy')
  );
}

const TableComponent = () => {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 700 }} aria-label="customized table">
        <TableHead>
          <TableRow>
            <StyledTableCell align="center">Bezeichnung</StyledTableCell>
            <StyledTableCell align="center">Ort</StyledTableCell>
            <StyledTableCell align="center">Besitzer_in</StyledTableCell>
            <StyledTableCell align="center">Verfügbarkeit</StyledTableCell>
            <StyledTableCell align="center"></StyledTableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <StyledTableRow key={row.bezeichnung}>
              <StyledTableCell component="th" scope="row" align="center">
                {row.bezeichnung}
              </StyledTableCell>
              <StyledTableCell align="center">{row.ort}</StyledTableCell>
              <StyledTableCell align="center">
                {row.besitzer_in}
              </StyledTableCell>
              <StyledTableCell align="center">
                {getFormattedAvailabilitySpan(row.startDatum, row.endDatum)}
              </StyledTableCell>
              <StyledTableCell align="center">
                <Link
                  href="#"
                  onClick={() => {
                    //Todo Reservation
                  }}
                >
                  Reservieren
                </Link>
              </StyledTableCell>
            </StyledTableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default TableComponent;
