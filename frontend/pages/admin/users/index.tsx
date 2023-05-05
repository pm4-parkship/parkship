import React, { useState } from 'react';
import {
  ParkingLotModel,
  ParkingLotState,
  UserRole
} from '../../../src/models';
import { logger } from '../../../src/logger';
import { toast } from 'react-toastify';
import { User } from '../../api/user';
import { Button, MenuItem, Select } from '@mui/material';
import UserAdministrationModal from 'src/components/user-administration/user-administration-modal/user-administration-modal';

const UserAdministrationPage = () => {
  const [openUserAdministrationModal, setOpenUserAdministrationModal] =
    useState(false);

  const handleAddedUser = (data) => {
    logger.log(data);
  };

  return (
    <div>
      <h1>Something great will come</h1>
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
      />
    </div>
  );
};

export default UserAdministrationPage;
