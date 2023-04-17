import SearchBar from '../../src/components/search-bar/search-bar';
import React, { useState } from 'react';
import { Grid, Typography } from '@mui/material';
import { TagData } from '../../src/components/search-bar/tag-bar';
import SearchParkingLotTable from '../../src/components/search-parking-lot/search-parking-lot-table';
import ParkingDetailModal from '../../src/components/parking-detail-modal/parking-detail-modal';
import { ParkingLotModel } from '../../src/models';
import { parkingDummyData } from '../../src/data/parkinglots';
import { formatDate } from '../../src/date/date-formatter';
import { format } from 'date-fns';

export interface SearchParameters {
  searchTerm: string;
  fromDate: string;
  toDate: string;
  tags: TagData[];
}

const SearchPage = () => {
  const [searchResult, setSearchResult] = useState<ParkingLotModel[]>([]);

  const [showDetails, setShowDetails] = useState(false);
  const [selectedParkingLot, setSelectedParkingLot] =
    useState<ParkingLotModel>();

  const onSelectParkingLot = (data: string[]) => {
    const id = data[0];
    setSelectedParkingLot(() => searchResult.find((value) => value.id == id));
    setShowDetails(true);
  };

  const makeOnSearch = (searchParameters: SearchParameters) => {
    fetchParkingSpots(searchParameters, false)
      .then((result) => {
        setSearchResult(result);
      })
      .catch();
  };

  const mappedResult: Array<string[]> = searchResult.map((item) => {
    return [
      `${item.id}`,
      `${item.address} ${item.addressNr}`,
      `${item.owner}`,
      `${formatDate(new Date())} - ${formatDate(new Date())}`,
      `reservieren`
    ];
  });

  return (
    <Grid padding={2}>
      <Grid item xs={12}>
        <SearchBar makeOnSearch={makeOnSearch}></SearchBar>
      </Grid>

      <Grid item xs={12}>
        {/*todo add result and loading*/}
        {searchResult ? (
          <div>
            <SearchParkingLotTable
              parkingLots={mappedResult}
              onRowClick={onSelectParkingLot}
            ></SearchParkingLotTable>
          </div>
        ) : (
          <Typography>Todo loading and result here</Typography>
        )}
      </Grid>
      {selectedParkingLot ? (
        <ParkingDetailModal
          showModal={showDetails}
          setShowModal={setShowDetails}
          parkingLotModel={selectedParkingLot}
        />
      ) : null}
    </Grid>
  );
};

const fetchParkingSpots = (
  searchParameters: SearchParameters,
  useApi = false
): Promise<ParkingLotModel[]> => {
  if (useApi) {
    const query = new URLSearchParams({
      searchTerm: searchParameters.searchTerm,
      startDate: format(new Date(searchParameters.fromDate), 'yyy-MM-dd'),
      endDate: format(new Date(searchParameters.toDate), 'yyy-MM-dd')
    });

    return fetch('/backend/parking-lot/searchTerm?' + query, {
      method: 'GET'
    }).then((response) => response.json());
  } else {
    return new Promise((resolve, reject) => {
      resolve(parkingDummyData);
      reject('epic fail');
    });
  }
};
export default SearchPage;
