/* eslint-disable @typescript-eslint/no-empty-interface */
import React, {useEffect, useState} from 'react';
import Head from 'next/head';

import {getDesignTokens} from '../styles/theme/theme';
import createEmotionCache from '../src/emotion-cache/create-emotion-cache';
import {CacheProvider, EmotionCache} from '@emotion/react';
import '../styles/globals.css';
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import {createTheme, Theme, ThemeProvider} from '@mui/material/styles';
import {CssBaseline, responsiveFontSizes} from '@mui/material';
import {AppProps} from 'next/app';

// When using TypeScript 4.x and above
import {Layout} from '../src/components/layout/layout';

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
  const [mode, setMode] = useState('dark');
  const [mounted, setMounted] = useState(false);


  useEffect(() => {
    setMounted(true);
  }, [mode]);


  const theme: Theme = React.useMemo(() => {
    return responsiveFontSizes(createTheme(getDesignTokens(mode)));
  }, [mode]);

  return (
      <React.Fragment>
          <CacheProvider value={emotionCache}>
            <Head>
              <title>PM4 - Parkship</title>
              <meta
                  name="viewport"
                  content="minimum-scale=1, initial-scale=1, width=device-width"
              />
              <meta
                  name="description"
                  content="This is a project."
              />
            </Head>
            {mounted && (
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
                        />
                        <Layout>
                          <Component {...pageProps} />
                        </Layout>
                      </CssBaseline>
                  </ThemeProvider>
            )}
          </CacheProvider>
      </React.Fragment>
  );
};

export default App;

declare module '@mui/styles/defaultTheme' {
  interface DefaultTheme extends Theme {}
}
