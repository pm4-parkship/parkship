import React, {useState} from 'react';
import { logger } from '../../src/logger';
import useUser from '../../src/auth/use-user';
import { toast } from 'react-toastify';
import { ParkingLotModel, UserRoles } from '../../src/models';
import { Button, MenuItem, Select } from '@mui/material';
import {User} from "../api/user";

const MyAdminPanel = () => {
  const { user } = useUser();

  const newParkingLot: ParkingLotModel = {
    latitude: 0,
    longitude: 0,
    nr: '',
    address: 'My Address in the backend',
    addressNr: '123',
    description: 'This is my most favourite parking lot',
    floor: 123,
    owner: '4',
    pictures: [],
    price: 19999,
    state: 'Lucerne',
    tags: ['new', 'parking', 'lot']
  };

  const createNewParkingLot = () => {
    if (user) {
      fetch('/backend/parking-lot', {
        method: 'POST',
        body: JSON.stringify(newParkingLot),
        headers: {
          Authorization: `Bearer ${user.token}`,
          'Content-Type': 'application/json'
        }
      }).then(async (response) => {
        const data = await response.json();
        logger.log(data);
        return data;
      });
    } else {
      logger.log('User not logged in');
      toast.error('User not logged in');
    }
  };

  const [newUser, setNewUser] = useState<User>();


  return (
    <div>
      <h1>Something great will come</h1>
      <Button onClick={createNewParkingLot}>Click me</Button>
      <Select
        value={'age'}
        label="User Roles"
        onChange={(value) => logger.log(value)}
      >
        {Object.keys(UserRoles).map((role) => {
          return <MenuItem value={role}>{role}</MenuItem>;
        })}
      </Select>
    </div>
  );
};

export default MyAdminPanel;
