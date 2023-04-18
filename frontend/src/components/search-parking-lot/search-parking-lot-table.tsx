import TableComponent from '../table/table-component';
import React from 'react';
import { RowDataType } from '../table/table-row';

interface SearchParkingLotTableProps {
  parkingLots: RowDataType[];
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

  return (
    <div>
      <TableComponent
        data={parkingLots}
        headerNames={headerNames}
        onRowClick={onRowClick}
      ></TableComponent>
    </div>
  );
};

export default SearchParkingLotTable;
