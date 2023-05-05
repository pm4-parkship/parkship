import React, { useState } from 'react';
import { logger } from '../../../src/logger';
import user, { User } from '../../api/user';
import { Button } from '@mui/material';
import UserAdministrationModal from 'src/components/user-administration/user-administration-modal/user-administration-modal';

const UserAdministrationPage = ({ user }) => {
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
        user={user}
      />
    </div>
  );
};

export default UserAdministrationPage;
