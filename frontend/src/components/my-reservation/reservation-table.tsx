import TableComponent from '../table/table-component';
import { logger } from '../../logger';
import { RowDataType } from '../table/table-row';

interface reservationTableProps {
  reservations: Array<RowDataType>;
}

const headerNames = [
  'ID',
  'Status',
  'Ort',
  'Besitzer',
  'gebucht von - bis',
  'storniert'
];

const ReservationTable = ({ reservations }: reservationTableProps) => {
  return (
    <TableComponent
      data={reservations}
      headerNames={headerNames}
      onRowClick={(e) => logger.log(e)}
    ></TableComponent>
  );
};

export default ReservationTable;
