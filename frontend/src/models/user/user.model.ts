export interface UserModel {
  id: string;
  name: string;
  surname: string;
  role: UserRole;
  email: string;
}

export enum UserRole {
  USER = 'USER',
  ADMIN = 'ADMIN'
}

export interface UserDto {
  id: number;
  name: string;
  surname: string;
  email: string;
  username: string;
  userRole: string;
  password: string;
}
