import React, { useEffect, useState } from 'react';
import { Loading } from '../../../src/components/loading-buffer/loading-buffer';
import { Grid, Typography } from '@mui/material';
import { logger } from '../../../src/logger';
import useUser from '../../../src/auth/use-user';
import { ParkingLotModel, ParkingLotState } from '../../../src/models';
import ParkingLotsFilter from '../../../src/components/parking-lots-list/parking-lots-filter';
import { RowDataType } from '../../../src/components/table/table-row';
import ParkingLotsTable from '../../../src/components/parking-lots-list/parking-lots-table';
import { User } from '../../api/user';
import ParkingLotStateToggleButton from '../../../src/components/parking-lots-list/parking-lot-state-toggle-button';
import { toast } from 'react-toastify';

export interface ParkingLotsFilterData {
  states: Set<ParkingLotState>;
  searchTerm: string;
}

const initFilter: ParkingLotsFilterData = { states: new Set(), searchTerm: '' };
const initState = {
  error: null,
  loading: false,
  result: Array<ParkingLotModel>()
};

const ParkingLotsPage = () => {
  const { user } = useUser();

  const [filter, setFilter] = useState<ParkingLotsFilterData>(initFilter);
  const [parkingLots, setParkingLots] = useState(initState);

  const changeParkingLotState = (selected) => {
    const lot = parkingLots.result.find((value) => value.id == selected.id);

    if (!lot || !user) return;
    lot.state =
      lot.state == ParkingLotState.locked
        ? ParkingLotState.released
        : ParkingLotState.locked;

    updateParkingLot(lot, user).then((result) => {
      if (result) {
        parkingLots.result.map((obj) => (obj.id == lot.id ? lot : obj));
        setParkingLots({
          error: null,
          loading: false,
          result: parkingLots.result
        });
        toast.success(`Parkplatz ${lot.id} erfolgreich aktualisiert`);
      } else {
        toast.error(`Parkplatz ${lot.id} Update fehlerhaft`);
      }
    });
  };

  useEffect(() => {
    if (user) {
      setParkingLots({ error: null, loading: true, result: [] });
      fetchAllParkingLots(user)
        .then((result) => {
          if (result) {
            setParkingLots({ error: null, loading: false, result: result });
          }
        })
        .catch();
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

  const filteredParkingLots: Array<RowDataType> =
    parkingLots.result &&
    parkingLots.result
      .filter((parkingLot) => parkingLotFilter(parkingLot))
      .map((parkingLot) => {
        return [
          `${parkingLot.id}`,
          `${parkingLot.address} ${parkingLot.addressNr}`,
          `${parkingLot.owner.name} ${parkingLot.owner.surname}`,
          <ParkingLotStateToggleButton
            parkingLot={parkingLot}
            changeParkingLotState={changeParkingLotState}
          />
        ];
      });

  return (
    <Grid padding={2}>
      <Grid item xs={12}>
        <ParkingLotsFilter updateFilter={setFilter} />
      </Grid>

      <Grid item xs={12}>
        <>
          <Loading loading={parkingLots.loading} />

          {parkingLots.result.length > 0 ? (
            <ParkingLotsTable parkingLots={filteredParkingLots} />
          ) : (
            <NoData size={parkingLots.result.length} />
          )}
        </>
      </Grid>
    </Grid>
  );
};

const NoData = ({ size }) =>
  size > 0 ? <Typography>Keine Parkplätze gefunden :(</Typography> : null;

const fetchAllParkingLots = async (user: User): Promise<ParkingLotModel[]> => {
  const query = new URLSearchParams({
    page: '1',
    size: '100'
  });
  return await fetch('/backend/parking-lot?' + query, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      logger.log(data);
      return data.data;
    }
  });
};
const updateParkingLot = async (
  parkingLot: ParkingLotModel,
  user: User
): Promise<boolean> => {
  return await fetch(`/backend/parking-lot/${parkingLot.id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then((response) => {
    return response.ok;
  });
};
export default ParkingLotsPage;