export interface UserModel {
  id: string;
  email: string;
  name: string;
  surname: string;
  role: UserRole;
  userState: UserState;
}

export enum UserRole {
  USER = 'USER',
  ADMIN = 'ADMIN'
}

export const UserRoleLabel = new Map<UserRole, string>([
  [UserRole.USER, 'Benutzer'],
  [UserRole.ADMIN, 'Administrator']
]);

export enum UserState {
  UNLOCKED = 'UNLOCKED',
  LOCKED = 'LOCKED'
}

export const UserStateLabel = new Map<UserState, string>([
  [UserState.UNLOCKED, 'freigegeben'],
  [UserState.LOCKED, 'gesperrt']
]);

export interface UserDto {
  id: number;
  name: string;
  surname: string;
  email: string;
  username: string;
  userRole: string;
  password: string;
}
