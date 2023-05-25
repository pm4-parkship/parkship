
import React, { useState } from 'react';
import { Grid, Link, Typography } from '@mui/material';
import { CreateParkingModal } from 'src/components/create-parking-modal/create-parking-modal';
import { logger } from 'src/logger';


const CreatePage = ({ user }) => {

  return (
    <Grid padding={2}>
        <CreateParkingModal addParkingLot={() => logger.log("tried...")} owner={user.username}/>
    </Grid>
  );
};


export default CreatePage;
