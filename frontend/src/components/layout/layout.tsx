import React, { ReactNode, useContext } from 'react';
import { Button, Typography } from '@mui/material';
import { makeStyles } from '@mui/styles';
import Navbar from '../navbar/navbar';
import { ColorModeContext } from '../../../context';

export type LayoutProps = {
  children: ReactNode;
};

export function Layout({ children }: LayoutProps) {
  const classes = useStyles();
  const colorMode = useContext(ColorModeContext);

  return (
    <>
      <Navbar />
      <main>
        <div className={classes.root}>{children}</div>
      </main>
      <div className={classes.bottomBar}>
        <Button //temporary button to change layouts on the page
          onClick={() => {
            colorMode.toggleColorMode();
          }}
        >
          Change Color
        </Button>
        <Typography variant="body2" color="textSecondary" align="center">
          Created by Parkship
        </Typography>
        <Typography variant="body2" color="textSecondary" align="center">
          {'© '}
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
