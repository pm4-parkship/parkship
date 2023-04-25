export interface UserModel {
  id: string;
  name: string;
  surname: string;
  roles: UserRoles[];
  email: string;
}

export enum UserRoles {
    ADMIN = 'ADMIN',
  USER = 'USER',
}
