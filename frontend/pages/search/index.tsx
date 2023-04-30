import SearchBar from '../../src/components/search-bar/search-bar';
import React, { useState } from 'react';
import { Grid } from '@mui/material';
import { TagData } from '../../src/components/search-bar/tag-bar';
import SearchParkingLotTable from '../../src/components/search-parking-lot/search-parking-lot-table';
import ParkingDetailModal from '../../src/components/parking-detail-modal/parking-detail-modal';
import { ParkingLotModel } from '../../src/models';
import { parkingDummyData } from '../../src/data/parkinglots';
import { formatDate } from '../../src/date/date-formatter';
import { format } from 'date-fns';
import { logger } from '../../src/logger';
import { toast } from 'react-toastify';
import useUser from '../../src/auth/use-user';
import { User } from '../api/user';
import {
  mapSearchResultToParkingLot,
  SearchResultModel
} from '../../src/models/search/search-result.model';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';

export interface SearchParameters {
  searchTerm: string;
  fromDate: string;
  toDate: string;
  tags: TagData[];
}

const SearchPage = () => {
  const { user } = useUser();

  const initState = {
    error: null,
    loading: false,
    result: Array<SearchResultModel>()
  };

  const [searchResult, setSearchResult] = useState(initState);

  const [showDetails, setShowDetails] = useState(false);
  const [selectedParkingLot, setSelectedParkingLot] =
    useState<ParkingLotModel>();

  let fromDatum = '2021-09-01';
  let toDatum = '2021-09-02';

  const onSelectParkingLot = (data: string[]) => {
    const id = data[0];
    const parkingLot = searchResult.result.find((value) => value.id == id);
    if (parkingLot) {
      setSelectedParkingLot(() => mapSearchResultToParkingLot(parkingLot));
      setShowDetails(true);
    }
  };

  const makeOnSearch = (searchParameters: SearchParameters) => {
    if (!user) return;
    fromDatum = searchParameters.fromDate;
    toDatum = searchParameters.toDate;
    setSearchResult({ error: null, loading: true, result: [] });
    fetchParkingSpots(searchParameters, false, user)
      .then((result) => {
        setSearchResult({ error: null, loading: false, result: result });
      })
      .catch((error) => toast.error(error.message));
  };

  const mappedResult: Array<string[]> = searchResult.result.map((item) => {
    return [
      `${item.id}`,
      `${item.address} ${item.addressNr}`,
      `${item.owner.name} ${item.owner.surname}`,
      `${formatDate(new Date())} - ${formatDate(new Date())}`,
      `reservieren`
    ];
  });

  return (
    <Grid padding={2}>
      <Grid item xs={12}>
        <SearchBar makeOnSearch={makeOnSearch} />
      </Grid>

      <Grid item xs={12}>
        {mappedResult.length > 0 ? (
          <SearchParkingLotTable
            parkingLots={mappedResult}
            onRowClick={onSelectParkingLot}
          />
        ) : (
          <Loading loading={searchResult.loading} />
        )}
      </Grid>
      {selectedParkingLot ? (
        <ParkingDetailModal
          showModal={showDetails}
          setShowModal={setShowDetails}
          parkingLotModel={selectedParkingLot}
          fromDate={fromDatum}
          toDate={toDatum}
        />
      ) : null}
    </Grid>
  );
};

const fetchParkingSpots = async (
  searchParameters: SearchParameters,
  useApi = false,
  user: User
): Promise<SearchResultModel[]> => {
  if (useApi) {
    const query = new URLSearchParams({
      searchTerm: searchParameters.searchTerm,
      startDate: format(new Date(searchParameters.fromDate), 'yyy-MM-dd'),
      endDate: format(new Date(searchParameters.toDate), 'yyy-MM-dd')
    });

    return fetch('/backend/parking-lot/searchTerm?' + query, {
      method: 'GET',
      headers: { Authorization: `Bearer ${user.token}` }
    }).then(async (response) => {
      const data = await response.json();
      logger.log(data);
      return data;
    });
  } else {
    return new Promise((resolve) => {
      setTimeout(() => resolve(parkingDummyData), 2000);
    });
  }
};

export default SearchPage;
