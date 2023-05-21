import SearchBar from '../../src/components/search-bar/search-bar';
import React, { useState } from 'react';
import { Grid } from '@mui/material';
import { TagData } from '../../src/components/search-bar/tag-bar';
import SearchParkingLotTable from '../../src/components/search-parking-lot/search-parking-lot-table';
import ParkingDetailModal from '../../src/components/parking-detail-modal/parking-detail-modal';
import { ParkingLotModel } from '../../src/models';
import { format } from 'date-fns';
import { logger } from '../../src/logger';
import { toast } from 'react-toastify';
import { User } from '../api/user';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import { SearchResultModel } from '../../src/models/search/search-result.model';
import CreateReservationModal, {
  CreateReservationConfirmationModalData
} from '../../src/components/reservation/create-reservation-modal';
import NoData from '../../src/components/loading-buffer/no-data';

export interface SearchParameters {
  searchTerm: string;
  fromDate: Date;
  toDate: Date;
  tags: TagData[];
}

const SearchPage = ({ user }) => {
  const initState: {
    error: unknown;
    loading: boolean;
    result: SearchResultModel[] | null;
  } = {
    error: null,
    loading: false,
    result: null //Array<SearchResultModel>()
  };

  const [searchResult, setSearchResult] = useState(initState);
  const [searchParameters, setSearchParameters] = useState<SearchParameters>({
    searchTerm: '',
    fromDate: new Date(),
    toDate: new Date(),
    tags: []
  });

  const [showDetails, setShowDetails] = useState(false);
  const [requestConfirmation, setRequestConfirmation] =
    useState<boolean>(false);
  const [selectedParkingLot, setSelectedParkingLot] =
    useState<ParkingLotModel>();
  const [createReservationData, setCreateReservationData] =
    useState<CreateReservationConfirmationModalData>();

  const onSelectParkingLot = (parkingLot: SearchResultModel) => {
    fetchParkingSpot(parkingLot.id, user).then((result) => {
      setSelectedParkingLot(() => result);
      setCreateReservationData({
        parkingLotID: parkingLot.id,
        id: parkingLot.name,
        fromDate: searchParameters.fromDate,
        toDate: searchParameters.toDate
      });
      setShowDetails(true);
    });
  };
  const closeDetails = (value: boolean) => {
    setRequestConfirmation(value);
    setShowDetails(value);
  };

  const makeOnSearch = (searchParameters: SearchParameters) => {
    setSearchParameters(searchParameters);
    setSearchResult({ error: null, loading: true, result: [] });
    fetchSearch(searchParameters, user)
      .then((result) => {
        setSearchResult({ error: null, loading: false, result: result });
      })
      .catch((error) => toast.error(error.message));
  };

  const createBookingOnClick = (searchItem: SearchResultModel) => {
    setCreateReservationData({
      parkingLotID: searchItem.id,
      id: searchItem.name,
      fromDate: searchParameters.fromDate,
      toDate: searchParameters.toDate
    });
    setRequestConfirmation(true);
  };

  return (
    <Grid padding={2}>
      <Grid item xs={12}>
        <SearchBar makeOnSearch={makeOnSearch}></SearchBar>
      </Grid>
      <Grid item xs={12}>
        {searchResult.result && searchResult.result.length > 0 ? (
          <SearchParkingLotTable
            parkingLots={searchResult.result}
            onRowClick={onSelectParkingLot}
            onCreateBookingClick={createBookingOnClick}
          ></SearchParkingLotTable>
        ) : null}
        {searchResult.result && !searchResult.loading ? (
          <NoData
            resultSize={searchResult.result.length}
            text={'Keine verfügbaren Parkplätze gefunden :('}
          />
        ) : null}

        <Loading loading={searchResult.loading} />
      </Grid>
      {selectedParkingLot && showDetails ? (
        <ParkingDetailModal
          showModal={showDetails}
          setShowModal={closeDetails}
          parkingLotModel={selectedParkingLot}
          makeOnAction={() => {
            setCreateReservationData({
              parkingLotID: selectedParkingLot.id,
              id: selectedParkingLot.name,
              fromDate: searchParameters.fromDate,
              toDate: searchParameters.toDate
            });
            setRequestConfirmation(true);
          }}
        />
      ) : null}
      {requestConfirmation && createReservationData ? (
        <CreateReservationModal
          user={user}
          data={createReservationData}
          onClose={() => {
            setRequestConfirmation(false);
            setCreateReservationData(undefined);
          }}
        />
      ) : null}
    </Grid>
  );
};

const fetchSearch = async (
  searchParameters: SearchParameters,
  user: User
): Promise<SearchResultModel[]> => {
  const query = new URLSearchParams({
    searchTerm: searchParameters.searchTerm,
    startDate: format(searchParameters.fromDate, 'yyyy-MM-dd'),
    endDate: format(searchParameters.toDate, 'yyyy-MM-dd')
  });
  searchParameters.tags.forEach((tag) => query.append('tagList', tag.label));
  return fetch('/backend/parking-lot/searchTerm?' + query, {
    method: 'GET',
    headers: { Authorization: `Bearer ${user.token}` }
  }).then(
    async (response) => {
      const data = await response.json();
      logger.log(data);
      return data;
    },
    (reject) => {
      return reject.message;
    }
  );
};

const fetchParkingSpot = async (
  id: number,
  user: User
): Promise<ParkingLotModel> => {
  return fetch('/backend/parking-lot/' + id, {
    method: 'GET',
    headers: { Authorization: `Bearer ${user.token}` }
  }).then(async (response) => {
    const data = await response.json();
    logger.log(data);
    return data;
  });
};

export default SearchPage;
