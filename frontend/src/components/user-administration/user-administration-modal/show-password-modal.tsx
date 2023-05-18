import { Box, Button, Grid, Modal, Typography } from '@mui/material';
import { Icon } from '@iconify/react';

import React from 'react';
import { toast } from 'react-toastify';

const ShowPasswordModal = ({
  showModal = true,
  setShowModal,
  password
}: {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
  password: string;
}) => {
  const showToast = async () => {
    navigator.clipboard.writeText(password);
    toast.success('Password copied');
  };

  return (
    <div>
      <Modal open={showModal}>
        <Box sx={style}>
          <Typography align="center" component="h1" variant="h5">
            Initialpasswort
          </Typography>

          <Grid
            container
            justifyContent="center"
            columnGap={'10px'}
            style={{ marginTop: '20px' }}
          >
            {password}
            <Icon icon="ic:outline-content-copy" onClick={showToast} />
          </Grid>

          <Grid container justifyContent="center">
            <Button
              type={'submit'}
              variant={'contained'}
              sx={{
                width: '50%',
                marginTop: '20px'
              }}
              onClick={() => setShowModal(false)}
            >
              OK
            </Button>
          </Grid>
        </Box>
      </Modal>
    </div>
  );
};

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'white',
  border: '2px solid #000',
  borderRadius: 4,
  boxShadow: 24,
  p: 4
};

export default ShowPasswordModal;
