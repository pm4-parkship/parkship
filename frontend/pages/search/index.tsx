import SearchBar from '../../src/components/search-bar/search-bar';
import React, { useState } from 'react';
import { Grid, Typography } from '@mui/material';
import { TagData } from '../../src/components/search-bar/tag-bar';
import SearchParkingLotTable from '../../src/components/search-parking-lot/search-parking-lot-table';
import ParkingDetailModal from '../../src/components/parking-detail-modal/parking-detail-modal';
import { ParkingLotModel } from '../../src/models';
import { searchDummy, parkingDummyData } from './dummy';

export interface SearchParameters {
  searchTerm: string;
  fromDate: string;
  toDate: string;
  tags: TagData[];
}

const SearchPage = () => {
  const [searchResult, setSearchResult] = useState<SearchParameters | null>(
    null
  );
  const [showDetails, setShowDetails] = useState(false);
  const [selectedParkingLot, setSelectedParkingLot] =
    useState<ParkingLotModel>();
  const fetchParkingSpots = (searchParameters: SearchParameters) => {
    setSearchResult(() => searchParameters);
  };
  const onSelectParkingLot = (data: string[]) => {
    const id = data[0];
    setSelectedParkingLot(() =>
      parkingDummyData.find((value) => value.id == id)
    );
    setShowDetails(true);
  };
  return (
    <Grid padding={2}>
      <Grid item xs={12}>
        <SearchBar fetchParkingSpots={fetchParkingSpots}></SearchBar>
      </Grid>

      <Grid item xs={12}>
        {/*todo add result and loading*/}
        {searchResult ? (
          <div>
            <Typography>{JSON.stringify(searchResult)}</Typography>
            <SearchParkingLotTable
              parkingLots={searchDummy}
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

export default SearchPage;
