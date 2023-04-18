import React from 'react';
import { makeStyles } from '@mui/styles';

const LogoutPage = () => {
  const classes = useStyles();
  return <div className={classes.superRoot}>You got logged out</div>;
};

const useStyles = makeStyles(() => ({
  superRoot: {
    maxWidth: '2000px',
    margin: '0 auto'
  }
}));

export default LogoutPage;
