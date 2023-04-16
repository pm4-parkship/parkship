import React, { ReactNode } from 'react';
import { Typography } from '@mui/material';
import { makeStyles } from '@mui/styles';
import Navbar from '../navbar/navbar';

export type LayoutProps = {
  children: ReactNode;
};

export function Layout({ children }: LayoutProps) {
  const classes = useStyles();

  return (
    <>
      <Navbar />
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
    minHeight: '100vh',
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
