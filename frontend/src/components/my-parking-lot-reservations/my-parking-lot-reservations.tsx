import TableComponent from '../table/table-component';
import { RowDataType } from '../table/table-row';

interface MyParkinLotReservationTableProps {
  reservations: Array<RowDataType>;
  styles?: Map<number, string>;
}

const headerNames = [
  'ID',
  'Status',
  'Parkplatzname',
  'Ort',
  'reserviert von',
  'gebucht von - bis'
];
const MyParkingLotReservationTable = ({
  reservations,
  styles
}: MyParkinLotReservationTableProps) => {
  return (
    <TableComponent
      data={reservations}
      headerNames={headerNames}
      styles={styles}
      paginationLabel={'Reservationen pro Seite'}
    ></TableComponent>
    
  );
};

export default MyParkingLotReservationTable;
