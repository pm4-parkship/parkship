import { Box, Button, Modal, Typography } from '@mui/material';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import ImageList from '@mui/material/ImageList';
import ImageListItem from '@mui/material/ImageListItem';
import React from 'react';
import ImageCustom from './image/image-custom';

const style = {
  position: 'absolute' as const,
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '90%',
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  padding: '30px',
  pt: 2,
  px: 4,
  pb: 3
};

const ParkingDetailModal = () => {
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };

  return (
    <>
      <Button onClick={handleOpen}>Open modal</Button>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <TableContainer component={Paper}>
            <Table
              sx={{ minWidth: '80%' }}
              aria-label="custom pagination table"
            >
              <TableHead>
                <TableRow>Parkplatztitel</TableRow>
              </TableHead>
              <TableBody>
                <TableRow>
                  <TableCell
                    component="th"
                    style={{ width: '10%' }}
                    scope="row"
                  >
                    {'Besitzer:'}
                  </TableCell>
                  <TableCell style={{ width: '40%' }} align="left">
                    {'TODO: Benjamin Blümchen'}
                  </TableCell>
                  <TableCell style={{ width: '10%' }} align="left">
                    {'Frei:'}
                  </TableCell>
                  <TableCell style={{ width: '40%' }} align="left">
                    {'TODO Wochentaganzeige'}
                  </TableCell>
                </TableRow>

                <TableRow>
                  <TableCell
                    component="th"
                    style={{ width: '10%' }}
                    scope="row"
                  >
                    {'Kontakt:'}
                  </TableCell>
                  <TableCell style={{ width: '40%' }} align="left">
                    {'todo +41 79 123 45 67'}
                  </TableCell>
                  <TableCell style={{ width: '10%' }} align="left">
                    {'Kosten:'}
                  </TableCell>
                  <TableCell style={{ width: '40%' }} align="left">
                    {'TODO Costs'}
                  </TableCell>
                </TableRow>

                <TableRow>
                  <TableCell
                    component="th"
                    style={{ width: '10%' }}
                    scope="row"
                  >
                    {/* placeholder */}
                  </TableCell>
                  <TableCell style={{ width: '40%' }} align="left">
                    {'todo emailadresse'}
                  </TableCell>
                </TableRow>

                <TableRow>
                  <TableCell
                    component="th"
                    style={{ width: '10%' }}
                    scope="row"
                  >
                    {'Wo:'}
                  </TableCell>
                  <TableCell style={{ width: '40%' }} align="left">
                    {'todo adresse #2'}
                  </TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </TableContainer>

          <Paper elevation={0}>
            haldsfjöalskjdf ölsjdf ölasd fölkasd fölas dfölasjdfölfsd{' '}
          </Paper>

          <Box
            component="img"
            sx={{
              height: '50%',
              width: '50%',
              maxHeight: { xs: 200, md: 200 },
              maxWidth: { xs: 300, md: 300 }
            }}
            alt="Placeholder - Google Maps location"
            src="https://www.google.com/maps/d/u/0/thumbnail?mid=1vv5kWqPmMMS0ST9S3N3oGNcFwrY&hl=en"
          />

          <Box
            component="img"
            sx={{
              height: '50%',
              width: '50%',
              maxHeight: { xs: 200, md: 200 }
            }}
            alt="Picture - Location"
            src="https://media.istockphoto.com/id/1301845245/de/vektor/parkplatzmangel.jpg?s=612x612&w=0&k=20&c=T1uRB1ufOTXQEM-34S9Zhq2LmqXMO9VztHWCOn041sE="
          />

          <Box width={'100%'}>
          <Button
            onClick={() => {
              //Todo reservation
            }}
            style={{ float: 'right' }}
          >
            reservieren
          </Button>
          </Box>
        </Box>
      </Modal>
    </>
  );
};

export default ParkingDetailModal;
