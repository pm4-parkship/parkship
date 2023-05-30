import React, { useEffect, useState } from 'react';
import { Loading } from '../../../src/components/loading-buffer/loading-buffer';
import { Grid, Typography } from '@mui/material';
import { ParkingLotModel, ParkingLotState } from '../../../src/models';
import ParkingLotsFilter from '../../../src/components/parking-lots-list/parking-lots-filter';
import ParkingLotsTable from '../../../src/components/parking-lots-list/parking-lots-table';
import { toast } from 'react-toastify';
import apiClient from '../../api/api-client';

export interface ParkingLotsFilterData {
  states: Set<ParkingLotState>;
  searchTerm: string;
}

const initFilter: ParkingLotsFilterData = { states: new Set(), searchTerm: '' };
const initState = {
  loading: false,
  result: Array<ParkingLotModel>()
};

const ParkingLotsPage = ({ user }) => {
  const [filter, setFilter] = useState<ParkingLotsFilterData>(initFilter);
  const [parkingLots, setParkingLots] = useState(initState);

  const updateParkingLot = (parkingLot) => {
    apiClient()
      .admin.updateParkingLotState(parkingLot, user)
      .then(() => {
        parkingLots.result.map((obj) =>
          obj.id == parkingLot.id ? parkingLot : obj
        );
        setParkingLots({
          loading: false,
          result: parkingLots.result
        });
        toast.success(`Parkplatz ${parkingLot.name} erfolgreich aktualisiert`);
      })
      .catch(() => {
        toast.error(`Parkplatz ${parkingLot.name} Update fehlerhaft`);
      });
  };

  useEffect(() => {
    if (user) {
      setParkingLots({ loading: true, result: [] });
      apiClient()
        .admin.getAllParkingLots(user)
        .then((result) => {
          if (result) {
            setParkingLots({ loading: false, result: result.data });
          }
        })
        .catch(() =>
          toast.error(
            `Die Parkplatzliste konnte nicht geladen werdne. Versuchen Sie es später nochmal`
          )
        );
    }
  }, []);

  const parkingLotFilter = (parkingLot: ParkingLotModel): boolean => {
    return (
      (filter.states.has(parkingLot.state) || !filter.states.size) &&
      (parkingLot.address.includes(filter.searchTerm) ||
        parkingLot.name.includes(filter.searchTerm) ||
        parkingLot.owner.name.includes(filter.searchTerm) ||
        parkingLot.owner.surname.includes(filter.searchTerm))
    );
  };

  const filteredParkingLots =
    parkingLots.result &&
    parkingLots.result.filter((parkingLot) => parkingLotFilter(parkingLot));

  return (
    <Grid padding={2}>
      <Grid item xs={12}>
        <ParkingLotsFilter updateFilter={setFilter} />
      </Grid>

      <Grid item xs={12}>
        <>
          <Loading loading={parkingLots.loading} />

          {parkingLots.result.length > 0 ? (
            <ParkingLotsTable
              parkingLots={filteredParkingLots}
              updateParkingLot={updateParkingLot}
            />
          ) : (
            <NoData size={parkingLots.result.length} />
          )}
        </>
      </Grid>
    </Grid>
  );
};
const NoData = ({ size }) =>
  size == 0 ? <Typography>Keine Parkplätze gefunden :(</Typography> : null;

export default ParkingLotsPage;
