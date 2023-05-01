import React, { ReactNode, useEffect } from 'react';
import { Typography } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { useRouter } from 'next/router';
import user from '../../../pages/api/user';
import { logger } from '../../logger';
import useUser from '../../auth/use-user';
import Navbar from '../navbar/navbar-user';
import NavbarAdmin from '../navbar/navbar-admin';

export type LayoutProps = {
  children: ReactNode;
};

export function Layout({ children }: LayoutProps) {
  const classes = useStyles();
  const router = useRouter();
  const { user: UserSession } = useUser();

  useEffect(() => {
    logger.error('Layout user', UserSession);
    if (!UserSession?.isLoggedIn) {
      router.push('/login');
    }
  }, [user]);

  return (
    <>
      <Navbar user={UserSession} />
      <main>
        <div className={classes.root}>{children}</div>
      </main>
      <div className={classes.bottomBar}>
        <Typography variant="body2" color="primary" align="center">
          {'Created with ❤️ '}
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
    margin: '1rem auto',
    background: theme.palette.background.default
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
