import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import React from 'react';
import { TableCell, Typography } from '@mui/material';

interface TableHeaderProps {
  headerNames: string[];
  onColumnClick: (name: string) => void;
}

const TableHeader = ({ headerNames, onColumnClick }: TableHeaderProps) => {
  return (
    <TableHead>
      <TableRow>
        {headerNames.map((value, index) => (
          <TableCell
            align={index > 0 ? 'right' : 'left'}
            component="th"
            scope="row"
            variant={'head'}
            key={`${value}-${index}`}
            onClick={() => onColumnClick(value)}
          >
            <Typography>{value}</Typography>
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
};
export default TableHeader;
