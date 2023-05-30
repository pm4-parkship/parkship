import TableComponent, { SortOrder } from '../table/table-component';
import { RowDataType } from '../table/table-row';
import ReservationStateIcon from '../my-reservation/reservation-state-icon';
import { makeStyles } from '@mui/styles';
import {
  ReservationModel,
  ReservationState
} from '../../models/reservation/reservation.model';
import { formatDate } from '../../date/date-formatter';
import { startOfToday } from 'date-fns';
import { useEffect, useState } from 'react';

interface MyParkingLotReservationTableProps {
  reservations: ReservationModel[];
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
  reservations
}: MyParkingLotReservationTableProps) => {
  const [sortFunction, setSortFunction] =
    useState<(a: ReservationModel, b: ReservationModel) => number>();

  const [sortOrder, setSortOrder] = useState<number>(SortOrder.desc);

  const classes = useStyles();
  const rowStyleMap = new Map<number, string>();

  const changeSorting = (colName: string) => {
    setSortOrder(() =>
      sortOrder == SortOrder.desc ? SortOrder.asc : SortOrder.desc
    );
    let fun;
    switch (colName) {
      case 'ID':
        fun = (a: ReservationModel, b: ReservationModel): number =>
          (a.id > b.id ? 1 : -1) * sortOrder;
        break;
      case 'Status':
        fun = (a: ReservationModel, b: ReservationModel): number =>
          (a.reservationState > b.reservationState ? 1 : -1) * sortOrder;
        break;
      case 'Parkplatzname':
        fun = (a: ReservationModel, b: ReservationModel): number =>
          (a.parkingLot.name > b.parkingLot.name ? 1 : -1) * sortOrder;
        break;
      case 'Ort':
        fun = (a: ReservationModel, b: ReservationModel): number =>
          (a.parkingLot.address > b.parkingLot.address ? 1 : -1) * sortOrder;
        break;
      case 'reserviert von':
        fun = (a: ReservationModel, b: ReservationModel): number =>
          (a.tenant.name > b.tenant.name ? 1 : -1) * sortOrder;
        break;
      case 'gebucht von - bis':
        fun = (a: ReservationModel, b: ReservationModel): number => {
          const dateA = new Date(a.from);
          const dateB = new Date(b.from);
          if (dateA > dateB) {
            return 1 * sortOrder;
          } else if (dateA < dateB) {
            return -1 * sortOrder;
          } else {
            return (dateA.getTime() - dateB.getTime()) * sortOrder;
          }
        };
        break;
      default:
    }
    setSortFunction(() => fun);
  };
  useEffect(() => {
    changeSorting('gebucht von - bis');
  }, []);

  const mappedReservations: Array<RowDataType> = reservations
    .sort(sortFunction)
    .map((item, index) => {
      rowStyleMap.set(
        index,
        new Date(item.to).getDate() <= startOfToday().getDate()
          ? classes.pastReservation
          : ''
      );

      return [
        `${item.id}`,
        ReservationStateIcon(ReservationState[item.reservationState]),
        `${item.parkingLot.name}`,
        `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
        `${item.tenant.name} ${item.tenant.surname}`,
        `${formatDate(new Date(item.from))} - ${formatDate(new Date(item.to))}`
      ];
    });

  return (
    <TableComponent
      data={mappedReservations}
      headerNames={headerNames}
      styles={rowStyleMap}
      paginationLabel={'Reservationen pro Seite'}
      onColumnClick={changeSorting}
    ></TableComponent>
    
  );
};

const useStyles = makeStyles((theme) => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    gap: '3rem'
  },
  pastReservation: {
    // backgroundColor:theme.palette.text,
    backgroundColor:
      theme.palette.mode == 'light'
        ? 'rgba(0, 0, 0, 0.04)'
        : 'rgba(255, 255, 255, 0.08)'
  }
}));
export default MyParkingLotReservationTable;
