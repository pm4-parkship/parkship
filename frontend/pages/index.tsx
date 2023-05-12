import React from 'react';
import { Grid } from '@mui/material';
import { makeStyles } from '@mui/styles';
import SearchPage from './search';

const Index = ({ user }) => {
  const classes = useStyles();

  return (
    <div className={classes.superRoot}>
      <Grid container justifyContent="center" display="flex">
        <Grid item xs={12}>
          {user?.isLoggedIn && <SearchPage user={user} />}
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
