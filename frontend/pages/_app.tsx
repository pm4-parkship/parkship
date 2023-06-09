/* eslint-disable @typescript-eslint/no-empty-interface */
import React, { useEffect, useMemo, useState } from 'react';
import Head from 'next/head';
import { getDesignTokens } from '../styles/theme/theme';
import createEmotionCache from '../src/emotion-cache/create-emotion-cache';
import { CacheProvider, EmotionCache } from '@emotion/react';
import '../styles/globals.css';
import { Theme as ToastTheme, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { createTheme, Theme, ThemeProvider } from '@mui/material/styles';
import { CssBaseline, responsiveFontSizes } from '@mui/material';
import { AppProps } from 'next/app';

// When using TypeScript 4.x and above
import { Layout } from '../src/components/layout/layout';
import { ColorModeContext } from 'context';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { enGB } from 'date-fns/locale';
import useSession from '../src/auth/use-session';
import { useAuthRedirect } from 'src/auth/use-redirect';
import 'leaflet/dist/leaflet.css';

// Client-side cache, shared for the whole session of the user in the browser.
const clientSideEmotionCache = createEmotionCache();

type AppPropsWithApm = AppProps & {
  emotionCache?: EmotionCache;
};

const App = ({
  Component,
  pageProps,
  emotionCache = clientSideEmotionCache
}: AppPropsWithApm) => {
  const { isInitialized, isSignedIn, user, signIn, signOut } = useSession();
  useAuthRedirect(pageProps, { isInitialized, isSignedIn, user });

  const [mode, setMode] = useState<string>('light');
  const [mounted, setMounted] = useState<boolean>(false);

  useEffect(() => {
    setMounted(true);
  }, [mode]);

  useEffect(() => {
    const mode = localStorage.getItem('mode') || 'light';
    setMode(mode);
  }, []);

  const colorMode = useMemo(
    () => ({
      toggleColorMode: () => {
        setMode((prevMode) => {
          const nextMode = prevMode === 'light' ? 'dark' : 'light';
          localStorage.setItem('mode', nextMode);
          return nextMode;
        });
      }
    }),
    []
  );

  const theme: Theme = useMemo(() => {
    return responsiveFontSizes(createTheme(getDesignTokens(mode)));
  }, [mode]);

  const newPageProps = {
    ...pageProps,
    user,
    signIn,
    signOut
  };

  return (
    <React.Fragment>
      <CacheProvider value={emotionCache}>
        <Head>
          <title>PM4 - Parkship</title>
          <meta
            name="viewport"
            content="minimum-scale=1, initial-scale=1, width=device-width"
          />
          <meta name="description" content="This is our PM4 Project." />
        </Head>
        {mounted && isInitialized && (
          <ColorModeContext.Provider value={colorMode}>
            <ThemeProvider theme={theme}>
              <CssBaseline enableColorScheme>
                <ToastContainer
                  position="bottom-right"
                  autoClose={3000}
                  hideProgressBar={false}
                  newestOnTop={false}
                  closeOnClick
                  rtl={false}
                  pauseOnFocusLoss
                  draggable
                  pauseOnHover
                  limit={5}
                  theme={mode as ToastTheme}
                />
                <LocalizationProvider
                  dateAdapter={AdapterDateFns}
                  adapterLocale={enGB}
                >
                  {/* To avoid flashes when accessing an unauthorized page */}
                  {(pageProps.publicPage || user?.isLoggedIn) && (
                    <Layout user={user} signOut={signOut}>
                      <Component {...newPageProps} />
                    </Layout>
                  )}
                </LocalizationProvider>
              </CssBaseline>
            </ThemeProvider>
          </ColorModeContext.Provider>
        )}
      </CacheProvider>
    </React.Fragment>
  );
};

export default App;

declare module '@mui/styles/defaultTheme' {
  interface DefaultTheme extends Theme {}
}
