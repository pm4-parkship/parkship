import React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import CustomTableRow from './table-row';
import Paper from '@mui/material/Paper';
import TableHeader from './table-header';

interface TableComponentProps {
  headerNames: string[];
  onRowClick: (row: any) => void;
  onCellClick: Map<string, (cell: any) => void>;
  data: Array<string[]>;
}

const TableComponent = ({
  headerNames,
  onRowClick,
  onCellClick,
  data
}: TableComponentProps) => {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHeader headerNames={headerNames} />

        <TableBody>
          {data.map((row, index) => (
            <CustomTableRow
              key={index}
              data={row}
              rowKey={index}
              onCellClick={onCellClick}
              onRowClick={onRowClick}
            />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default TableComponent;
