import React, { useEffect, useState } from 'react';
import {
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Select,
  SelectChangeEvent,
  TextField
} from '@mui/material';
import { UserFilterData } from '../../../pages/admin/users';
import { logger } from '../../logger';
import {
  UserRole,
  UserRoleLabel,
  UserState,
  UserStateLabel
} from '../../models';

interface UserFilterProps {
  updateFilter: (data: UserFilterData) => void;
}

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 250
    }
  }
};

const UsersFilter = ({ updateFilter }: UserFilterProps) => {
  const [searchTerm, setSearchTerm] = useState<string>('');

  const [userState, setUserState] = React.useState<string[]>([]);

  const [userRole, setUserRole] = React.useState<string[]>([]);

  const handleChangeRole = (event: SelectChangeEvent<typeof userRole>) => {
    logger.log(event);
    const {
      target: { value }
    } = event;
    setUserRole(typeof value === 'string' ? value.split(',') : value);
  };

  const handleChangeState = (event: SelectChangeEvent<typeof userState>) => {
    logger.log(event);
    const {
      target: { value }
    } = event;
    setUserState(typeof value === 'string' ? value.split(',') : value);
  };

  useEffect(() => {
    const states = new Set(
      userState.map((value) => UserState[value as keyof typeof UserState])
    );

    const roles = new Set(
      userRole.map((value) => UserRole[value as keyof typeof UserRole])
    );

    updateFilter({ states: states, roles: roles, searchTerm: searchTerm });
  }, [userState, userRole, searchTerm]);

  return (
    <Grid
      container
      justifyContent="space-between"
      display="flex"
      spacing={1}
      rowSpacing={2}
      padding={2}
    >
      <Grid item md={5} lg={5} xl={5} sm={8}>
        <TextField
          label={'Suchbegriff'}
          fullWidth={true}
          value={searchTerm}
          onChange={(event) => setSearchTerm(event.currentTarget.value)}
        />
      </Grid>
      <Grid item xs={3}>
        <FormControl fullWidth>
          <InputLabel id="user-role-label">Benutzerrolle</InputLabel>
          <Select
            labelId="user-role-label"
            id="user-role"
            multiple
            value={userRole}
            onChange={handleChangeRole}
            input={<OutlinedInput label="Benutzerrolle" />}
            MenuProps={MenuProps}
            fullWidth
          >
            {Object.entries(UserRole).map(([key, role]) => (
              <MenuItem key={key} value={key}>
                {UserRoleLabel.get(role)}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Grid>

      <Grid item xs={3}>
        <FormControl fullWidth>
          <InputLabel id="user-state-label">Benutzerstatus</InputLabel>
          <Select
            labelId="user-state-label"
            id="user-state"
            multiple
            value={userState}
            onChange={handleChangeState}
            input={<OutlinedInput label="Benutzerstatus" />}
            MenuProps={MenuProps}
            fullWidth
          >
            {Object.entries(UserState).map(([key, state]) => (
              <MenuItem key={key} value={key}>
                {UserStateLabel.get(state)}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Grid>
    </Grid>
  );
};

export default UsersFilter;
