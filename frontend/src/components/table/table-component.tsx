import React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import CustomTableRow, { RowDataType } from './table-row';
import Paper from '@mui/material/Paper';
import TableHeader from './table-header';

interface TableComponentProps {
  headerNames: string[];
  onRowClick?: (row: unknown) => void;
  data: RowDataType[];
  className?: string;
}

const TableComponent = ({
  headerNames,
  onRowClick,
  data,
  className,
}: TableComponentProps) => {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }}>
        <TableHeader headerNames={headerNames} />

        <TableBody>
          {data.map((row, index) => (
            <CustomTableRow
              key={index}
              data={row}
              rowKey={index}
              onRowClick={onRowClick}
            />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default TableComponent;
