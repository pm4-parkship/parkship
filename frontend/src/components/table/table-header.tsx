import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import React from 'react';
import { TableCell, Typography } from '@mui/material';

interface TableHeaderProps {
  headerNames: string[];
}

const TableHeader = ({ headerNames }: TableHeaderProps) => {
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
          >
            <Typography>{value}</Typography>
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
};
export default TableHeader;
