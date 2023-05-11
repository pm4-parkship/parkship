import TableComponent from '../table/table-component';
import { RowDataType } from '../table/table-row';

interface reservationTableProps {
  reservations: Array<RowDataType>;
  openModifyModal: (row: RowDataType) => void;
}

const headerNames = [
  'ID',
  'Status',
  'Ort',
  'Besitzer',
  'gebucht von - bis',
  'storniert'
];

const ReservationTable = ({
  reservations,
  openModifyModal
}: reservationTableProps) => {
  return (
    <TableComponent
      data={reservations}
      headerNames={headerNames}
      onRowClick={openModifyModal}
    ></TableComponent>
  );
};

export default ReservationTable;
