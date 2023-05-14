import React from 'react';
import TableComponent from '../table/table-component';
import { RowDataType } from '../table/table-row';

const headerNames = ['ID', 'Bezeichnung', 'Ort', 'Besitzer', 'Status'];

const ParkingLotsTable = ({
  parkingLots
}: {
  parkingLots: Array<RowDataType>;
}) => {
  return (
    <TableComponent
      data={parkingLots}
      headerNames={headerNames}
      paginationLabel={'Parklätze pro Seite'}
    ></TableComponent>
  );
};

export default ParkingLotsTable;
