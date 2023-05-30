import { User } from './user';
import { logger } from '../../src/logger';
import { ParkingLotReservationModel } from '../../src/models/parking-lot-reservations/parking-lot-reservations.model';
import {
  CreateParkingLotModel,
  CreateUserModel,
  OfferCreateModel,
  ParkingLotModel,
  UserDto,
  UserModel,
  UserRole,
  UserState
} from '../../src/models';
import {
  CreateReservationModel,
  ModifyReservationModel,
  ReservationModel
} from '../../src/models/reservation/reservation.model';
import { SearchResultModel } from '../../src/models/search/search-result.model';
import { format } from 'date-fns';
import { SearchParameters } from '../search';

const BASE_URL = '/backend';
const fetchData = async <T>(
  url: string,
  user: User,
  method,
  body?: unknown
): Promise<T> => {
  const response = await fetch(BASE_URL + url, {
    method,
    headers: {
      Authorization: `Bearer ${user.token}`,
      'content-type': 'application/json'
    },
    body: JSON.stringify(body)
  });

  const contentType = response.headers.get('Content-Type') || '';
  if (!contentType.includes('application/json')) return response.ok as T;
  const data = await response.json();

  if (!response.ok) {
    logger.error(data);
    throw new Error(data.message.match(/"(.*)"/)[1]);
  }
  logger.log(data);
  return data;
};

export interface PaginationResponse<T> {
  data: T;
  page: number;
  size: number;
  totalPages: number;
  totalSize: number;
}

const apiClient = () => {
  const getReservationsFromMyParkingLots = async (
    user: User
  ): Promise<ParkingLotReservationModel> => {
    return fetchData<ParkingLotReservationModel>(
      '/parking-lot/reservations',
      user,
      'GET'
    );
  };

  const getMyParkingLots = async (user: User): Promise<ParkingLotModel[]> => {
    return fetchData<ParkingLotModel[]>(
      '/parking-lot/my-parkinglots',
      user,
      'GET'
    );
  };
  const getMyReservations = async (user: User): Promise<ReservationModel[]> => {
    return fetchData<ReservationModel[]>('/reservations', user, 'GET');
  };
  const cancelReservation = async (
    reservationID: number,
    user: User
  ): Promise<ReservationModel> => {
    return fetchData<ReservationModel>(
      `/reservations/${reservationID}`,
      user,
      'DELETE'
    );
  };

  const createReservation = async (
    body: CreateReservationModel,
    user: User
  ): Promise<ReservationModel> => {
    return fetchData<ReservationModel>('/reservations', user, 'POST', body);
  };
  const updateReservation = async (
    body: ModifyReservationModel,
    user: User
  ): Promise<ReservationModel> => {
    return fetchData<ReservationModel>(
      `/reservations/${body.reservationID}`,
      user,
      'PUT',
      body
    );
  };
  const searchParkingLot = async (
    searchParameters: SearchParameters,
    user: User
  ): Promise<SearchResultModel[]> => {
    const query = new URLSearchParams({
      searchTerm: searchParameters.searchTerm,
      startDate: format(searchParameters.fromDate, 'yyy-MM-dd'),
      endDate: format(searchParameters.toDate, 'yyy-MM-dd')
    });
    searchParameters.tags.forEach((tag) => query.append('tagList', tag.label));
    return fetchData<SearchResultModel[]>(
      `/parking-lot/searchTerm?${query}`,
      user,
      'GET'
    );
  };

  const getParkingLot = async (
    id: number,
    user: User
  ): Promise<ParkingLotModel> => {
    return fetchData<ParkingLotModel>(`/parking-lot/${id}`, user, 'GET');
  };

  const createParkingLot = async (
    user: User,
    body: CreateParkingLotModel
  ): Promise<ParkingLotModel> => {
    return fetchData('/parking-lot', user, 'POST', body);
  };
  const createParkingLotOffer = async (
    user: User,
    body: OfferCreateModel[]
  ): Promise<OfferCreateModel[]> => {
    return fetchData('/offer', user, 'POST', body);
  };
  //ADMIN
  const getAllUsers = async (user: User): Promise<UserModel[]> => {
    const query = new URLSearchParams({
      page: '1',
      size: '100'
    });
    return fetchData<UserModel[]>(`/users?${query}`, user, 'GET');
  };

  const updateUserState = async (
    users: UserModel,
    user: User
  ): Promise<boolean> => {
    return fetchData<boolean>(
      `/users/${users.id}/${
        users.userState == UserState.UNLOCKED ? 'unlock' : 'lock'
      }`,
      user,
      'PUT'
    );
  };

  const getAllParkingLots = async (
    user: User
  ): Promise<PaginationResponse<ParkingLotModel[]>> => {
    const query = new URLSearchParams({
      page: '1',
      size: '100'
    });
    return fetchData<PaginationResponse<ParkingLotModel[]>>(
      `/parking-lot?${query}`,
      user,
      'GET'
    );
  };
  const updateParkingLotState = async (
    parkingLot: ParkingLotModel,
    user: User
  ): Promise<boolean> => {
    return fetchData<boolean>(
      `/parking-lot/${parkingLot.id}/update-state/${parkingLot.state}`,
      user,
      'PUT'
    );
  };
  const getUserRoles = async (user: User): Promise<UserRole[]> => {
    return fetchData<UserRole[]>(`/users/roles`, user, 'GET');
  };
  const createUser = async (
    user: User,
    body: CreateUserModel
  ): Promise<UserDto> => {
    return fetchData<UserDto>(`/users/signup`, user, 'POST', body);
  };

  const admin = {
    updateParkingLotState,
    getAllParkingLots,
    updateUserState,
    getAllUsers,
    getUserRoles,
    createUser
  };
  const user = {
    updateReservation,
    getReservationsFromMyParkingLots,
    getMyParkingLots,
    getParkingLot,
    getMyReservations,
    createReservation,
    cancelReservation,
    searchParkingLot,
    createParkingLot,
    createParkingLotOffer
  };
  return {
    user,
    admin
  };
};

export default apiClient;
