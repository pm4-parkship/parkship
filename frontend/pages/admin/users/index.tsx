import React, { useEffect, useState } from 'react';
import { UserDto, UserModel, UserRole, UserState } from '../../../src/models';
import { toast } from 'react-toastify';
import { Button, Grid, Typography } from '@mui/material';
import { Loading } from '../../../src/components/loading-buffer/loading-buffer';
import UsersTable from '../../../src/components/user-list/users-table';
import UsersFilter from '../../../src/components/user-list/user-filter';
import UserAdministrationModal from 'src/components/user-administration/user-administration-modal/user-administration-modal';
import apiClient from '../../api/api-client';

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
  loading: false,
  result: Array<UserModel>()
};

const UserPage = ({ user }) => {
  const [openUserAdministrationModal, setOpenUserAdministrationModal] =
    useState(false);
  const [filter, setFilter] = useState<UserFilterData>(initFilter);
  const [users, setUsers] = useState(initState);

  const makeUpdate = (userUpdated: UserModel) => {
    apiClient()
      .admin.updateUserState(userUpdated, user)
      .then(() => {
        users.result.map((obj) =>
          obj.id == userUpdated.id ? userUpdated : obj
        );
        setUsers({
          loading: false,
          result: users.result
        });
        toast.success(`Benutzer ${userUpdated.id} erfolgreich aktualisiert`);
      })
      .catch(() => toast.error(`Benutzer ${userUpdated.id} Update fehlerhaft`));
  };
  const handleAddedUser = (data: UserDto) => {
    users.result.push({
      role: UserRole[data.userRole],
      surname: data.surname,
      name: data.name,
      email: data.email,
      userState: UserState.LOCKED,
      id: data.id.toString()
    });
  };

  useEffect(() => {
    setUsers({ loading: true, result: [] });
    apiClient()
      .admin.getAllUsers(user)
      .then((result) => {
        setUsers({ loading: false, result: result });
      })
      .catch(() =>
        toast.error(
          `Die Benutzerliste konnte nicht geladen werden. Versuchen Sie es später nochmal`
        )
      );
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
            <UsersTable users={filteredUsers} updateUser={makeUpdate} />
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
export default UserPage;
