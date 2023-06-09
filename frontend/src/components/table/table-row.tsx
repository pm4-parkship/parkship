import TableCell from '@mui/material/TableCell';
import TableRow from '@mui/material/TableRow';
import React from 'react';
import { Typography } from '@mui/material';

export type RowDataType = Array<string | JSX.Element>;

interface CustomTableRowProps {
  rowKey: number;
  onRowClick?: (row: RowDataType) => void;
  data: RowDataType;
  className?: string;
}

const CustomTableRow = ({
  rowKey,
  data,
  onRowClick,
  className
}: CustomTableRowProps) => {
  return (
    <TableRow
      key={rowKey}
      onClick={() => {
        onRowClick && onRowClick(data);
      }}
      className={className}
    >
      {data.map((cell, index) => (
        <TableCell
          align={index > 0 ? 'right' : 'left'}
          variant={'body'}
          key={`${cell}-${index}`}
        >
          {typeof cell != 'string' ? (
            <>{cell}</>
          ) : (
            <Typography variant={'body2'}>{cell}</Typography>
          )}
        </TableCell>
      ))}
    </TableRow>
  );
};

export default CustomTableRow;
