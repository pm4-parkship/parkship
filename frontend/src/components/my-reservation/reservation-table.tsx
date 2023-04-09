import TableComponent from '../table/table-component';
import { logger } from '../../logger';

interface reservationTableProps {
  reservations: Array<string[]>;
}

const ReservationTable = ({ reservations }: reservationTableProps) => {
  const headerNames = [
    'ID',
    'Bezeichnung',
    'Ort',
    'Besitzer',
    'gebucht von bis',
    'storniert'
  ];
  const onCellClick = new Map([['stornieren', (e: any) => alert(e)]]);

  return (
    <TableComponent
      data={reservations}
      headerNames={headerNames}
      onCellClick={onCellClick}
      onRowClick={(e) => logger.log(e)}
    ></TableComponent>
  );
};

export default ReservationTable;
