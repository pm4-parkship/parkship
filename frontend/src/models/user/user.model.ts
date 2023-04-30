export interface UserModel {
  id?: string;
  name?: string;
  surname?: string;
  roles?: string[];
  email: string;
  username: string;
}
export enum UserRole {
  user = 'USER',
  admin = 'ADMIN'
}
