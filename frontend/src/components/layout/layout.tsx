import React, { ReactNode } from 'react';
import makeStyles from '@mui/styles/makeStyles';
import { Toolbar, Typography } from '@mui/material';
import { useTheme } from '@mui/styles';

export type LayoutProps = {
  children: ReactNode;
};

export function Layout({ children }: LayoutProps) {
  const classes = useStyles();
  const theme = useTheme();

  return (
    <>
      <Toolbar className={classes.topBarBottom}>
          <Typography component={'span'}>Header on Top</Typography>
      </Toolbar>
      <main>
        <div className={classes.root}>{children}</div>
      </main>
      <div>Bottom Bar</div>
    </>
  );
}

const useStyles = makeStyles((theme) => ({
  root: {
    maxWidth: '1600px',
    margin: '0 auto'
  },
  topBarBottom: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    overflow: 'hidden',
  },
  toggleBtn: {
    marginRight: 20,
    [theme.breakpoints.down('xs')]: {
      marginRight: 5
    }
  }
}));
