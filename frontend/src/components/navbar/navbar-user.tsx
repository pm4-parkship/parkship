import { AppBar, Toolbar, Typography, useMediaQuery } from '@mui/material';
import Link from '../link/link';
import DrawerComponent from './drawer/drawer-user';
import { makeStyles, useTheme } from '@mui/styles';
import LogoutIcon from '@mui/icons-material/Logout';
import React, { useContext } from 'react';
import { ColorModeContext } from '../../../context';
import { User } from '../../../pages/api/user';

function NavbarUser({ user }: { user?: User }) {
  const classes = useStyles();
  const colorMode = useContext(ColorModeContext);
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  return (
    <AppBar position="static">
      <Toolbar>
        {isMobile ? (
          <DrawerComponent />
        ) : (
          user?.isLoggedIn && (
            <>
              {' '}
              <div className={classes.navlinks}>
                <Link href="/search">Parkplatz finden</Link>
                <Link href="/my-reservation">Meine Reservation</Link>
                <Link href="/my-parking-lot">Mein Parkplatz</Link>
              </div>
              <div className={classes.logout}>
                <Typography
                  variant="h6"
                  className={classes.user}
                  onClick={() => {
                    colorMode.toggleColorMode();
                  }}
                >
                  Willkommen {user?.username}
                </Typography>

                <Link href="/logout">
                  <LogoutIcon />
                </Link>
              </div>
            </>
          )
        )}
      </Toolbar>
    </AppBar>
  );
}

const useStyles = makeStyles((theme) => ({
  navlinks: {
    marginLeft: theme.spacing(5),
    display: 'flex',
    gap: '20px',
    flexGrow: '1',
    justifyContent: 'center',
    alignItems: 'center'
  },
  logout: {
    gap: theme.spacing(10),
    display: 'flex',
    flexGrow: '1',
    justifyContent: 'right',
    alignItems: 'center'
  },
  logo: {
    flexGrow: '1',
    cursor: 'pointer'
  },
  user: {
    justifyContent: 'right'
  },
  link: {
    textDecoration: 'none',
    color: 'white',
    fontSize: '20px',
    marginLeft: theme.spacing(20),
    '&:hover': {
      color: 'yellow',
      borderBottom: '1px solid white'
    }
  }
}));

export default NavbarUser;