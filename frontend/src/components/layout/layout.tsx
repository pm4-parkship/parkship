import React, { ReactNode } from 'react';
import { Toolbar } from '@mui/material';
import { makeStyles } from '@mui/styles';

export type LayoutProps = {
  children: ReactNode;
};

export function Layout({ children }: LayoutProps) {
  const classes = useStyles();

  return (
    <>
      <Toolbar className={classes.topBarBottom}></Toolbar>
      <main>
        <div className={classes.root}>{children}</div>
      </main>
      <div className={classes.toggleBtn}>Bottom Bar</div>
    </>
  );
}

const useStyles = makeStyles((theme) => ({
  root: {
    maxWidth: '1600px',
    margin: '0 auto',
    background: theme.palette.background.default
  },
  topBarBottom: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    overflow: 'hidden'
  },
  toggleBtn: {
    marginRight: 20,
    display: 'block',
    [theme.breakpoints.down('md')]: {
      marginRight: 5,
      display: 'none'
    }
  }
}));
