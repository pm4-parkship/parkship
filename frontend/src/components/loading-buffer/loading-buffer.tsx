import React from 'react';
import { CircularProgress } from '@mui/material';
import { makeStyles } from '@mui/styles';

export const Loading = ({ loading }) => {
  const classes = useStyles();
  return loading ? (
    <div className={classes.root}>
      <CircularProgress size={60} />
    </div>
  ) : null;
};

const useStyles = makeStyles(() => ({
  root: {
    textAlign: 'center'
  }
}));
