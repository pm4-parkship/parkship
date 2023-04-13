import React, { useContext, useState } from 'react';
import {
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

function DrawerComponent() {
  const classes = useStyles();
  const [openDrawer, setOpenDrawer] = useState(false);
  const colorMode = useContext(ColorModeContext);
  const { user } = useUser({
    redirectTo: '/login',
    redirectIfFound: false
  });
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
