import React, { useEffect, useState } from 'react';
import TableComponent, { SortOrder } from '../table/table-component';
import { RowDataType } from '../table/table-row';
import { UserModel, UserRoleLabel, UserState } from '../../models';
import UserStateToggleButton from './user-state-toggle-button';

const headerNames = ['Name', 'Vorname', 'Email', 'Rolle', 'Status'];

const UsersTable = ({
  users,
  updateUser
}: {
  users: Array<UserModel>;
  updateUser: (user: UserModel) => void;
}) => {
  const [sortFunction, setSortFunction] =
    useState<(a: UserModel, b: UserModel) => number>();

  const [sortOrder, setSortOrder] = useState<number>(SortOrder.asc);
  const changeSorting = (colName: string) => {
    setSortOrder(() =>
      sortOrder == SortOrder.desc ? SortOrder.asc : SortOrder.desc
    );
    let fun;
    switch (colName) {
      case 'ID':
        fun = (a: UserModel, b: UserModel): number =>
          (a.id > b.id ? 1 : -1) * sortOrder;
        break;
      case 'Name':
        fun = (a: UserModel, b: UserModel): number =>
          (a.name > b.name ? 1 : -1) * sortOrder;
        break;
      case 'Vorname':
        fun = (a: UserModel, b: UserModel): number =>
          (a.surname > b.surname ? 1 : -1) * sortOrder;
        break;
      case 'Email':
        fun = (a: UserModel, b: UserModel): number =>
          (a.email > b.email ? 1 : -1) * sortOrder;
        break;
      case 'Rolle':
        fun = (a: UserModel, b: UserModel): number =>
          (a.role > b.role ? 1 : -1) * sortOrder;
        break;
      case 'Status':
        fun = (a: UserModel, b: UserModel): number =>
          (a.userState > b.userState ? 1 : -1) * sortOrder;
        break;
      default:
    }
    setSortFunction(() => fun);
  };
  const updateState = (selected) => {
    const user = users.find((value) => value.id == selected.id);

    if (!user) return;
    user.userState =
      user.userState == UserState.LOCKED
        ? UserState.UNLOCKED
        : UserState.LOCKED;
    updateUser(user);
  };

  const mapped: Array<RowDataType> = users.sort(sortFunction).map((user) => {
    return [
      `${user.name}`,
      `${user.surname}`,
      `${user.email}`,
      `${UserRoleLabel.get(user.role)}`,
      <UserStateToggleButton user={user} changeUserState={updateState} />
    ];
  });

  useEffect(() => {
    changeSorting('Status');
  }, []);
  return (
    <TableComponent
      data={mapped}
      headerNames={headerNames}
      paginationLabel={'Benutzer pro Seite'}
      onColumnClick={changeSorting}
    ></TableComponent>
  );
};

export default UsersTable;
