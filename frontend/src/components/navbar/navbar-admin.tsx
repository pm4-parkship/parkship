import { AppBar, Toolbar, Typography, useMediaQuery } from '@mui/material';
import Link from '../link/link';
import DrawerComponent from './drawer/drawer-admin';
import { makeStyles, useTheme } from '@mui/styles';
import LogoutIcon from '@mui/icons-material/Logout';
import React, { useContext } from 'react';
import { ColorModeContext } from '../../../context';
import { User } from '../../../pages/api/user';
import { useRouter } from 'next/router';
import Image from 'next/image';

function NavbarUser({
  user,
  signOut
}: {
  user?: User;
  signOut: () => Promise<void>;
}) {
  const router = useRouter();
  const classes = useStyles();
  const colorMode = useContext(ColorModeContext);
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  return (
    <AppBar position="static">
      <Toolbar>
        {isMobile ? (
          <DrawerComponent user={user} signOut={signOut} />
        ) : (
          user?.isLoggedIn && (
            <>
              {' '}
              <div className={classes.navlinks}>
                <Link href="/search">
                  <Image
                    src="/parkship-heart.png"
                    alt="logo"
                    width={64}
                    height={64}
                  />
                </Link>
                <Link href="/admin/parking-lots">Parkplatzverwaltung</Link>
                <Link href="/admin/users">Benutzerverwaltung</Link>
              </div>
              <div className={classes.logout}>
                <Typography
                  variant="h6"
                  className={classes.user}
                  onClick={() => {
                    colorMode.toggleColorMode();
                  }}
                >
                  {user?.greeting}
                </Typography>

                <Link href="/logout">
                  <span
                    onClick={async (e) => {
                      e.preventDefault();
                      await signOut();
                      router.push('/login');
                    }}
                  >
                    <LogoutIcon />
                  </span>
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
