import React from 'react';
import { Grid } from '@mui/material';
import { makeStyles } from '@mui/styles';
import SearchPage from './search';

const Index = () => {
  const classes = useStyles();

  return (
    <div className={classes.superRoot}>
      <Grid container justifyContent="center" display="flex" spacing={2}>
        <Grid item xs={12}>
          <SearchPage></SearchPage>
        </Grid>
      </Grid>
    </div>
  );
};

const useStyles = makeStyles((theme) => ({
  superRoot: {
    maxWidth: '2000px',
    margin: '0 auto'
  }
}));

export default Index;
