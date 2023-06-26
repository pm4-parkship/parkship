import SearchBar from '../../src/components/search-bar/search-bar';
import React, { useState } from 'react';
import { Grid } from '@mui/material';
import SearchParkingLotTable from '../../src/components/search-parking-lot/search-parking-lot-table';
import ParkingDetailModal from '../../src/components/parking-detail-modal/parking-detail-modal';
import { ParkingLotModel, TagData } from '../../src/models';
import { toast } from 'react-toastify';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import { SearchResultModel } from '../../src/models/search/search-result.model';
import CreateReservationModal, {
  CreateReservationConfirmationModalData
} from '../../src/components/reservation/create-reservation-modal';
import NoData from '../../src/components/loading-buffer/no-data';
import apiClient from '../api/api-client';

export interface SearchParameters {
  searchTerm: string;
  fromDate: Date;
  toDate: Date;
  tags: TagData[];
}

const SearchPage = ({ user }) => {
  const initState: {
    loading: boolean;
    result: SearchResultModel[] | null;
  } = {
    loading: false,
    result: null
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
    apiClient()
      .user.getParkingLot(parkingLot.id, user)
      .then((result) => {
        setSelectedParkingLot(() => result);
        setCreateReservationData({
          parkingLotID: parkingLot.id,
          id: parkingLot.name,
          fromDate: searchParameters.fromDate,
          toDate: searchParameters.toDate
        });
        setShowDetails(true);
      })
      .catch(() =>
        toast.error(
          `Daten zum Parkplatz ${parkingLot.name} konnte nicht geladen werden. Versuchen Sie es später nochmal`
        )
      );
  };
  const closeDetails = (value: boolean) => {
    setRequestConfirmation(value);
    setShowDetails(value);
  };

  const makeOnSearch = (searchParameters: SearchParameters) => {
    setSearchParameters(searchParameters);
    setSearchResult({ loading: true, result: [] });
    apiClient()
      .user.searchParkingLot(searchParameters, user)
      .then((result) => {
        setSearchResult({ loading: false, result: result });
      })
      .catch(() =>
        toast.error(
          'Es gibt Schwierigkeiten bei der Suche von verfügbaren Parkplätze. Versuchen Sie es später nochmal.'
        )
      );
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

export default SearchPage;
