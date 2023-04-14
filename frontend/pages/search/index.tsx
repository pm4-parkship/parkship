import SearchBar from '../../src/components/search-bar/search-bar';
import React, { useState } from 'react';
import { Grid, Typography } from '@mui/material';
import { TagData } from '../../src/components/search-bar/tag-bar';
import TableComponent from '../../src/components/table/table';
import { ParkingLotAction } from 'src/components/parking-reservation-confirmation-modal/parking-reservation-confirmation-modal';

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

  const fetchParkingSpots = (searchParameters: SearchParameters) => {
    setSearchResult(() => searchParameters);
  };

  return (
    <Grid>
      <Grid item xs={12}>
        <SearchBar fetchParkingSpots={fetchParkingSpots}></SearchBar>
      </Grid>

      <Grid item xs={12}>
        {' '}
        {/*todo add result and loading*/}
        {searchResult ? (
          <div>
            <Typography>{JSON.stringify(searchResult)}</Typography>
            <TableComponent
              status={ParkingLotAction.RESERVIEREN}
            ></TableComponent>
          </div>
        ) : (
          <Typography>Todo loading and result here</Typography>
        )}
      </Grid>
    </Grid>
  );
};

export default SearchPage;
