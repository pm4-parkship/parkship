import {
  Box,
  Button,
  ClickAwayListener,
  Grid,
  Modal,
  Tooltip,
  Typography,
  createTheme
} from '@mui/material';
import React from 'react';
import { Icon } from '@iconify/react';

const ShowPasswordModal = ({
  showModal = true,
  setShowModal,
  password
}: {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
  password: string;
}) => {
  const [open, setOpen] = React.useState(false);

  const handleTooltipClose = () => {
    setOpen(false);
  };

  const handleTooltipOpen = async () => {
    navigator.clipboard.writeText(password);
    setOpen(true);
    await delay(5000);
    setOpen(false);
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

            <ClickAwayListener onClickAway={handleTooltipClose}>
              <div>
                <Tooltip
                  PopperProps={{
                    disablePortal: true
                  }}
                  onClose={handleTooltipClose}
                  open={open}
                  disableFocusListener
                  disableHoverListener
                  disableTouchListener
                  title="Kopiert!"
                >
                  <Icon
                    icon="ic:outline-content-copy"
                    onClick={handleTooltipOpen}
                  />
                </Tooltip>
              </div>
            </ClickAwayListener>
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
  boxShadow: 24,
  p: 4
};

function delay(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

export default ShowPasswordModal;
