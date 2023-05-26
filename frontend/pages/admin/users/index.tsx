import React, { useEffect, useState } from 'react';
import { UserModel, UserRole, UserState } from '../../../src/models';
import { logger } from '../../../src/logger';
import { toast } from 'react-toastify';
import { User } from '../../api/user';
import { Button, Grid, Typography } from '@mui/material';
import { Loading } from '../../../src/components/loading-buffer/loading-buffer';
import UsersTable from '../../../src/components/user-list/users-table';
import UsersFilter from '../../../src/components/user-list/user-filter';
import UserAdministrationModal from 'src/components/user-administration/user-administration-modal/user-administration-modal';

export interface UserFilterData {
  states: Set<UserState>;
  roles: Set<UserRole>;
  searchTerm: string;
}

const initFilter: UserFilterData = {
  states: new Set(),
  roles: new Set(),
  searchTerm: ''
};
const initState = {
  error: null,
  loading: false,
  result: Array<UserModel>()
};

const handleAddedUser = (data) => {
  logger.log(data);
};

const UserPage = ({ user }) => {
  const [openUserAdministrationModal, setOpenUserAdministrationModal] =
    useState(false);
  const [filter, setFilter] = useState<UserFilterData>(initFilter);
  const [users, setUsers] = useState(initState);

  const updateUser = (userUpdated: UserModel) => {
    updateUsers(userUpdated, user).then((result) => {
      if (result) {
        users.result.map((obj) =>
          obj.id == userUpdated.id ? userUpdated : obj
        );
        setUsers({
          error: null,
          loading: false,
          result: users.result
        });
        toast.success(`Benutzer ${userUpdated.id} erfolgreich aktualisiert`);
      } else {
        toast.error(`Benutzer ${userUpdated.id} Update fehlerhaft`);
      }
    });
  };

  useEffect(() => {
    if (user) {
      setUsers({ error: null, loading: true, result: [] });
      fetchUsers(user)
        .then((result) => {
          if (result) {
            setUsers({ error: null, loading: false, result: result });
          }
        })
        .catch();
    }
  }, []);

  const filterUsers = (user: UserModel): boolean => {
    return (
      (filter.states.has(user.userState) || !filter.states.size) &&
      (filter.roles.has(user.role) || !filter.roles.size) &&
      (user.name.includes(filter.searchTerm) ||
        user.surname.includes(filter.searchTerm) ||
        user.email.includes(filter.searchTerm))
    );
  };
  const filteredUsers =
    users.result && users.result.filter((user) => filterUsers(user));

  return (
    <Grid padding={2}>
      <Button
        variant="contained"
        onClick={() => setOpenUserAdministrationModal(true)}
      >
        Benutzer hinzufügen
      </Button>
      <UserAdministrationModal
        showModal={openUserAdministrationModal}
        setShowModal={setOpenUserAdministrationModal}
        onAddedUser={handleAddedUser}
        user={user}
      />

      <Grid item xs={12}>
        <UsersFilter updateFilter={setFilter} />
      </Grid>

      <Grid item xs={12}>
        <>
          <Loading loading={users.loading} />

          {users.result.length > 0 ? (
            <UsersTable users={filteredUsers} updateUser={updateUser} />
          ) : (
            <NoData size={users.result.length} />
          )}
        </>
      </Grid>
    </Grid>
  );
};

const NoData = ({ size }) =>
  size > 0 ? <Typography>Kein Benutzer gefunden :(</Typography> : null;

const fetchUsers = async (user: User): Promise<UserModel[]> => {
  const query = new URLSearchParams({
    page: '1',
    size: '100'
  });
  return await fetch('/backend/users?' + query, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      logger.log(data);
      return data;
    }
  });
};

const updateUsers = async (users: UserModel, user: User): Promise<boolean> => {
  return await fetch(
    `/backend/users/${users.id}/${
      users.userState == UserState.UNLOCKED ? 'unlock' : 'lock'
    }`,
    {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${user.token}`
      }
    }
  ).then((response) => {
    return response.ok;
  });
};
export default UserPage;
