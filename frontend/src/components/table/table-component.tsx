import React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import CustomTableRow, { RowDataType } from './table-row';
import Paper from '@mui/material/Paper';
import TableHeader from './table-header';
import { TableFooter, TablePagination } from '@mui/material';
import TableRow from '@mui/material/TableRow';
import TablePaginationActions from './table-pagination';
import TableCell from '@mui/material/TableCell';

interface TableComponentProps {
  headerNames: string[];
  onRowClick?: (row: RowDataType) => void;
  data: RowDataType[];
  styles?: Map<number, string>;
}

const TableComponent = ({
  headerNames,
  onRowClick,
  data,
  styles
}: TableComponentProps) => {
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  // Avoid a layout jump when reaching the last page with empty rows.
  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - data.length) : 0;

  const handleChangePage = (
    event: React.MouseEvent<HTMLButtonElement> | null,
    newPage: number
  ) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }}>
        <TableHeader headerNames={headerNames} />
        <TableBody>
          {(rowsPerPage > 0
            ? data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            : data
          ).map((row, index) => (
            <CustomTableRow
              key={index}
              data={row}
              rowKey={index}
              onRowClick={onRowClick}
              className={styles && styles.get(index)}
            />
          ))}
          {emptyRows > 0 && (
            <TableRow style={{ height: 62 * emptyRows }}>
              <TableCell colSpan={6} />
            </TableRow>
          )}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25, { label: 'Alle', value: -1 }]}
              colSpan={headerNames.length}
              count={data.length}
              rowsPerPage={rowsPerPage}
              page={page}
              SelectProps={{
                inputProps: {
                  'aria-label': 'Reservationen pro Seite'
                },
                native: true
              }}
              labelRowsPerPage={'Reservationen pro Seite'}
              onPageChange={handleChangePage}
              onRowsPerPageChange={handleChangeRowsPerPage}
              ActionsComponent={TablePaginationActions}
            />
          </TableRow>
        </TableFooter>
      </Table>
    </TableContainer>
  );
};

export default TableComponent;
