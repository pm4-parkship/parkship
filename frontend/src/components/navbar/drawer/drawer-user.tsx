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
import { ColorModeContext } from '../../../../context';
import { useRouter } from 'next/router';
import Image from 'next/image';

function DrawerComponent({ user, signOut }) {
  const classes = useStyles();
  const router = useRouter();
  const [openDrawer, setOpenDrawer] = useState(false);
  const colorMode = useContext(ColorModeContext);
  return (
    <div className={classes.root}>
      <Drawer open={openDrawer} onClose={() => setOpenDrawer(false)}>
        <List>
          <ListItem onClick={() => setOpenDrawer(false)}>
            <ListItemText style={{ display: 'flex', justifyContent: 'center' }}>
              <Link href="/search">
                <Image
                  src="/parkship-heart.png"
                  alt="logo"
                  width={64}
                  height={64}
                />
              </Link>
            </ListItemText>
          </ListItem>
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
                <span
                  className={classes.logout}
                  onClick={async (e) => {
                    e.preventDefault();
                    await signOut();
                    router.push('/login');
                  }}
                >
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
        {user?.greeting}
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
