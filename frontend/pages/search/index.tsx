import SearchBar from '../../src/components/search-bar/search-bar';
import React, { useState } from 'react';
import { Grid } from '@mui/material';
import { TagData } from '../../src/components/search-bar/tag-bar';
import SearchParkingLotTable from '../../src/components/search-parking-lot/search-parking-lot-table';
import ParkingDetailModal from '../../src/components/parking-detail-modal/parking-detail-modal';
import { ParkingLotModel } from '../../src/models';
import { formatDate } from '../../src/date/date-formatter';
import { format } from 'date-fns';
import { logger } from '../../src/logger';
import { toast } from 'react-toastify';
import useUser from '../../src/auth/use-user';
import { User } from '../api/user';
import { Loading } from '../../src/components/loading-buffer/loading-buffer';
import { RowDataType } from '../../src/components/table/table-row';
import { SearchResultModel } from '../../src/models/search/search-result.model';
import {
  CreateReservationModel,
  ReservationModel
} from '../../src/models/reservation/reservation.model';

export interface SearchParameters {
  searchTerm: string;
  fromDate: Date;
  toDate: Date;
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
  const [searchParameters, setSearchParameters] = useState<SearchParameters>();

  const [showDetails, setShowDetails] = useState(false);
  const [selectedParkingLot, setSelectedParkingLot] =
    useState<ParkingLotModel>();

  const onSelectParkingLot = (data: string[]) => {
    const name = data[0];
    const selected = searchResult.result.find((value) =>
      value.name.includes(name)
    );
    if (selected && user) {
      fetchParkingSpot(selected.id, user).then((result) => {
        setSelectedParkingLot(() => result);
        setShowDetails(true);
      });
    }
  };

  const createReservation = () => {
    if (selectedParkingLot && searchParameters) {
      createReservationCall(
        {
          from: format(searchParameters.fromDate, 'yyy-MM-dd'),
          to: format(searchParameters.toDate, 'yyy-MM-dd'),
          parkingLotID: selectedParkingLot.id
        },
        user
      )
        .then((response) => toast.success('Buchung erfolgreich ' + response.id))
        .catch((reject) => toast.error(reject.message));
    }
  };

  const makeOnSearch = (searchParameters: SearchParameters) => {
    if (!user) return;
    setSearchParameters(searchParameters);
    setSearchResult({ error: null, loading: true, result: [] });
    fetchSearch(searchParameters, user)
      .then((result) => {
        setSearchResult({ error: null, loading: false, result: result });
      })
      .catch((error) => toast.error(error.message));
  };

  const mappedResult: Array<RowDataType> = searchResult.result.map((item) => {
    return [
      `${item.name}`,
      `${item.address}`,
      `${item.owner}`,
      `${formatDate(new Date(item.from))} - ${formatDate(new Date(item.to))}`,
      `reservieren`
    ];
  });

  return (
    <Grid padding={2}>
      <Grid item xs={12}>
        <SearchBar makeOnSearch={makeOnSearch}></SearchBar>
      </Grid>

      <Grid item xs={12}>
        {mappedResult.length > 0 ? (
          <SearchParkingLotTable
            parkingLots={mappedResult}
            onRowClick={onSelectParkingLot}
          ></SearchParkingLotTable>
        ) : (
          <Loading loading={searchResult.loading} />
        )}
      </Grid>
      {selectedParkingLot ? (
        <ParkingDetailModal
          showModal={showDetails}
          setShowModal={setShowDetails}
          parkingLotModel={selectedParkingLot}
          fromDate={searchParameters?.fromDate || new Date()}
          toDate={searchParameters?.toDate || new Date()}
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
    startDate: format(searchParameters.fromDate, 'yyy-MM-dd'),
    endDate: format(searchParameters.toDate, 'yyy-MM-dd')
  });

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
const createReservationCall = async (
  body: CreateReservationModel,
  user: User
): Promise<ReservationModel> => {
  return fetch('/backend/reservations', {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${user.token}`,
      'content-type': 'application/json'
    },
    body: JSON.stringify(body)
  }).then(
    async (response) => {
      if (response.ok) {
        const data = await response.json();
        logger.log(data);
        return data;
      }
    },
    async (reject) => {
      const data = await reject.json();
      return data.message;
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
