import TableComponent from '../table/table-component';
import { logger } from '../../logger';
import { toast } from 'react-toastify';
import { useTheme } from '@mui/styles';

interface reservationTableProps {
  reservations: Array<string[]>;
}

const headerNames = [
  'ID',
  'Bezeichnung',
  'Ort',
  'Besitzer',
  'gebucht von bis',
  'storniert'
];

const ReservationTable = ({ reservations }: reservationTableProps) => {
  const { palette } = useTheme();
  const cancelReservation = (e: any) => {
    toast.success('🦄 Wow so easy!', {
      // theme: palette.mode
    });
  };

  const onCellClick = new Map([
    ['stornieren', (e: any) => cancelReservation(e)]
  ]);

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
