import React from 'react';
import { useRouter } from 'next/router';
import { Button, Grid } from '@mui/material';
import { makeStyles } from '@mui/styles';
import Link from 'src/components/link/link';

const Index = () => {
  const classes = useStyles();
  const router = useRouter();

  return (
    <div className={classes.superRoot}>
      <Grid container justifyContent="center" display="flex">
        <Link
          href={'https://github.com/pm4-parkship/parkship/tree/main/frontend'}
          skipLocaleHandling
        >
          <Button variant="contained">Click Me</Button>
        </Link>

        <Link
          href={{
            pathname: '/info/test',
            query: { locale: 'en' }
          }}
        >
          <Button variant="contained">Click Me Inside</Button>
        </Link>
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
