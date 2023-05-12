import { Typography } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { ReactNode } from 'react';
import { UserRole } from '../../models';
import NavbarAdmin from '../navbar/navbar-admin';
import NavbarUser from '../navbar/navbar-user';
import { User } from 'pages/api/user';

export type LayoutProps = {
  children: ReactNode;
  user?: User;
  signOut: () => Promise<void>;
};

export function Layout({ user, signOut, children }: LayoutProps) {
  const classes = useStyles();

  return (
    <>
      {user?.role === UserRole.USER ? (
        <NavbarUser user={user} signOut={signOut} />
      ) : user?.role === UserRole.ADMIN ? (
        <NavbarAdmin user={user} signOut={signOut} />
      ) : null}
      <main style={{ paddingTop: '1rem' }}>
        <div className={classes.root}>{children}</div>
      </main>
      <div className={classes.bottomBar}>
        <Typography variant="body2" color="primary" align="center">
          {'Created with ❤️'}
        </Typography>
        <Typography variant="body2" color="textPrimary" align="center">
          {' by Parkship © '}
          {new Date().getFullYear()}
        </Typography>
      </div>
    </>
  );
}

const useStyles = makeStyles((theme) => ({
  root: {
    maxWidth: '1600px',
    minHeight: '87vh',
    margin: '0 auto',
    background: theme.palette.background.default,
    display: 'flex',
    flexDirection: 'column'
  },
  topBarBottom: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    overflow: 'hidden'
  },
  bottomBar: {
    display: 'flex',
    justifyContent: 'center'
  }
}));
