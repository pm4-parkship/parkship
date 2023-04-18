export interface UserModel {
  id: string;
  name: string;
  surname: string;
  role: UserRole;
  email: string;
}
export enum UserRole {
  user = 'USER',
  admin = 'ADMIN'
}
