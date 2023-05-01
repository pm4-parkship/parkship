import React, { useContext, useState } from 'react';
import {
  Button,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemText,
  Typography
} from '@mui/material';
import Link from '../../link/link';
import { makeStyles } from '@mui/styles';
import { Icon } from '@iconify/react';
import LogoutIcon from '@mui/icons-material/Logout';
import useUser from '../../../auth/use-user';
import { ColorModeContext } from '../../../../context';
import UserAdministrationModal from 'src/components/user-administration/user-administration-modal/user-administration-modal';

function DrawerComponent() {
  const classes = useStyles();
  const [openDrawer, setOpenDrawer] = useState(false);
  const colorMode = useContext(ColorModeContext);
  const { user } = useUser({
    redirectTo: '/login',
    redirectIfFound: false
  });

  /* TODO Safiyya move to pages/admin/users */
  const [openUserAdministrationModal, setOpenUserAdministrationModal] =
    useState(false);
  /* TODO Safiyya move to pages/admin/users */

  return (
    <div className={classes.root}>
      <Drawer open={openDrawer} onClose={() => setOpenDrawer(false)}>
        <List>
          <ListItem onClick={() => setOpenDrawer(false)}>
            <ListItemText>
              <Link href="/search">Parkplatz finden</Link>
            </ListItemText>
          </ListItem>
          <ListItem onClick={() => setOpenDrawer(false)}>
            <ListItemText>
              <Link href="/my-reservation">Meine Reservationen</Link>
            </ListItemText>
          </ListItem>
          <ListItem onClick={() => setOpenDrawer(false)}>
            <ListItemText>
              <Link href="/my-parking-lot">Mein Parkplatz</Link>
            </ListItemText>
          </ListItem>
          <ListItem onClick={() => setOpenDrawer(false)}>
            <ListItemText>
              <Link href="/logout">
                <span className={classes.logout}>
                  <LogoutIcon />
                  Abmelden
                </span>
              </Link>
            </ListItemText>
          </ListItem>
        </List>
      </Drawer>
      <IconButton onClick={() => setOpenDrawer(!openDrawer)}>
        <Icon icon="uil:bars" />
      </IconButton>

      {/* TODO Safiyya move to pages/admin/users – Achtung Button nur ersichtlich, wenn Fenster verkleinert wird */}
      <Button
        variant="contained"
        onClick={() => setOpenUserAdministrationModal(true)}
      >
        Benutzer hinzufügen
      </Button>

      <UserAdministrationModal
        showModal={openUserAdministrationModal}
        setShowModal={setOpenUserAdministrationModal}
      />
      {/* TODO Safiyya move to pages/admin/users */}

      <Typography
        variant="h6"
        className={classes.user}
        onClick={() => {
          colorMode.toggleColorMode();
        }}
      >
        Hi {user?.username}
      </Typography>
    </div>
  );
}

const useStyles = makeStyles(() => ({
  root: {
    display: 'flex',
    gap: '20px',
    flexGrow: '1',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  logout: {
    display: 'flex',
    gap: 6
  },
  user: {
    justifyContent: 'right'
  }
}));

export default DrawerComponent;
