import TableComponent, { SortOrder } from '../table/table-component';
import { RowDataType } from '../table/table-row';
import ReservationStateIcon from './reservation-state-icon';
import { formatDate } from '../../date/date-formatter';
import CancelCell from './reservation-cancel-cell';
import { ReservationModel } from '../../models/reservation/reservation.model';
import { useEffect, useState } from 'react';

interface reservationTableProps {
  reservations: ReservationModel[];
  openModifyModal: (row: RowDataType) => void;
  onCancel: (reservation: ReservationModel) => void;
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
  openModifyModal,
  onCancel
}: reservationTableProps) => {
  const [sortFunction, setSortFunction] =
    useState<(a: ReservationModel, b: ReservationModel) => number>();

  const [sortOrder, setSortOrder] = useState<number>(SortOrder.desc);

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
      case 'Besitzer':
        fun = (a: ReservationModel, b: ReservationModel): number =>
          (a.parkingLot.owner.name > b.parkingLot.owner.name ? 1 : -1) *
          sortOrder;
        break;
      case 'Ort':
        fun = (a: ReservationModel, b: ReservationModel): number =>
          (a.parkingLot.address > b.parkingLot.address ? 1 : -1) * sortOrder;
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

  const mapped: Array<RowDataType> = reservations
    .sort(sortFunction)
    .map((item) => {
      return [
        `${item.id}`,
        ReservationStateIcon(item.reservationState),
        `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
        `${item.tenant.name} ${item.tenant.surname}`,
        `${formatDate(new Date(item.from))} - ${formatDate(new Date(item.to))}`,
        CancelCell({
          reservation: item,
          onClick: () => onCancel(item)
        })
      ];
    });

  return (
    <TableComponent
      data={mapped}
      headerNames={headerNames}
      onRowClick={openModifyModal}
      paginationLabel={'Reservationen pro Seite'}
      onColumnClick={changeSorting}
    ></TableComponent>
  );
};

export default ReservationTable;
