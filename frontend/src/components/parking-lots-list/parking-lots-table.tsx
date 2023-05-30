import React, { useEffect, useState } from 'react';
import TableComponent, { SortOrder } from '../table/table-component';
import { RowDataType } from '../table/table-row';
import ParkingLotStateToggleButton from './parking-lot-state-toggle-button';
import { ParkingLotModel, ParkingLotState } from '../../models';

const headerNames = ['ID', 'Bezeichnung', 'Ort', 'Besitzer', 'Status'];

const ParkingLotsTable = ({
  parkingLots,
  updateParkingLot
}: {
  parkingLots: ParkingLotModel[];
  updateParkingLot: (parkingLot: ParkingLotModel) => void;
}) => {
  const [sortFunction, setSortFunction] =
    useState<(a: ParkingLotModel, b: ParkingLotModel) => number>();

  const [sortOrder, setSortOrder] = useState<number>(SortOrder.desc);
  const changeSorting = (colName: string) => {
    setSortOrder(() =>
      sortOrder == SortOrder.desc ? SortOrder.asc : SortOrder.desc
    );
    let fun;
    switch (colName) {
      case 'ID':
        fun = (a: ParkingLotModel, b: ParkingLotModel): number =>
          (a.id > b.id ? 1 : -1) * sortOrder;
        break;
      case 'Bezeichnung':
        fun = (a: ParkingLotModel, b: ParkingLotModel): number =>
          (a.name > b.name ? 1 : -1) * sortOrder;
        break;
      case 'Besitzer':
        fun = (a: ParkingLotModel, b: ParkingLotModel): number =>
          (a.owner.name > b.owner.name ? 1 : -1) * sortOrder;
        break;
      case 'Ort':
        fun = (a: ParkingLotModel, b: ParkingLotModel): number =>
          (a.address > b.address ? 1 : -1) * sortOrder;
        break;
      case 'Status':
        fun = (a: ParkingLotModel, b: ParkingLotModel): number =>
          (a.state > b.state ? 1 : -1) * sortOrder;
        break;
      default:
    }
    setSortFunction(() => fun);
  };

  const changeParkingLotState = (selected) => {
    const lot = parkingLots.find((value) => value.id == selected.id);

    if (!lot) return;
    lot.state =
      lot.state == ParkingLotState.locked
        ? ParkingLotState.active
        : ParkingLotState.locked;
    updateParkingLot(lot);
  };

  useEffect(() => {
    changeSorting('gebucht von - bis');
  }, []);

  const mapped: Array<RowDataType> = parkingLots
    .sort(sortFunction)
    .map((parkingLot) => {
      return [
        `${parkingLot.id}`,
        `${parkingLot.name}`,
        `${parkingLot.address} ${parkingLot.addressNr}`,
        `${parkingLot.owner.name} ${parkingLot.owner.surname}`,
        <ParkingLotStateToggleButton
          parkingLot={parkingLot}
          changeParkingLotState={changeParkingLotState}
        />
      ];
    });
  return (
    <TableComponent
      data={mapped}
      headerNames={headerNames}
      paginationLabel={'Parklätze pro Seite'}
      onColumnClick={changeSorting}
    ></TableComponent>
  );
};

export default ParkingLotsTable;
