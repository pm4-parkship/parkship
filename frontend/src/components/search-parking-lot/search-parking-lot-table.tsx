import { logger } from '../../logger';
import TableComponent from '../table/table-component';
import React from 'react';

interface SearchParkingLotTableProps {
  parkingLots: Array<string[]>;
  onRowClick: (data: string[]) => void;
}

const SearchParkingLotTable = ({
  parkingLots,
  onRowClick
}: SearchParkingLotTableProps) => {
  const headerNames = [
    'Bezeichnung',
    'Ort',
    'Besitzer',
    'verfügbar',
    'reservieren'
  ];
  const onCellClick = new Map([
    ['reservieren', (e: any) => logger.log('book' + e)]
  ]);

  return (
    <div>
      <TableComponent
        data={parkingLots}
        headerNames={headerNames}
        onCellClick={onCellClick}
        onRowClick={onRowClick}
      ></TableComponent>
    </div>
  );
};

export default SearchParkingLotTable;
