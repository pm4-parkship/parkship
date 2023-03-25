import React from 'react';
import { useRouter } from 'next/router';
import { Grid } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { logger } from '../../logger';

const MyDummyComponentPage = () => {
  const classes = useStyles();
  const router = useRouter();

  logger.log('router', router);

  return (
    <div className={classes.superRoot}>
      <Grid container justifyContent="center" display="flex">
        Content Me
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

export default MyDummyComponentPage;
