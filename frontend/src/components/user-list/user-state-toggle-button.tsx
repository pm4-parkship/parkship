import { ToggleButton, ToggleButtonGroup, Typography } from '@mui/material';
import React from 'react';
import { UserModel, UserState } from '../../models';

interface userStateToggleButtonProps {
  user: UserModel;
  changeUserState: (item: UserModel) => void;
}

const UserStateToggleButton = ({
  user,
  changeUserState
}: userStateToggleButtonProps): JSX.Element => {
  return (
    <ToggleButtonGroup
      value={user.userState}
      exclusive
      onChange={() => changeUserState(user)}
    >
      <ToggleButton
        value={UserState.UNLOCKED}
        color={'success'}
        disabled={user.userState == UserState.UNLOCKED}
        style={{ minWidth: 150 }}
      >
        {releasedText(user.userState)}
      </ToggleButton>
      <ToggleButton
        value={UserState.LOCKED}
        color={'error'}
        disabled={user.userState == UserState.LOCKED}
        style={{ minWidth: 150 }}
      >
        {lockedText(user.userState)}
      </ToggleButton>
    </ToggleButtonGroup>
  );
};

const lockedText = (state: UserState) =>
  state == UserState.LOCKED ? (
    <Typography>gesperrt</Typography>
  ) : (
    <Typography>sperren</Typography>
  );
const releasedText = (state: UserState) =>
  state === UserState.UNLOCKED ? (
    <Typography>freigegeben</Typography>
  ) : (
    <Typography>freigeben</Typography>
  );
export default UserStateToggleButton;
