import TableCell from '@mui/material/TableCell';
import TableRow from '@mui/material/TableRow';
import Link from '@mui/material/Link';
import React from 'react';
import { Typography } from '@mui/material';

interface CustomTableRowProps {
  rowKey: number;
  onCellClick: Map<string, (args: any) => void>;
  onRowClick: (row: any) => void;
  data: string[];
}

const CustomTableRow = ({
  rowKey,
  data,
  onRowClick,
  onCellClick
}: CustomTableRowProps) => {
  const handleLink = (
    event: React.MouseEvent<HTMLElement>,
    cell: string,
    rowData: any
  ) => {
    const cellFunction = onCellClick.get(cell);
    if (cellFunction) {
      cellFunction.bind(rowData);
      cellFunction(data);
      event.stopPropagation();
    }
  };

  return (
    <TableRow key={rowKey} onClick={() => onRowClick(data)}>
      {data.map((cell, index) => (
        <TableCell
          align={index > 0 ? 'right' : 'left'}
          variant={'body'}
          key={`${cell}-${index}`}
        >
          {onCellClick.has(cell) ? (
            <Link href="#" onClick={(event) => handleLink(event, cell, data)}>
              <Typography variant={'body2'}>{cell}</Typography>
            </Link>
          ) : (
            <Typography variant={'body2'}>{cell}</Typography>
          )}
        </TableCell>
      ))}
    </TableRow>
  );
};

export default CustomTableRow;
