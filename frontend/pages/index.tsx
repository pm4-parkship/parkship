import React from 'react';
import { useRouter } from 'next/router';
import { Button, Grid, Paper, Typography } from '@mui/material';
import { Icon } from '@iconify/react';
import { makeStyles } from '@mui/styles';
import ImageCustom from '../src/components/image/image-custom';

const Index = () => {
  const classes = useStyles();
  const router = useRouter();

  return (
    <div className={classes.superRoot}>
      <Grid
        className={classes.bottomButtons}
        container
        justifyContent="center"
        display="flex"
      >
        <Grid item sm={3} xs={12}>
          <Paper className={classes.buildPaper}>
            <Icon className={classes.iconStats} icon="gridicons:create" />
            <Typography component={'span'} variant="h4">
              Landing
            </Typography>
          </Paper>
        </Grid>
        <Grid item sm={3} xs={12}>
          <Paper className={classes.buildPaper}>
            <Icon className={classes.iconStats} icon="wpf:create-new" />
            <Typography component={'span'} variant="h4">
              Experience
            </Typography>
          </Paper>
        </Grid>
        <Grid item sm={3} xs={12}>
          <Paper className={classes.buildPaper}>
            <Icon className={classes.iconStats} icon="akar-icons:lock-on" />
            <Typography component={'span'} variant="h4">
              Test Me
            </Typography>
          </Paper>
        </Grid>
        <Grid item sm={3} xs={12}>
          <Paper className={classes.buildPaper}>
            <Icon className={classes.iconStats} icon="tabler:parachute" />
            <Typography component={'span'} variant="h4">
              Today
            </Typography>
          </Paper>
        </Grid>
      </Grid>
    </div>
  );
};

const useStyles = makeStyles((theme) => ({
  superRoot: {
    maxWidth: '2000px',
    margin: '0 auto'
  },
  root: {
    alignItems: 'center',
    maxHeight: '600px',
    height: '65vh',
    backgroundPosition: 'center',
    [theme.breakpoints.down('md')]: {
      backgroundPosition: 'initial',
      height: '55vh'
    },
    textAlign: 'center'
  },
  imageSpinner: {
    width: '345px',
    height: '325px',
    [theme.breakpoints.down('md')]: {
      width: '150px',
      height: '150px'
    }
  },
  bottomButtons: {
    marginBottom: '50px',
    alignItems: 'center',
    maxWidth: '2000px',
    height: '15vh',
    textAlign: 'center',
    [theme.breakpoints.down('md')]: {
      height: '40vh'
    }
  },
  subBarTitle: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center'
  },
  buildPaper: {
    background: `linear-gradient(270deg, ${theme.palette.primary.main} 20%, ${theme.palette.secondary.main} 80%)`,
    borderRadius: '10px',
    margin: '10px',
    color: 'white',
    padding: '20px',
    textAlign: 'center',
    display: 'flex',
    justifyContent: 'space-evenly',
    alignItems: 'center',
    flexDirection: 'row',
    '&:hover': {
      margin: '50px',
      transition: 'all 0.5s ease',
      borderRadius: '20px'
    }
  },
  icon: {
    marginLeft: '10px',
    width: '15px',
    height: '15px',
    [theme.breakpoints.up('md')]: { width: '25px', height: '25px' }
  },
  iconStats: {
    width: '45px',
    height: '45px',
    [theme.breakpoints.down('md')]: { width: '25px', height: '25px' }
  },
  outerStatsBox: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center'
  },
  statsBox: {
    width: '50px',
    height: '50px',
    display: 'grid',
    borderRadius: '20%',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: '10px',
    backgroundColor: theme.palette.primary.light,
    [theme.breakpoints.down('md')]: {
      marginTop: '20px'
    }
  },
  statsContainer: {
    display: 'flex',
    justifyContent: 'space-around',
    alignItems: 'center',
    padding: '20px'
  }
}));

export default Index;
