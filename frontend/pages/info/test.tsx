import React from 'react';
import { useRouter } from 'next/router';
import { Grid } from '@mui/material';
import { makeStyles } from '@mui/styles';
import MyDummyComponentPage from '../../src/components/test/test';

const Index = () => {
  const classes = useStyles();
  const router = useRouter();

  return (
    <div className={classes.superRoot}>
      <Grid container justifyContent="center" display="flex">
        <MyDummyComponentPage />
      </Grid>
    </div>
  );
};

const useStyles = makeStyles((theme) => ({
  superRoot: {
    maxWidth: '2000px',
    margin: '0 auto',
    animation: '$bounce 2s infinite'
  },
  '@keyframes bounce': {
    '0%': {
      transform: 'translateY(0)'
    },
    '50%': {
      transform: 'translateY(-10px)'
    },
    '100%': {
      transform: 'translateY(0)'
    }
  }
}));

export default Index;
