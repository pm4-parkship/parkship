import React from 'react';
import TableComponent from '../table/table-component';
import { RowDataType } from '../table/table-row';

const headerNames = ['Name', 'Vorname', 'Email', 'Rolle', 'Status'];

const UsersTable = ({ users }: { users: Array<RowDataType> }) => {
  return (
    <TableComponent data={users} headerNames={headerNames}></TableComponent>
  );
};

export default UsersTable;
